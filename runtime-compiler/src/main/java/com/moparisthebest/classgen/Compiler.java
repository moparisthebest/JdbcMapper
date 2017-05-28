package com.moparisthebest.classgen;


import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This let's you take compile and then run Java source code at runtime, this class only allows you to compile 1 class
 * at a time.
 * <p>
 * This is not thread safe, though it could be extended and made thread safe, or locked around.
 *
 * @author moparisthebest
 * @see MultiCompiler for compiling multiple classes at once
 * <p>
 * The original idea was taken from:
 * http://mindprod.com/jgloss/javacompiler.html#SAMPLECODE
 */
public class Compiler {

	private final JavaCompiler compiler;
	private final MemoryJavaFileManager mjfm;
	//private final ClassLoader classLoader;
	private final List<JavaFileObject> singleton = Arrays.asList(new JavaFileObject[1]);

	public Compiler() {
		this(false);
	}

	protected Compiler(final boolean multi) {
		compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null)
			throw new RuntimeException("tools.jar needs to be on classpath to compile code at runtime");
		final JavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		mjfm = multi ? new MultiMemoryJavaFileManager(fileManager) : new SingleMemoryJavaFileManager(fileManager);
		//classLoader = new MemoryClassLoader(mjfm);
	}

	protected ClassLoader compile(final Iterable<? extends JavaFileObject> source) {
		//final MemoryJavaFileManager mjfm = new MemoryJavaFileManager(compiler.getStandardFileManager(null, null, null));
		final JavaCompiler.CompilationTask task = compiler.getTask(null, mjfm, null, null, null, source);
		return task.call() ?
				new MemoryClassLoader(mjfm)
				//classLoader
				: null;
	}

	protected ClassLoader compile(final JavaFileObject... source) {
		return compile(Arrays.asList(source));
	}

	public ClassLoader compile(final JavaFileObject source) {
		singleton.set(0, source);
		return compile(singleton);
	}

	protected <T> T compile(final String className, final Iterable<? extends JavaFileObject> source) {
		// compile item
		final ClassLoader cl = compile(source);
		if (cl == null)
			throw new RuntimeException("Error compiling class, aborting...");
		return instantiate(cl, className);
	}

	protected <T> T compile(final String className, final JavaFileObject... source) {
		return compile(className, Arrays.asList(source));
	}

	public <T> T compile(final String className, final JavaFileObject source) {
		singleton.set(0, source);
		return compile(className, singleton);
	}

	public <T> T compile(final String className, final CharSequence code) {
		return compile(className, new StringJavaFileObject(className, code));
	}

	public <T> T instantiate(final ClassLoader cl, final String className) {
		try {
			// Load class and create an instance.
			final Class<?> calcClass = cl.loadClass(className);
			@SuppressWarnings("unchecked")            final T ret = (T) calcClass.newInstance();
			return ret;
		} catch (Exception e) {
			throw new RuntimeException("Error finding or instantiating class, exiting...", e);
		}
	}

}

class MemoryJavaFileObject extends ForwardingJavaFileObject<JavaFileObject> {

	final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	MemoryJavaFileObject(JavaFileObject fileObject) {
		super(fileObject);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return baos;
	}
}

interface MemoryJavaFileManager extends JavaFileManager {
	MemoryJavaFileObject getMemoryJavaFileObject(final String name);
}

class SingleMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> implements MemoryJavaFileManager {

	private MemoryJavaFileObject classes = null;

	SingleMemoryJavaFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}

	@Override
	public MemoryJavaFileObject getMemoryJavaFileObject(final String name) {
		final MemoryJavaFileObject ret = classes;
		classes = null;
		return ret;
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
		final MemoryJavaFileObject jfo = new MemoryJavaFileObject(super.getJavaFileForOutput(location, className, kind, sibling));
		//System.out.printf("MemoryJavaFileManager.getJavaFileForOutput(%s, %s, %s, %s): %s\n", location, className, kind, sibling, jfo);
		classes = jfo;
		return jfo;
	}
}

class MultiMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> implements MemoryJavaFileManager {

	private Map<String, MemoryJavaFileObject> classes = new HashMap<String, MemoryJavaFileObject>();

	MultiMemoryJavaFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}

	@Override
	public MemoryJavaFileObject getMemoryJavaFileObject(final String name) {
		return classes.remove(name);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
		final MemoryJavaFileObject jfo = new MemoryJavaFileObject(super.getJavaFileForOutput(location, className, kind, sibling));
		//System.out.printf("MemoryJavaFileManager.getJavaFileForOutput(%s, %s, %s, %s): %s\n", location, className, kind, sibling, jfo);
		classes.put(className, jfo);
		return jfo;
	}
}

class MemoryClassLoader extends ClassLoader {
	final MemoryJavaFileManager jfm;

	MemoryClassLoader(MemoryJavaFileManager jfm) {
		this.jfm = jfm;
	}

	public Class findClass(String name) throws ClassNotFoundException {
		final MemoryJavaFileObject jfo = jfm.getMemoryJavaFileObject(name);
		if (jfo == null)
			throw new ClassNotFoundException("Class '" + name + "' cannot be found in the MemoryJavaFileManager");
		final byte[] b = jfo.baos.toByteArray();
		return defineClass(name, b, 0, b.length);
	}
}
