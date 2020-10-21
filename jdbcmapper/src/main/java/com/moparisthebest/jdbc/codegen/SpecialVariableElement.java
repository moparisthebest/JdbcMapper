package com.moparisthebest.jdbc.codegen;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;

import static com.moparisthebest.jdbc.codegen.CompileTimeRowToObjectMapper.getAllImplementedTypes;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.booleanType;
import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.types;

/**
 * Created by mopar on 6/1/17.
 */
class SpecialVariableElement extends DelegatingVariableElement {

	enum SpecialType {
		BIND_IN_LIST,
		IN_LIST,
		CLOB,
		BLOB,
		SQL,
		STR_BOOLEAN,
		PLAIN,
	}

	final SpecialType specialType;
	final String blobStringCharset;
	final int index;
	final boolean iterable, bindable, allowReflection;
	
	TypeMirror type;

	String name, componentTypeString;

	SpecialVariableElement(final boolean allowReflection, final VariableElement delegate, final String paramName, final int indexOfFirstPeriod, final SpecialType specialType) {
		this(allowReflection, delegate, paramName, indexOfFirstPeriod, specialType, null, 0);
	}

	SpecialVariableElement(final boolean allowReflection, final VariableElement delegate, final String paramName, final int indexOfFirstPeriod, final SpecialType specialType, final int index) {
		this(allowReflection, delegate, paramName, indexOfFirstPeriod, specialType, null, index);
	}

	SpecialVariableElement(final boolean allowReflection, final VariableElement delegate, final String paramName, final int indexOfFirstPeriod, final SpecialType specialType, final String blobStringCharset) {
		this(allowReflection, delegate, paramName, indexOfFirstPeriod, specialType, blobStringCharset, 0);
	}

	SpecialVariableElement(final boolean allowReflection, final VariableElement delegate, final String paramName, final int indexOfPeriod, final SpecialType specialType, final String blobStringCharset, final int index) {
		super(delegate);
		this.allowReflection = allowReflection;
		this.specialType = specialType;
		this.blobStringCharset = blobStringCharset;
		this.index = index;
		if(indexOfPeriod == -1) {
			// no recursion or anything complicated, straight parameter
			this.name = paramName;
			this.type = delegate.asType();
		} else {
			final String[] paramSplit = paramName.split("\\s*\\.\\s*");
			if(paramSplit.length < 2) {
				JdbcMapperProcessor.messager.printMessage(Diagnostic.Kind.ERROR, "paramName invalid with period at end: " + paramName, delegate);
				throw new RuntimeException("paramName invalid with period at end: " + paramName);
			}
			final StringBuilder sb = new StringBuilder();
			appendVar(paramSplit[0], sb);
			this.type = delegate.asType();
			for(int x = 1; x < paramSplit.length; ++x) {
				appendVar(paramSplit[x], sb);
			}
			this.name = sb.toString();
		}
		this.iterable = specialType == SpecialType.SQL && types.isAssignable(delegate.asType(), JdbcMapperProcessor.iterableType);
		this.bindable = !this.iterable && specialType == SpecialType.SQL && types.isAssignable(delegate.asType(), JdbcMapperProcessor.bindableType);
	}

	public void appendVar(final String fullParam, final StringBuilder sb) {
		final boolean nullSafe = fullParam.endsWith("?");
		final String param = nullSafe ? fullParam.substring(0, fullParam.length() - 1) : fullParam;
		// hack for first param
		if(type != null) {
			final String name = getGetterTypeAppendString(param, sb);
			if(name == null) {
				JdbcMapperProcessor.messager.printMessage(Diagnostic.Kind.ERROR, "recursive param not found: " + param, delegate);
			}
			sb.append(name);
		} else {
			sb.append(param);
		}
		if (nullSafe) {
			final String var = sb.toString();
			sb.setLength(0);
			sb.append("((").append(var).append(") == null ? null : (").append(var).append("))");
		}
	}

