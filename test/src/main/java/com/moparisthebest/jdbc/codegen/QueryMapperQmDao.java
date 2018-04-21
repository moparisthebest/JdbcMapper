package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.QueryMapper;
import com.moparisthebest.jdbc.ResultSetMapper;
import com.moparisthebest.jdbc.dto.*;

import java.sql.Connection;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.TryClose.tryClose;

public class QueryMapperQmDao implements QmDao {

	public static final String personRegular = "SELECT * FROM person WHERE person_no = ?";
	public static final String bossRegularAndUnderscore = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossRegularAndUnderscoreReverse = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossRegular = "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";
	public static final String bossUnderscore = "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department " +
			"FROM person p " +
			"JOIN boss b ON p.person_no = b.person_no " +
			"WHERE p.person_no = ?";

	private final QueryMapper qm;

	public QueryMapperQmDao(final Connection conn, final ResultSetMapper rsm) {
		this.qm = new QueryMapper(conn, rsm);
	}

	@Override
	public Connection getConnection() {
		return qm.getConnection();
	}

	@Override
	public void close() {
		tryClose(qm);
	}

	@Override
	public FieldPerson getFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, FieldPerson.class, personNo);
	}

	@Override
	public BuilderPerson getBuilderPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, BuilderPerson.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, FieldBoss.class, personNo);
	}

	@Override
	public FieldBoss getFieldUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, FieldBoss.class, personNo);
	}

	@Override
	public SetPerson getSetRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, SetPerson.class, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, SetBoss.class, personNo);
	}

	@Override
	public SetBoss getSetUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, SetBoss.class, personNo);
	}

	@Override
	public ReverseFieldPerson getReverseFieldRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, ReverseFieldPerson.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseFieldBoss getReverseFieldUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, ReverseFieldBoss.class, personNo);
	}

	@Override
	public ReverseSetPerson getReverseSetRegularPerson(final long personNo) throws SQLException {
		return qm.toObject(personRegular, ReverseSetPerson.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscore, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegularAndUnderscoreReverse(final long personNo) throws SQLException {
		return qm.toObject(bossRegularAndUnderscoreReverse, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetRegular(final long personNo) throws SQLException {
		return qm.toObject(bossRegular, ReverseSetBoss.class, personNo);
	}

	@Override
	public ReverseSetBoss getReverseSetUnderscore(final long personNo) throws SQLException {
		return qm.toObject(bossUnderscore, ReverseSetBoss.class, personNo);
	}
}
