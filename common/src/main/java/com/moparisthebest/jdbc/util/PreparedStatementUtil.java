package com.moparisthebest.jdbc.util;

import com.moparisthebest.jdbc.InList;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

//IFJAVA8_START
import java.time.*;

import static java.nio.charset.StandardCharsets.UTF_8;
//IFJAVA8_END

public class PreparedStatementUtil {

    public static final Object noBind = new Object();

    public static PreparedStatement bind(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
        recursiveBind(ps, bindObjects);
        return ps;
    }

    public static void setObject(final PreparedStatement ps, final int index, final Object o) throws SQLException {
        // we are going to put most common ones up top so it should execute faster normally
        if (o == null || o instanceof String || o instanceof Number)
            ps.setObject(index, o);
            // java.util.Date support, put it in a Timestamp
        else if (o instanceof java.util.Date)
            ps.setObject(index, o.getClass().equals(java.util.Date.class) ? new java.sql.Timestamp(((java.util.Date)o).getTime()) : o);
            //IFJAVA8_START// todo: other java.time types
        else if (o instanceof Instant)
            ps.setObject(index, java.sql.Timestamp.from((Instant)o));
        else if (o instanceof LocalDateTime)
            ps.setObject(index, java.sql.Timestamp.valueOf((LocalDateTime)o));
        else if (o instanceof LocalDate)
            ps.setObject(index, java.sql.Date.valueOf((LocalDate)o));
        else if (o instanceof LocalTime)
            ps.setObject(index, java.sql.Time.valueOf((LocalTime)o));
        else if (o instanceof ZonedDateTime)
            ps.setObject(index, java.sql.Timestamp.from(((ZonedDateTime)o).toInstant()));
        else if (o instanceof OffsetDateTime)
            ps.setObject(index, java.sql.Timestamp.from(((OffsetDateTime)o).toInstant()));
        else if (o instanceof OffsetTime)
            ps.setObject(index, java.sql.Time.valueOf(((OffsetTime)o).toLocalTime())); // todo: no timezone?

            //IFJAVA8_END
            // CLOB support
        else if (o instanceof Reader)
            ps.setClob(index, (Reader) o);
        else if (o instanceof ClobString)
            ps.setClob(index, ((ClobString) o).s == null ? null : new StringReader(((ClobString) o).s));
        else if (o instanceof java.sql.Clob)
            ps.setClob(index, (java.sql.Clob) o);
            // BLOB support
        else if (o instanceof byte[])
            ps.setBlob(index, new ByteArrayInputStream((byte[]) o));
        else if (o instanceof InputStream)
            ps.setBlob(index, (InputStream) o);
        else if (o instanceof File)
            try {
                ps.setBlob(index, new FileInputStream((File) o)); // todo: does this close this or leak a file descriptor?
            } catch (FileNotFoundException e) {
                throw new SQLException("File to Blob FileNotFoundException", e);
            }
        else if (o instanceof BlobString)
            ps.setBlob(index, ((BlobString) o).s == null ? null : new ByteArrayInputStream(((BlobString) o).s.getBytes(((BlobString) o).charset)));
        else if (o instanceof java.sql.Blob)
            ps.setBlob(index, (java.sql.Blob) o);
        else if (o instanceof java.sql.Array)
            ps.setArray(index, (java.sql.Array) o);
        else if (o instanceof Enum)
            ps.setObject(index, ((Enum)o).name());
        else
            ps.setObject(index, o); // probably won't get here ever, but just in case...
		/*
		switch(ps.getParameterMetaData().getParameterType(index)){ // 'java.sql.SQLException: Unsupported feature', fully JDBC 3.0 compliant my ass, freaking oracle...
			case Types.CLOB:
				if(o instanceof String)
					ps.setObject(index, o);
				else if (o instanceof Reader)
					ps.setClob(index, (Reader) o);
				else if (o instanceof Clob)
					ps.setClob(index, (Clob) o);
				return;
			case Types.BLOB:
				if (o instanceof byte[])
					ps.setBlob(index, new ByteArrayInputStream((byte[])o));
				else if (o instanceof InputStream)
					ps.setBlob(index, (InputStream) o);
				else if (o instanceof File)
					try {
						ps.setBlob(index, new FileInputStream((File) o));
					} catch (FileNotFoundException e) {
						throw new SQLException("File to Blob FileNotFoundException", e);
					}
				else if (o instanceof Blob)
					ps.setBlob(index, (Blob) o);
				else if(o instanceof String)
					try{
						ps.setBlob(index, new ByteArrayInputStream(((String) o).getBytes("UTF-8")));
					}catch(UnsupportedEncodingException e){
						throw new SQLException("String to Blob UnsupportedEncodingException", e);
					}
				return;
			default:
				ps.setObject(index, o);
		}
		*/
    }

    public static int recursiveBind(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
        return recursiveBindIndex(ps, 0, bindObjects);
    }

    public static int recursiveBindIndex(final PreparedStatement ps, int index, final Object... bindObjects) throws SQLException {
        if (bindObjects != null && bindObjects.length > 0) {
            for (final Object o : bindObjects) {
                if (o != null) {
                    if (o == InList.InListObject.inEmpty() || o == InList.InListObject.notInEmpty() || o == noBind) {
                        continue; // ignore
                    } else if (o instanceof Bindable) {
                        index = recursiveBindIndex(ps, index, ((Bindable) o).getBindObject());
                        continue;
                    } else if (o instanceof Object[]) {
                        index = recursiveBindIndex(ps, index, (Object[]) o);
                        continue;
                    } else if (o instanceof Collection) {
                        // is creating 1 array and doing 1 method call faster than falling through to iterator and making multiple method calls/arrays? *probably* ?
                        index = recursiveBindIndex(ps, index, ((Collection) o).toArray());
                        continue;
                    } else if(o instanceof Iterable) {
                        for(final Object o2 : (Iterable) o) {
                            index = recursiveBindIndex(ps, index, o2);
                        }
                        continue;
                    }
                }
                //System.out.printf("index: '%d' bound to '%s'\n", index+1, o);
                setObject(ps, ++index, o);
                //ps.setObject(++index, o);
            }
        }
        return index;
    }

    public static Object wrapClob(String s) {
        return new ClobString(s);
    }

    public static Object wrapBlob(String s) {
        return new BlobString(s, UTF_8);
    }

    public static Object wrapBlob(final String s, final Charset charset) {
        return new BlobString(s, charset == null ? UTF_8 : charset);
    }

    private static class StringWrapper {
        public final String s;

        private StringWrapper(String s) {
            this.s = s;
        }

        public String toString() {
            return s;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StringWrapper)) return false;
            StringWrapper that = (StringWrapper) o;
            return !(s != null ? !s.equals(that.s) : that.s != null);
        }

        public int hashCode() {
            return s != null ? s.hashCode() : 0;
        }
    }

    private static class ClobString extends StringWrapper {
        private ClobString(String s) {
            super(s);
        }
    }

    private static class BlobString extends StringWrapper {
        private final Charset charset;
        private BlobString(final String s, final Charset charset) {
            super(s);
            this.charset = charset;
        }
    }

    /*IFJAVA6_START
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	IFJAVA6_END*/
}
