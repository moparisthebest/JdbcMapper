package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class SpringBeanDAOTest {

    @Test
    public void testSpringBeanDAOBeanImportsRepository() {
        try {
            String genClass = new String(Files.readAllBytes(Paths.get("target/generated-sources/annotations/com/moparisthebest/jdbc/codegen/SpringBeanDAOBean.java")), StandardCharsets.UTF_8);
            assertTrue(genClass.contains("import org.springframework.stereotype.Repository;"));
        } catch (IOException e) {
            assertFalse("Failed to read file at target/generated-sources/annotations/com/moparisthebest/jdbc/codegen/SpringBeanDAOBean.java", true);
        }
    }

    @Test
    public void testSpringBeanDAOHasRepositoryStereotype() {
        try {
            Class clazz = ClassLoader.getSystemClassLoader()
                    .loadClass("com.moparisthebest.jdbc.codegen.SpringBeanDAOBean");
            assertTrue(clazz.isAnnotationPresent(org.springframework.stereotype.Repository.class));
        } catch (ClassNotFoundException e) {
            assertFalse("Failed to find class com.moparisthebest.jdbc.codegen.SpringBeanDAOBean", true);
        }
    }

    @Test
    public void testSpringBeanDAOBeanImplementsSpringBeanDAO() {
        try {
            Class[] clazzInterfaces = ClassLoader.getSystemClassLoader()
                    .loadClass("com.moparisthebest.jdbc.codegen.SpringBeanDAOBean").getInterfaces();
            assertTrue(Arrays.asList(clazzInterfaces).contains(SpringBeanDAO.class));
        } catch (ClassNotFoundException e) {
            assertFalse("Failed to find class com.moparisthebest.jdbc.codegen.SpringBeanDAOBean", true);
        }
    }


    @Test
    public void testAbstractSpringBeanDAOBeanExtendsAbstractSpringBeanDAO() {
        try {
            Class clazz = ClassLoader.getSystemClassLoader()
                    .loadClass("com.moparisthebest.jdbc.codegen.AbstractSpringBeanDAOBean");
            assertTrue(clazz.getSuperclass().equals(AbstractSpringBeanDAO.class));
        } catch (ClassNotFoundException e) {
            assertFalse("Failed to find class com.moparisthebest.jdbc.codegen.AbstractSpringBeanDAOBean", true);
        }
    }
}
