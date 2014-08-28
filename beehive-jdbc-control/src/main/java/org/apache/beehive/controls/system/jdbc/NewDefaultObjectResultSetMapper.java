package org.apache.beehive.controls.system.jdbc;

import org.apache.beehive.controls.api.context.ControlBeanContext;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.*;

/**
 * Refer to org.apache.beehive.controls.system.jdbc.ResultSetMapper for how this class operates
 */
public class NewDefaultObjectResultSetMapper extends com.moparisthebest.jdbc.CaseInsensitiveMapResultSetMapper implements org.apache.beehive.controls.system.jdbc.ResultSetMapper {
	public NewDefaultObjectResultSetMapper() {
		super(1024);
	}

	/**
	 * Map the ResultSet to the method's return type. The object type returned is defined by the return type of the method.
	 *
	 * @param context A ControlBeanContext instance, see Beehive controls javadoc for additional information
	 * @param m       Method assoicated with this call.
	 * @param rs      Result set to map.
	 * @param cal     A Calendar instance for time/date value resolution.
	 * @return The Object resulting from the ResultSet
	 */
	@SuppressWarnings({"unchecked"})
	public Object mapToResultType(ControlBeanContext context, Method m, ResultSet rs, Calendar cal) {
		final Class returnType = m.getReturnType();
		if (returnType.isArray()) {
			return toArray(rs, returnType.getComponentType(), context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal);
		} else if (Collection.class.isAssignableFrom(returnType)) {
			return toCollection(rs, returnType, (Class) getActualTypeArguments(m)[0], context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal);
		} else if (Map.class.isAssignableFrom(returnType)) {
			Type[] types = getActualTypeArguments(m);
			if (types[1] instanceof ParameterizedType) { // for collectionMaps
				ParameterizedType pt = (ParameterizedType) types[1];
				Class collectionType = (Class) pt.getRawType();
				if (Collection.class.isAssignableFrom(collectionType))
					return toMapCollection(rs, returnType, (Class) types[0], collectionType, (Class) pt.getActualTypeArguments()[0], context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal);
			}
			return toMap(rs, returnType, (Class) types[0], (Class) types[1], context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal);
		} else if (Iterator.class.isAssignableFrom(returnType)) {
			return ListIterator.class.isAssignableFrom(returnType) ?
					toListIterator(rs, (Class) getActualTypeArguments(m)[0], context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal) :
					toIterator(rs, (Class) getActualTypeArguments(m)[0], context.getMethodPropertySet(m, JdbcControl.SQL.class).arrayMaxLength(), cal);
		} else {
			return toObject(rs, returnType, cal);
		}
	}

	private static Type[] getActualTypeArguments(Method m) {
		return ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
	}

	public boolean canCloseResultSet() {
		return true;
	}
}
