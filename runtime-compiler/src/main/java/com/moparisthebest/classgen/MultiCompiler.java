package com.moparisthebest.classgen;

import javax.tools.JavaFileObject;

/**
 * This lets you compile multiple source files at once
 */
public class MultiCompiler extends Compiler {

	public static Compiler instance = new MultiCompiler();

	public MultiCompiler() {
		super(true);
	}

	@Override
	public ClassLoader compile(final Iterable<? extends JavaFileObject> source) {
		return super.compile(source);
	}

	@Override
	public ClassLoader compile(final JavaFileObject... source) {
		return super.compile(source);
	}

	@Override
	public <T> T compile(final String className, final JavaFileObject... source) {
		return super.compile(className, source);
	}

	@Override
	public <T> T compile(final String className, final Iterable<? extends JavaFileObject> source) {
		return super.compile(className, source);
	}
}
