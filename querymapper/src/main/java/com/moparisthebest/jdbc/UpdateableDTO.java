package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.moparisthebest.jdbc.TryClose.tryClose;

public abstract class UpdateableDTO implements Finishable {
	private boolean finished = false;
	private Map<String, Object> changes = null;

	private String tableName;
	private String whereClause;

	public static final String YES = System.getProperty("UpdateableDTO.YES", "Y");
	public static final String NO = System.getProperty("UpdateableDTO.NO", "N");

	/**
	 * Will always return YES or NO from this class, so they CAN be compared with object equality '==' instead of '.equals()'
	 * @param bool
	 * @return
	 */
	public static String booleanToString(boolean bool){
		return bool ? YES : NO;
	}
	
	public static boolean changed(Object oldValue, Object newValue){
		return oldValue != newValue	&& ((oldValue != null && !oldValue.equals(newValue)) || !newValue.equals(oldValue));
	}

	public static <K, E> Object[] getUpdate(final StringBuilder sb, final Map<K, E> changes, final String tableName, final String whereClause){
		if(changes.isEmpty())
			return new Object[0];
		sb.append("UPDATE ").append(tableName).append(" SET ");
		int count = -1;
		Object[] objectsToBind = new Object[changes.size()];
		boolean notFirst = false;
		for (Map.Entry<K, E> column : changes.entrySet()) {
			if (column.getKey() == null) continue;
			if (notFirst) sb.append(",");
			else notFirst = true;
			sb.append(column.getKey()).append("=?");
			objectsToBind[++count] = column.getValue();
		}
		if(whereClause != null)
			sb.append(" ").append(whereClause);
		//System.out.println("updateRow: "+sb);
		return objectsToBind;
	}

	public static <K, E> Object[] getInsert(final StringBuilder sb, final Map<K, E> changes, final String tableName){
		if(changes.isEmpty())
			return new Object[0];
		sb.append("INSERT INTO ").append(tableName).append(" (");
		int count = -1;
		Object[] objectsToBind = new Object[changes.size()];
		boolean notFirst = false;
		for (Map.Entry<K, E> column : changes.entrySet()) {
			if (column.getKey() == null) continue;
			if (notFirst) sb.append(",");
			else notFirst = true;
			sb.append(column.getKey());
			objectsToBind[++count] = column.getValue();
		}
		sb.append(") VALUES (?");
		for(int x = 1; x < objectsToBind.length; ++x)
			sb.append(",?");
		sb.append(")");
		//System.out.println("insertRow: "+sb);
		return objectsToBind;
	}

