package com.moparisthebest.classgen;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * For sending java source as strings to a Compiler
 * @see Compiler
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
	private final CharSequence code;

	public StringJavaFileObject(final String name, final CharSequence code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
