package com.moparisthebest.jdbc.util;

import com.moparisthebest.jdbc.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

//IFJAVA8_START
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.InList.defaultInList;

public class SqlBuilder implements Appendable, CharSequence, Collection<Object>, Bindable {

    private final StringBuilder sb;
    private final Collection<Object> bindObjects;

    private final InList inList;
    private final Connection conn;

    private SqlBuilder(final Connection conn, final InList inList, final StringBuilder sb, final Collection<Object> bindObjects) {
        if(sb == null || bindObjects == null || inList == null || conn == null)
            throw new NullPointerException("all arguments must be non-null");
        this.sb = sb;
        this.bindObjects = bindObjects;
        this.inList = inList.instance(conn);
        this.conn = conn;
    }

    public static SqlBuilder of(final Connection conn, final InList inList, final StringBuilder sb, final Collection<Object> bindObjects) {
        return new SqlBuilder(conn, inList, sb, bindObjects);
    }

    public static SqlBuilder of(final Connection conn) {
        return new SqlBuilder(conn, defaultInList, new StringBuilder(), new ArrayList<Object>());
    }

    public static SqlBuilder of(final Connection conn, final Collection<Object> bindObjects) {
        return new SqlBuilder(conn, defaultInList, new StringBuilder(), bindObjects);
    }

    public static SqlBuilder of(final Connection conn, final StringBuilder sb) {
        return new SqlBuilder(conn, defaultInList, sb, new ArrayList<Object>());
    }

    public static SqlBuilder of(final Connection conn, final InList inList) {
        return new SqlBuilder(conn, inList, new StringBuilder(), new ArrayList<Object>());
    }

    // start custom SqlBuilder methods

    public SqlBuilder bind(final Object bindObject) {
        this.bindObjects.add(bindObject);
        return this;
    }

    public SqlBuilder bind(final Object... bindObjects) {
        return this.bind((Object) bindObjects);
    }

    public SqlBuilder appendBind(final String sql, final Object bindObject) {
        sb.append(sql);
        return bind(bindObject);
    }

    public SqlBuilder appendBind(final String sql, final Object... bindObjects) {
        return this.appendBind(sql, (Object) bindObjects);
    }

    public <T> SqlBuilder appendInList(final String columnName, final Collection<T> values) throws SQLException {
        final InList.InListObject inListObject = inList.inList(conn, columnName, values);
        return this.appendBind(inListObject.toString(), inListObject.getBindObject());
    }

    public <T> SqlBuilder appendNotInList(final String columnName, final Collection<T> values) throws SQLException {
        final InList.InListObject inListObject = inList.notInList(conn, columnName, values);
        return this.appendBind(inListObject.toString(), inListObject.getBindObject());
    }

    public SqlBuilder and() {
        return append(" AND ");
    }

    public SqlBuilder or() {
        return append(" OR ");
    }

    public StringBuilder getStringBuilder() {
        return sb;
    }

    @Override
    public Collection<Object> getBindObject() {
        return bindObjects;
    }

    public InList getInList() {
        return inList;
    }