	public String getGetterTypeAppendString(final String fieldName, final StringBuilder sb) {

		if(type.getKind() != TypeKind.DECLARED) {
			JdbcMapperProcessor.messager.printMessage(Diagnostic.Kind.ERROR, "type " + type + " not TypeKind.DECLARED ?? how?? fieldName: " + fieldName, delegate);
			return "";
		}

		final DeclaredType declaredReturnType = (DeclaredType) type;

		final List<DeclaredType> allTypes = getAllImplementedTypes(declaredReturnType, new ArrayList<DeclaredType>());

		// public methods
		// have to loop to get super methods too
		final String methodSuffix = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		final String getMethodName = "get" + methodSuffix, isMethodName = "is" + methodSuffix;
		for (final DeclaredType clazz : allTypes) {
			for (Element e : ((TypeElement) clazz.asElement()).getEnclosedElements()) {
				if (e.getKind() != ElementKind.METHOD)
					continue;
				final ExecutableElement m = (ExecutableElement) e;
				//System.out.printf("method: '%s', isSetterMethod: '%s'\n", m, isSetterMethod(m));
				final String ret = matchingGetterMethod(m, getMethodName, isMethodName);
				if(ret != null) {
					return ret;
				}
			}
		}

		// fix for 8813: include inherited and non-public fields
		for (final DeclaredType clazz : allTypes) {
			//System.out.println("fields in class: "+Arrays.toString(classFields));
			for (Element e : ((TypeElement)clazz.asElement()).getEnclosedElements()) {
				if(e.getKind() != ElementKind.FIELD)
					continue;
				final VariableElement f = (VariableElement)e;
				final Set<Modifier> modifiers = f.getModifiers();
				// we want the name to match exactly
				if(!fieldName.equals(f.getSimpleName().toString())) continue;
				// cannot be static
				if (modifiers.contains(Modifier.STATIC)) return null;
				// must be public todo: what about package-private?
				if (modifiers.contains(Modifier.PUBLIC)) {
					this.type = f.asType();
					return "." + fieldName;
				}
				if(!allowReflection) {
					JdbcMapperProcessor.messager.printMessage(Diagnostic.Kind.ERROR, "cannot public setter, but did find non-public field on parameter: '" + delegate.getSimpleName() + "' with this name: '" + fieldName + "', but reflection is not allowed", delegate);
					return "";
				}
				// otherwise support terrible reflection
				final TypeMirror oldType = this.type;
				this.type = f.asType();
				final String obj = sb.toString();
				sb.setLength(0);
				return "com.moparisthebest.jdbc.util.ReflectionUtil.getValue(" + oldType.toString() + ".class, \"" + fieldName + "\", " +
						this.type.toString() + ".class, (" + obj + "))";
			}
		}


		JdbcMapperProcessor.messager.printMessage(Diagnostic.Kind.ERROR, "cannot find field or public setter on parameter: '" + delegate.getSimpleName() + "' with this name: '" + fieldName + "'", delegate);
		return "";
	}

	/**
	 * Determine if the given method is a java bean setter method.
	 * @param method Method to check
	 * @param getMethodName
	 * @param isMethodName
	 * @return True if the method is a setter method.
	 */
	public String matchingGetterMethod(final ExecutableElement method, final String getMethodName, final String isMethodName) {
		final String methodName = method.getSimpleName().toString();
		final boolean isMethod = isMethodName.equals(methodName);
		if (isMethod || getMethodName.equals(methodName)) {

			final Set<Modifier> modifiers = method.getModifiers();
			// cannot be static
			if (modifiers.contains(Modifier.STATIC)) return null;
			// must be public todo: what about package-private?
			if (!modifiers.contains(Modifier.PUBLIC)) return null;
			
			// must take no parameters
			if (method.getParameters().size() != 0) return null;

			final TypeMirror ret = method.getReturnType();

			// must return boolean/Boolean to qualify for is*
			if(isMethod && !(ret.getKind() == TypeKind.BOOLEAN || types.isAssignable(ret, booleanType))) return null;

			this.type = ret;
			return "." + methodName + "()";
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComponentTypeString() {
		return componentTypeString;
	}

	public void setComponentTypeString(String componentTypeString) {
		this.componentTypeString = componentTypeString;
	}

	@Override
	public TypeMirror asType() {
		return type;
	}

	@Override
	public String toString() {
		return "SpecialVariableElement{" +
				"delegate=" + delegate +
				'}';
	}
}