	public static <K, E> int updateRow(final Connection conn, final Map<K, E> changes, final String tableName, final String whereClause, final Object... additionalBindObjects) throws SQLException {
		StringBuilder sb = new StringBuilder();
		Object[] objectsToBind = getUpdate(sb, changes, tableName, whereClause);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			QueryMapper.bindStatement(ps, objectsToBind, additionalBindObjects == null ? new Object[0] : additionalBindObjects);
			return ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	public static <K, E> int updateRow(final QueryMapper qm, final Map<K, E> changes, final String tableName, final String whereClause, final Object... additionalBindObjects) throws SQLException {
		StringBuilder sb = new StringBuilder();
		Object[] objectsToBind = getUpdate(sb, changes, tableName, whereClause);
		return qm.executeUpdate(sb.toString(), objectsToBind, additionalBindObjects == null ? new Object[0] : additionalBindObjects);
	}

	public static <K, E> int insertRow(final Connection conn, final Map<K, E> changes, final String tableName) throws SQLException {
		StringBuilder sb = new StringBuilder();
		Object[] objectsToBind = getInsert(sb, changes, tableName);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			QueryMapper.bindStatement(ps, objectsToBind);
			return ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	public static <K, E> int insertRow(final QueryMapper qm, final Map<K, E> changes, final String tableName) throws SQLException {
		StringBuilder sb = new StringBuilder();
		Object[] objectsToBind = getInsert(sb, changes, tableName);
		return qm.executeUpdate(sb.toString(), objectsToBind);
	}

	public static <K, E> int deleteRow(final Connection conn, final String tableName, final String whereClause, final Object... additionalBindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(String.format("DELETE FROM %s %s", tableName, whereClause == null ? "" : whereClause));
			if(additionalBindObjects != null)
				QueryMapper.bindStatement(ps, additionalBindObjects);
			return ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	public static <K, E> int deleteRow(final QueryMapper qm, final String tableName, final String whereClause, final Object... additionalBindObjects) throws SQLException {
		return qm.executeUpdate(String.format("DELETE FROM %s %s", tableName, whereClause == null ? "" : whereClause), additionalBindObjects == null ? new Object[0] : additionalBindObjects);
	}

	protected UpdateableDTO(String tableName, String whereClause) {
		setTableName(tableName);
		this.whereClause = whereClause;
	}

	protected String getTableName() {
		return tableName;
	}

	protected void setTableName(String tableName) {
		if(tableName == null)
			throw new NullPointerException("tableName can never be null! Insert, update, or delete would never work!");
		this.tableName = tableName;
	}

	protected String getWhereClause() {
		return whereClause;
	}

	protected void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	protected final void change(String columnName, boolean oldValue, boolean newValue) {
		change(columnName, booleanToString(oldValue), booleanToString(newValue));
	}

	/**
	 * Right now this is NOT thread-safe, if this class is to be used concurrently, it needs synchronized
	 * 
	 * This never saves the ORIGINAL value, so if you change a certain column to something new, then change it back
	 * to the original, it is still counted as a change. The way to fix this would be another Map<String, Object> 
	 * of ORIGINAL values, but I'm not sure this is desired.
	 *
	 * @param columnName
	 * @param newValue
	 */
	protected final boolean change(String columnName, Object oldValue, Object newValue) {
		if(finished && changed(oldValue, newValue)){
			/*
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("before: "+this);
			System.out.printf("changing column '%s' from '%s' to '%s'\n", columnName, oldValue, newValue);
			new Exception("Stack trace").printStackTrace(System.out);
			System.out.println("----------------------------------------------");
			*/
			if (changes == null)
				changes = new HashMap<String, Object>();
			changes.put(columnName, newValue);
			return true;
		}
		return false;
	}

	public boolean hasChanged() {
		return changes != null && !changes.isEmpty(); // not null would probably suffice?
	}

	public int updateRow(Connection conn) throws SQLException {
		if (!hasChanged())
			return 0;
		return updateRow(conn, changes, this.tableName, this.whereClause, this.getAdditionalBindObjects());
	}

	public int updateRow(QueryMapper qm) throws SQLException {
		if (!hasChanged())
			return 0;
		return updateRow(qm, changes, this.tableName, this.whereClause, this.getAdditionalBindObjects());
	}

	public int insertRow(Connection conn) throws SQLException {
		if (!hasChanged())
			return 0;
		return insertRow(conn, changes, this.tableName);
	}

	public int insertRow(QueryMapper qm) throws SQLException {
		if (!hasChanged())
			return 0;
		return insertRow(qm, changes, this.tableName);
	}

	public int deleteRow(Connection conn) throws SQLException {
		return deleteRow(conn, this.tableName, this.whereClause, this.getAdditionalBindObjects());
	}

	public int deleteRow(QueryMapper qm) throws SQLException {
		return deleteRow(qm, this.tableName, this.whereClause, this.getAdditionalBindObjects());
	}

	@Override
	public void finish(ResultSet rs) throws SQLException {
		this.finished = true;
	}

	/**
	 * You will almost always want to return some objects here for your where clause
	 *
	 * if you need to bind a single null, return new Object[]{null}, otherwise nothing will be binded
	 * if you only need to bind a single non-null object, return that object here
	 * if you need to bind multiple objects, return an Object[] or a Collection
	 */
	public abstract Object getAdditionalBindObjects();

	@Override
	public String toString() {
		return "UpdateableDTO{" +
				"finished=" + finished +
				", changes=" + changes +
				'}';
	}
}