    public Connection getConnection() {
        return conn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlBuilder)) return false;

        final SqlBuilder that = (SqlBuilder) o;

        return bindObjects.equals(that.bindObjects) && sb.toString().equals(that.sb.toString());
    }

    @Override
    public int hashCode() {
        int result = sb.toString().hashCode();
        result = 31 * result + bindObjects.hashCode();
        return result;
    }

    // end custom SqlBuilder methods
    // start StringBuilder delegates

    public SqlBuilder append(Object obj) {
        sb.append(obj);
        return this;
    }

    public SqlBuilder append(String str) {
        sb.append(str);
        return this;
    }

    public SqlBuilder append(StringBuffer sb) {
        this.sb.append(sb);
        return this;
    }

    @Override
    public SqlBuilder append(CharSequence s) {
        sb.append(s);
        return this;
    }

    @Override
    public SqlBuilder append(CharSequence s, int start, int end) {
        sb.append(s, start, end);
        return this;
    }

    public SqlBuilder append(char[] str) {
        sb.append(str);
        return this;
    }

    public SqlBuilder append(char[] str, int offset, int len) {
        sb.append(str, offset, len);
        return this;
    }

    public SqlBuilder append(boolean b) {
        sb.append(b);
        return this;
    }

    @Override
    public SqlBuilder append(char c) {
        sb.append(c);
        return this;
    }

    public SqlBuilder append(int i) {
        sb.append(i);
        return this;
    }

    public SqlBuilder append(long lng) {
        sb.append(lng);
        return this;
    }

    public SqlBuilder append(float f) {
        sb.append(f);
        return this;
    }

    public SqlBuilder append(double d) {
        sb.append(d);
        return this;
    }

    public SqlBuilder appendCodePoint(int codePoint) {
        sb.appendCodePoint(codePoint);
        return this;
    }

    public SqlBuilder delete(int start, int end) {
        sb.delete(start, end);
        return this;
    }

    public SqlBuilder deleteCharAt(int index) {
        sb.deleteCharAt(index);
        return this;
    }

    public SqlBuilder replace(int start, int end, String str) {
        sb.replace(start, end, str);
        return this;
    }

    public SqlBuilder insert(int index, char[] str, int offset, int len) {
        sb.insert(index, str, offset, len);
        return this;
    }

    public SqlBuilder insert(int offset, Object obj) {
        sb.insert(offset, obj);
        return this;
    }

    public SqlBuilder insert(int offset, String str) {
        sb.insert(offset, str);
        return this;
    }

    public SqlBuilder insert(int offset, char[] str) {
        sb.insert(offset, str);
        return this;
    }

    public SqlBuilder insert(int dstOffset, CharSequence s) {
        sb.insert(dstOffset, s);
        return this;
    }

    public SqlBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        sb.insert(dstOffset, s, start, end);
        return this;
    }

    public SqlBuilder insert(int offset, boolean b) {
        sb.insert(offset, b);
        return this;
    }

    public SqlBuilder insert(int offset, char c) {
        sb.insert(offset, c);
        return this;
    }

    public SqlBuilder insert(int offset, int i) {
        sb.insert(offset, i);
        return this;
    }

    public SqlBuilder insert(int offset, long l) {
        sb.insert(offset, l);
        return this;
    }

    public SqlBuilder insert(int offset, float f) {
        sb.insert(offset, f);
        return this;
    }

    public SqlBuilder insert(int offset, double d) {
        sb.insert(offset, d);
        return this;
    }

    public int indexOf(String str) {
        return sb.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return sb.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return sb.lastIndexOf(str, fromIndex);
    }

    public SqlBuilder reverse() {
        sb.reverse();
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    @Override
    public int length() {
        return sb.length();
    }

    public int capacity() {
        return sb.capacity();
    }

    public void ensureCapacity(int minimumCapacity) {
        sb.ensureCapacity(minimumCapacity);
    }

    public void trimToSize() {
        sb.trimToSize();
    }

    public void setLength(int newLength) {
        sb.setLength(newLength);
    }

    @Override
    public char charAt(int index) {
        return sb.charAt(index);
    }

    public int codePointAt(int index) {
        return sb.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return sb.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return sb.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return sb.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        sb.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public void setCharAt(int index, char ch) {
        sb.setCharAt(index, ch);
    }

    public String substring(int start) {
        return sb.substring(start);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return sb.subSequence(start, end);
    }

    public String substring(int start, int end) {
        return sb.substring(start, end);
    }

    //IFJAVA8_START

    @Override
    public IntStream chars() {
        return sb.chars();
    }

    @Override
    public IntStream codePoints() {
        return sb.codePoints();
    }

    //IFJAVA8_END

    // end StringBuilder delegates
    // start Collection delegates

    @Override
    public int size() {
        return bindObjects.size();
    }

    @Override
    public boolean isEmpty() {
        return bindObjects.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return bindObjects.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return bindObjects.iterator();
    }

    @Override
    public Object[] toArray() {
        return bindObjects.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return bindObjects.toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return bindObjects.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return bindObjects.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return bindObjects.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return bindObjects.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bindObjects.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bindObjects.retainAll(c);
    }

    @Override
    public void clear() {
        bindObjects.clear();
    }

    //IFJAVA8_START

    @Override
    public boolean removeIf(Predicate<? super Object> filter) {
        return bindObjects.removeIf(filter);
    }

    @Override
    public Spliterator<Object> spliterator() {
        return bindObjects.spliterator();
    }

    @Override
    public Stream<Object> stream() {
        return bindObjects.stream();
    }

    @Override
    public Stream<Object> parallelStream() {
        return bindObjects.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Object> action) {
        bindObjects.forEach(action);
    }

    //IFJAVA8_END

    // end Collection delegates
}
