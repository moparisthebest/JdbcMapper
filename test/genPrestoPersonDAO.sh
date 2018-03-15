#!/bin/bash
cd "$(dirname "$0")"

sed -e 's/PersonDAO extends JdbcMapper/PrestoPersonDAO extends PersonDAO/' -e 's@//		, sqlParser = SimpleSQLParser.class@		, sqlParser = PrestoSQLParser.class@' ./src/main/java/com/moparisthebest/jdbc/codegen/PersonDAO.java > ./src/main/java/com/moparisthebest/jdbc/codegen/PrestoPersonDAO.java

