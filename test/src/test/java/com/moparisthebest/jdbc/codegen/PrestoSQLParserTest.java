package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mopar on 5/30/17.
 */
public class PrestoSQLParserTest extends SimpleSQLParserTest {

    private final SQLParser factory = new PrestoSQLParser();

    public SQLParser getFactory() {
        return factory;
    }

}
