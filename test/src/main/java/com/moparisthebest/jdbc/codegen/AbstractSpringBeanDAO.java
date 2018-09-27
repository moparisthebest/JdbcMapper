package com.moparisthebest.jdbc.codegen;

@JdbcMapper.Mapper(jndiName = "bob",
        databaseType = JdbcMapper.DatabaseType.ANY,
        cachePreparedStatements = JdbcMapper.OptionalBool.FALSE,
        allowReflection = JdbcMapper.OptionalBool.TRUE,
        generateAsSpringBean = JdbcMapper.OptionalBool.TRUE)
public abstract class AbstractSpringBeanDAO implements JdbcMapper {
}
