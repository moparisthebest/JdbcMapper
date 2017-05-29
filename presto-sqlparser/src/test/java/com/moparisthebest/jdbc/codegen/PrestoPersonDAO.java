package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.FieldPerson;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by mopar on 5/24/17.
 */
@JdbcMapper.Mapper(
//		jndiName = "bob",
        cachePreparedStatements = false
		, sqlParser = PrestoSQLParser.class
)
public interface PrestoPersonDAO extends PersonDAO {

    @JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE last_name = {lastName}")
    int setFirstName(String firstName, String lastName);

    @JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
    void setFirstName(String firstName, long personNo) throws SQLException;

    @JdbcMapper.SQL("UPDATE person SET first_name = {firstName} WHERE person_no = {personNo}")
    void setFirstNameBlob(byte[] firstName, long personNo) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo}")
    String getFirstName(long personNo) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE person_no = {personNo}")
    FieldPerson getPerson(long personNo) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
    List<FieldPerson> getPeople(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
    FieldPerson[] getPeopleArray(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
    Iterator<FieldPerson> getPeopleIterator(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name FROM person WHERE last_name = {lastName}")
    ListIterator<FieldPerson> getPeopleListIterator(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name, person_no FROM person WHERE last_name = {lastName}")
    Map<String, FieldPerson> getPersonMap(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name, last_name, person_no FROM person WHERE last_name = {lastName}")
    Map<String, List<FieldPerson>> getPersonMapList(String lastName) throws SQLException;

    @JdbcMapper.SQL("SELECT first_name FROM person WHERE person_no = {personNo} and last_name = {lastName}")
    String getFirstName(long personNo, String lastName) throws SQLException;
}
