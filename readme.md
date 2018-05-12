JdbcMapper is an ORM (Object Relational Mapper) that enables you to write normal SQL queries and use them to select 
POJOs (Plain Old Java Objects) from the database in different types of collections quickly and easily.  Type safety is
enforced throughout so neither casting nor ignoring warnings is required.

There are 2 different approaches to accomplish this.  JdbcMapper generates code at compile time, QueryMapper does
everything at runtime.

Why
---

The java.sql API is horrible, ResultSet.wasNull() ?, enough said.  Hibernate is black magic that generates some truly
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

Column to Object Mapping
------------------------

todo: explain how individual columns are mapped to simple objects

Row to Object Mapping
---------------------

todo: explain how rows are mapped to POJOs

JdbcMapper
----------

Write an interface or abstract class with methods that make sense for accessing your database, annotate the methods with
SQL, and on compilation an annotation processor will generate the required java.sql API code to execute your query and
return what you wanted.  This code is guaranteed to be the fastest code possible because hand written code would look
the same, just more error prone and harder to maintain.  The annotation processor also checks that the SQL queries are
valid, have all the right bind parameters, and can bind the result columns to all the correct fields on the result object.
If anything is wrong it's a compile error pointing you to the exact problem.

Example:

```
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

```
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

TODO
----

 * DOCUMENTATION!!!!!
 * sql other than select return boolean, int > 0 ?
 * @RunInTransaction void support
 * QueryMapper mapping errors should be clearer, especially if a .finish(ResultSet) throws an error