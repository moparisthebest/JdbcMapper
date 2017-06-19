package com.moparisthebest.jdbc.codegen;

import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mopar on 6/19/17.
 */
public class ReflectionFields implements Iterable<VariableElement> {
	private final List<VariableElement> fields = new ArrayList<VariableElement>();

	public int addGetIndex(final VariableElement ve) {
		final int ret = fields.indexOf(ve);
		if(ret != -1)
			return ret;
		fields.add(ve);
		return fields.size() - 1;
	}

	public boolean isEmpty() {
		return fields.isEmpty();
	}

	@Override
	public Iterator<VariableElement> iterator() {
		return fields.iterator();
	}
}
