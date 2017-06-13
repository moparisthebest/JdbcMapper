#!/bin/bash
cd "$(dirname "$0")"

sed -e 's/PersonDAO extends Closeable/PrestoPersonDAO extends PersonDAO/' -e 's@//		, sqlParser = SimpleSQLParser.class@		, sqlParser = PrestoSQLParser.class@' ../jdbcmapper/src/test/java/com/moparisthebest/jdbc/codegen/PersonDAO.java > src/test/java/com/moparisthebest/jdbc/codegen/PrestoPersonDAO.java

