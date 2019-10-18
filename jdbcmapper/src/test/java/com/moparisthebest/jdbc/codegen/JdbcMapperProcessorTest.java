package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import static com.moparisthebest.jdbc.codegen.JdbcMapperProcessor.baseTypeMirrorString;
import static org.junit.Assert.*;

public class JdbcMapperProcessorTest {

    @Test
    public void baseTypeMirrorStringTest() {
        // generics
        assertEquals("java.util.List", baseTypeMirrorString("java.util.List<java.util.Map<java.lang.String,java.lang.String>>"));
        assertEquals("java.util.Map", baseTypeMirrorString("java.util.Map<java.lang.String,java.lang.String>"));
        assertEquals("java.util.Map", baseTypeMirrorString("java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>>"));
        // how java 8 formats TYPE_USE annotations
        assertEquals("java.lang.String", baseTypeMirrorString("(@com.moparisthebest.jdbc.TypeUseAnnotation :: java.lang.String)"));
        assertEquals("java.util.Date", baseTypeMirrorString("(@com.moparisthebest.jdbc.TypeUseAnnotation :: java.util.Date)"));
        // how java 13 formats TYPE_USE annotations
        assertEquals("java.lang.String", baseTypeMirrorString("@com.moparisthebest.jdbc.TypeUseAnnotation java.lang.String"));
        assertEquals("java.util.Date", baseTypeMirrorString("@com.moparisthebest.jdbc.TypeUseAnnotation java.util.Date"));
    }
}