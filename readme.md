JdbcMapper is an ORM (Object Relational Mapper) that enables you to write normal SQL queries and use them to select 
POJOs (Plain Old Java Objects) from the database in different types of collections quickly and easily.  Type safety is
enforced throughout so neither casting nor ignoring warnings is required.

There are 2 different approaches to accomplish this.  JdbcMapper generates code at compile time, QueryMapper does
everything at runtime.  Currently there are different packages for java6 and java8+, these are built from the same
source with a bit of sed-like magic, when the documentation refers to classes only available in java8+ just know these
are obviously unavailable if you use the java6 version.

Why
---

The [java.sql](https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html) API is horrible, [ResultSet.wasNull()](https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#wasNull--) ?, enough said.  Hibernate is black magic that generates some truly
awful SQL queries.  Everything in between insists on writing your queries and/or forcing you to fully annotate all your
POJOs with information on how to map them from SQL, making them some combination of too verbose, too slow, or too much
unknown magic.

Goals
-----

1. Write as little code as possible
2. Have it run as fast as possible
3. Have it check and error out on everything it possibly can at compile time
4. Be runnable and testable inside or outside of containers easily
5. No surprises, as little magic as possible

JdbcMapper
----------

Write an interface or abstract class with methods that make sense for accessing your database, annotate the methods with
SQL, and on compilation an annotation processor will generate the required java.sql API code to execute your query and
return what you wanted.  This code is guaranteed to be the fastest code possible because hand written code would look
the same, just more error prone and harder to maintain.  The annotation processor also checks that the SQL queries are
valid, have all the right bind parameters, and can bind the result columns to all the correct fields on the result object.
If anything is wrong it's a compile error pointing you to the exact problem.

Example:

```java
@JdbcMapper.Mapper(jndiName = "java:/comp/env/jdbc/testPool") // omit jndiName and you must send in a java.sql.Connection
public interface PersonDAO extends Closeable {  // Closeable is optional but must have a 'void close()' method to use cachePreparedStatements or jndiName

	@JdbcMapper.SQL("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)")
	void createTablePerson();

	@JdbcMapper.SQL("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES ({personNo}, {birthDate}, {firstName}, {lastName})")
	int insertPerson(long personNo, Date birthDate, String firstName, String lastName);

	@JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
	int setFirstName(String firstName, long personNo);  // returning int will return number of rows modified, can also return void

	@JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
	String getFirstName(long personNo) throws SQLException;  // can map directly to simple types

	@JdbcMapper.SQL("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = {personNo}")
	Person getPerson(long personNo) throws SQLException; // or multiple fields, set methods, or constructor parameters on a POJO

	@JdbcMapper.SQL("SELECT person_no, first_name, last_name, birth_date FROM person WHERE last_name = {lastName}")
	List<Person> getPeople(String lastName) throws SQLException; // all rows in any Collection<T> (like Set<T>, LinkedList<T> etc), T[], ResultSetIterable<T> or Stream<T> (java8+) works too
}

// code:
try(PersonDAO personDao = JdbcMapperFactory.create(PersonDAO.class)) {
    personDao.createTablePerson();
    System.out.println(personDao.insertPerson(0, null, "First", "Person")); // 1
    System.out.println(personDao.insertPerson(1, null, "First", "Person")); // 1
    System.out.println(personDao.setFirstName("Second", 1));                // 1

    System.out.println(personDao.getFirstName(0));                          // First
    System.out.println(personDao.getFirstName(1));                          // Second

    System.out.println(personDao.getPerson(0));                             // Person{personNo=0,birthDate=null,firstName=First,lastName=Person}
    System.out.println(personDao.getPerson(1));                             // Person{personNo=1,birthDate=null,firstName=Second,lastName=Person}

    System.out.println(personDao.getPeople("Person"));                      // [Person{personNo=0,birthDate=null,firstName=First,lastName=Person}, Person{personNo=1,birthDate=null,firstName=Second,lastName=Person}]
}
```

QueryMapper
-----------

Need to generate SQL dynamically or just execute some queries quickly and easily?  Mapping is done using reflection in
ResultSetMapper or code is dynamically generated, compiled, instantiated, and cached at runtime to do the mapping using 
CompilingResultSetMapper.

Example:

```java
// CompilingResultSetMapper is an alternative to ResultSetMapper, default is ResultSetMapper
try(QueryMapper qm = new QueryMapper("java:/comp/env/jdbc/testPool", new ResultSetMapper())) { // or send in java.sql.Connection
    // executeUpdate returns int
    qm.executeUpdate("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)");
    System.out.println(qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", 0, null, "First", "Person"));                    // 1
    System.out.println(qm.executeUpdate("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", 1, null, "First", "Person"));                    // 1
    System.out.println(qm.executeUpdate("UPDATE person SET first_name = ? WHERE person_no = ?", "Second", 1));                                                                    // 1

    // can map directly to simple types
    System.out.println(qm.toObject("SELECT first_name FROM person WHERE person_no = ?", String.class, 0));                                                                        // First
    System.out.println(qm.toObject("SELECT first_name FROM person WHERE person_no = ?", String.class, 1));                                                                        // Second

    // or multiple fields, set methods, or constructor parameters on a POJO
    System.out.println(qm.toObject("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?", String.class, 0));                                      // Person{personNo=0,birthDate=null,firstName=First,lastName=Person}
    System.out.println(qm.toObject("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?", String.class, 1));                                      // Person{personNo=1,birthDate=null,firstName=Second,lastName=Person}

    // instead of toCollection can use toList, toArray, toResultSetIterable, toStream (java8+)
    System.out.println(qm.toCollection("SELECT person_no, first_name, last_name, birth_date FROM person WHERE last_name = ?", new ArrayList<String>(), String.class, "Person"));  // [Person{personNo=0,birthDate=null,firstName=First,lastName=Person}, Person{personNo=1,birthDate=null,firstName=Second,lastName=Person}]
}
```

ResultSet (multiple rows) to Object/Collection Mapping
--------------------------------------

An entire ResultSet (query) can be returned in any number of useful data structures, for the purposes of this list,
E will represent a simple object as listed in [Column to Object Mapping](#column-to-object-mapping), and
T will represent a possibly more complex object as listed in [Row to Object Mapping](#row-to-object-mapping), unless
otherwise noted the ResultSet is closed before these methods return:

  1. `T`
      * this simply returns the first row as an object
      * to return E[] or Map<String, E> as a single row, annotate JdbcMapper method with @JdbcMapper.SingleRow for
        compile-time, or for runtime QueryMapper/ResultSetMapper call .toObject or .toSingleMap
  2. `T[]`
  3. `java.util.Collection<T>`
      * any class implementing java.util.Collection can be used, java.util.List is popular
  4. `java.util.Iterator<T>`
      * an Iterator from a Collection
  5. `java.util.ListIterator<T>`
      * a ListIterator from a List
  6. `java.util.Map<E, T>`
      * any class implementing java.util.Map can be used, java.util.HashMap is popular, java.util.LinkedHashMap to
        retain order
      * The first column in the ResultSet will be the Map's key
      * If there are only two columns, the second will be the Map's value
      * If there are more than two columns, the value will be mapped to an object with the entire ResultSet in it,
        including the key, just like returning a Single complex object or a Collection would do
  7. `java.util.Map<E, java.util.Collection<T>>`
      * for the map, any class implementing java.util.Map can be used, java.util.HashMap is popular,
        java.util.LinkedHashMap to retain order
      * for the collection, any class implementing java.util.Collection can be used, java.util.List is popular
      * All mapping behavior is the same as `java.util.Map<E, T>`, except the value is used to aggregate all values with
        the same key
        * Example: you want to look up all firstNames for a given lastName, return type is `Map<String, List<String>>`,
          query might be `SELECT last_name, first_name FROM person`, returned value might be something like
          `{Monroe=[Marilyn, James], Washington=[George]}`
        * Example: you want to look up all People with a given lastName, return type is `Map<String, List<Person>>`,
          query might be `SELECT last_name, first_name, person_no FROM person`, returned value might be something like
          `{Monroe=[Person{firstName=Marilyn,lastName=Monroe,personNo=1}, Person{firstName=James,lastName=Monroe,personNo=2}], Washington=[Person{firstName=George,lastName=Washington,personNo=3}]}`
  8. `java.sql.ResultSet`
      * WARNING: you MUST ensure this is closed in finally or try-with-resources
      * no mapping happens here of course
  9. `com.moparisthebest.jdbc.util.ResultSetIterable<T>`
      * WARNING: you MUST ensure this is closed in finally or try-with-resources
      * this holds the ResultSet and lazily maps one row as needed until none remain
      * The .iterator() implementation just returns `this`, meaning you can only loop over it once, if you need to loop
         multiple times get a `Collection<T>` or something
  10. `java.util.stream.Stream<T>`
      * WARNING: you MUST ensure this is closed in finally or try-with-resources
      * WARNING: see above again, it's not common to try-with-resources a Stream, but it is the ONLY SAFE WAY to use this
      * this holds the ResultSet and lazily maps one row as needed until none remain

Row to Object Mapping
---------------------

In cases of only one column being returned from the query (or two in the case of Map<K,V>), the [simple
Column to Object Mapping](#column-to-object-mapping) will take place.  If a more complex object is 
requested, column names or indices are used to decide how to construct/map the object.

A single row can be represented in one of these ways:

  1. A simple object, where a single column is mapped as described in [Column to Object Mapping](#column-to-object-mapping)
  2. A single map entry, where there are exactly 2 columns, return-type is a Map<K,V>, where K and V are both simple
      objects, each row is mapped to a single map entry and both columns mapped as described in [Column to Object Mapping](#column-to-object-mapping).
  3. Array, where each column is mapped by index, starting at 0, array type of course determines the type returned
  4. Map<String, E>, where each column is mapped by name as key, and column value as value, mapped according to type
      * consider using the supplied com.moparisthebest.jdbc.util.CaseInsensitiveHashMap where case is ignored for keys
  5. Custom class Object, which attempts many different ways to map all returned columns to the class, if one of these
     is not a perfect match, an exception is thrown at runtime with QueryMapper, and a compile-time error happens with
     JdbcMapper.  This is an ordered list of how rows are mapped to class objects:
     1. If the class has a public constructor that takes a single java.sql.ResultSet parameter and nothing else, each
        row is sent in to create a new object, nothing else is done.
     2. If the class has a public constructor that takes the same number of arguments as columns returned, and all names
        match (order does not matter, case-insensitive, underscores ignored), this constructor is used.  This method has
        some requirements though:
        * Java 8+ only
        * requires -parameters argument to javac for runtime with QueryMapper, or compiling against classes without 
        source with JdbcMapper
        * Beware Java 8 only Bug ID [JDK-8191074](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8191074),
        fixed in Java 9+ but will not be backported to 8
     3. Otherwise the class must have a public no-arg constructor which will be used to instantiate the class.
     4. All 'set' methods for the class are searched for matches to the column name, the most specific match is chosen
        * a 'set' method is public, returns void or the class type (builder pattern), begins with the string 'set', and
          takes 1 argument which supports [Column to Object Mapping](#column-to-object-mapping)
        * 'set' is removed from the method name and all column names are searched (case-insensitive) for an exact match,
           if no match is found, all column names stripped of underscore '_' are searched (case-insensitive).
     5. For columns that have no matching 'set' methods, fields are searched following the same algorithm
        * using the field name, all column names are searched (case-insensitive) for an exact match,
          if no match is found, all column names stripped of underscore '_' are searched (case-insensitive).
        * Note: QueryMapper at runtime uses reflection by default and can set 'private final' (or any combination)
          fields directly without problem, this incurs overhead with pure java code though, so JdbcMapper will refuse to
          do this unless explicitly allowed with @JdbcMapper.Mapper(allowReflection = JdbcMapper.OptionalBool.TRUE)
     6. Examples:
        * USERID would prefer method setUserId(long/String/etc), then fall back to field 'userId' if the method doesn't 
          exist
        * USER_ID would prefer method setUser_Id(long/String/etc), then setUserId(long/String/etc), then field 'user_id',
          then field 'userId'
     7. If any columns cannot be mapped to fields/methods, this throws a MapperException at runtime with QueryMapper,
        and is a compile-time error with JdbcMapper.

Column to Object Mapping
------------------------

All decisions as to which ResultSet method(s) to call are based on the Java type being mapped to, because we have no 
knowledge of any database schema.  These mappings rarely if ever need changed, they can be overridden with QueryMapper
but not currently at compile-time with JdbcMapper.

If you are thinking 'shut up and show me the code already' refer to [ResultSetUtil.java](https://github.com/moparisthebest/JdbcMapper/blob/master/common/src/main/java/com/moparisthebest/jdbc/util/ResultSetUtil.java)
which contains the implementations actually called.

For the purposes of this mapping, consider 'rs' an instance of ResultSet, and 'index' an int index of a ResultSet column.

### numeric primitives
if the SQL value is NULL, 0 is returned for these, and no exception is thrown
##### byte
```java
return rs.getByte(index);
```
##### short
```java
return rs.getShort(index);
```
##### int
```java
return rs.getInt(index);
```
##### long
```java
return rs.getLong(index);
```
##### float
```java
return rs.getFloat(index);
```
##### double
```java
return rs.getDouble(index);
```
### numeric wrapper objects
##### Byte/Short/Integer/Long/Float/Double
these wrapper types are retrieved using the same function returning their primitives above, except null is returned
if the SQL value is NULL instead of 0, this example is for Long, but the same applies for all of these types
```java
long ret = rs.getLong(index);
return rs.wasNull() ? null : ret;
```
##### java.math.BigDecimal
```java
return rs.getBigDecimal(index);
```
### boolean
in all cases of SQL NULL being returned, if primitive boolean is requested an SQLException is thrown, if Object Boolean
is requested then null is returned.

boolean has special handling due to many popular databases not actually having a boolean type (hi Oracle!),
forcing application level workarounds.

0/1 numeric types convert to boolean using the standard ResultSet API, but many systems use char/varchar of Y/N or T/F,
which we default to Y/N but can be set via system properties:

ResultSetUtil.TRUE=Y  
ResultSetUtil.FALSE=N

First the standard ResultSet API is attempted:
```java
return rs.getBoolean(index);
```
If this does not throw an SQLException, it is returned directly
If SQLException is thrown, then we try to compare as a String:
```java
String bool = rs.getString(index);
boolean ret = ResultSetUtil.TRUE.equals(bool);
if (!ret && !ResultSetUtil.FALSE.equals(bool))
    throw new SQLException(String.format("Implicit conversion of database string to boolean failed on column '%d'. Returned string needs to be '%s' or '%s' and was instead '%s'.", index, ResultSetUtil.TRUE, ResultSetUtil.FALSE, bool));
return ret;
```
The returned string MUST be either TRUE or FALSE (or null, for Object Boolean) or an exception will be thrown
### Misc Objects
For all of these, when SQL NULL is returned, it maps to null
##### String
```java
return rs.getString(index);
```
##### java.lang.Enum (any enum)
```java
String name = rs.getString(index);
return name == null ? null : YourEnumType.valueOf(name);
```
##### byte[]
```java
return rs.getBytes(index);
```
##### java.sql.Ref
```java
return rs.getRef(index);
```
##### java.sql.Blob
```java
return rs.getBlob(index);
```
##### java.sql.Clob
```java
return rs.getClob(index);
```
##### java.sql.Array
```java
return rs.getArray(index);
```
##### java.sql.Struct
```java
return rs.getObject(index);
```
##### *
If nothing else fits, we call getObject and cross our fingers with QueryMapper at runtime, this is a compile-time error
with JdbcMapper. todo: is this actually a compile-time error? it *should* be, check...
```java
return rs.getObject(index);
```
### Date/Time Objects
For all of these, when SQL NULL is returned, it maps to null.  All of the [ResultSet.getDate/Timestamp/etc](https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#getTimestamp-int-java.util.Calendar-)
functions optionally take a Calendar object which is used to construct a time value if the database doesn't store
timezone information.  I am not going to show the variants that take Calendar here.  For QueryMapper, methods are
overloaded to take the Calendar values, for JdbcMapper, if the abstract method takes a Calendar object that is not mapped
in the query, that is used.

In the Java 8 java.time code below that uses `ZoneId.systemDefault()`, where a Calendar object is sent in, 
`calendar.getTimeZone().toZoneId()` is used instead.
##### java.sql.Date
```java
return rs.getDate(index);
```
##### java.sql.Time
```java
return rs.getTime(index);
```
##### java.sql.Timestamp
```java
return rs.getTimestamp(index);
```
##### java.util.Date
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : new java.util.Date(ts.getTime());
```
##### java.util.Calendar
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
if (null == ts)
    return null;
Calendar c = Calendar.getInstance();
c.setTimeInMillis(ts.getTime());
return c;
```
##### java.time.Instant
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : ts.toInstant();
```
##### java.time.LocalDateTime
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : ts.toLocalDateTime();
```
##### java.time.LocalDate
```java
java.sql.Date ts =  rs.getDate(index);
return ts == null ? null : ts.toLocalDate();
```
##### java.time.LocalTime
```java
java.sql.Time ts = rs.getTime(index);
return ts == null ? null : ts.toLocalTime();
```
##### java.time.ZonedDateTime
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
```
##### java.time.OffsetDateTime
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : OffsetDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
```
##### java.time.OffsetTime
```java
java.sql.Timestamp ts = rs.getTimestamp(index);
return ts == null ? null : OffsetTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
```
##### java.time.Year
done this way instead of Year.of(int) because usually int->string database coercion is allowed and the other way is not
```java
String s = rs.getString(index);
return s == null ? null : Year.parse(s);
```
##### java.time.ZoneId
```java
String s = rs.getString(index);
return s == null ? null : ZoneId.of(s);
```
##### java.time.ZoneOffset
```java
String s = rs.getString(index);
return s == null ? null : ZoneOffset.of(s);
```

TODO
----

 * DOCUMENTATION!!!!!
 * sql other than select return boolean, int > 0 ?
 * @RunInTransaction void support
 * QueryMapper mapping errors should be clearer, especially if a .finish(ResultSet) throws an error
 * check QueryMapper/ResultSetMapper closing of ResultSets, it doesn't look guaranteed