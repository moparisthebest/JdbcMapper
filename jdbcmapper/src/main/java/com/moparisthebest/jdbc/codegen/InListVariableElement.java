package com.moparisthebest.jdbc.codegen;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Created by mopar on 6/1/17.
 */
class InListVariableElement implements VariableElement {

	final VariableElement delegate;

	InListVariableElement(final VariableElement delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object getConstantValue() {
		return delegate.getConstantValue();
	}

	@Override
	public TypeMirror asType() {
		return delegate.asType();
	}

	@Override
	public ElementKind getKind() {
		return delegate.getKind();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		return delegate.getAnnotationMirrors();
	}

	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
		return delegate.getAnnotation(annotationType);
	}

	@Override
	public Set<Modifier> getModifiers() {
		return delegate.getModifiers();
	}

	@Override
	public Name getSimpleName() {
		return delegate.getSimpleName();
	}

	@Override
	public Element getEnclosingElement() {
		return delegate.getEnclosingElement();
	}

	@Override
	public List<? extends Element> getEnclosedElements() {
		return delegate.getEnclosedElements();
	}

	@Override
	public boolean equals(final Object obj) {
		return delegate.equals(obj);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
		return delegate.accept(v, p);
	}

	//IFJAVA8_START

	@Override
	public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
		return delegate.getAnnotationsByType(annotationType);
	}

	//IFJAVA8_END
}
