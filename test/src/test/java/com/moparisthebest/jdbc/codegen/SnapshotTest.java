package com.moparisthebest.jdbc.codegen;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import static com.moparisthebest.jdbc.TryClose.tryClose;
import static org.junit.Assert.assertEquals;

public class SnapshotTest {

    // we want to only run this when explicitly asked to by runSnapshotTests.sh, which carefully exercises all options
    private final static boolean skipSnapshotTest = !"SnapshotTest".equals(System.getProperty("test"));
    private final static boolean updateSnapshots = Boolean.parseBoolean(System.getProperty("updateSnapshots", "false"));

    //IFJAVA8_START
    private static final String snapshotPrefix = "src/test/snapshot/";
    //IFJAVA8_END

	/*IFJAVA6_START
	private static final String snapshotPrefix = "src/test/snapshot/jdk6/";
	IFJAVA6_END*/

    @Test
    public void testAbstractDao() throws IOException {
        testFile(AbstractDao.class);
    }

    @Test
    public void testCleaningPersonDao() throws IOException {
        testFile(CleaningPersonDao.class);
    }

    @Test
    public void testPersonDAO() throws IOException {
        testFile(PersonDAO.class);
    }

    @Test
    public void testQmDao() throws IOException {
        testFile(QmDao.class);
    }

    static void testFile(final Class<?> testClass) throws IOException {
        testFile(testClass.getName());
    }

    static void testFile(final String className) throws IOException {
        if(skipSnapshotTest)
            return; // skip
        final String javaName = className.replace('.', '/') + JdbcMapper.beanSuffix + ".java";
        final String snapshotName = snapshotPrefix + javaName;
        final String generatedName = "target/generated-sources/annotations/" + javaName;
        if (updateSnapshots) {
            final byte[] generatedBytes = readAllBytes(generatedName);
            writeFile(snapshotName, generatedBytes);
        } else {
            final String snapshotContents = readUtf8File(snapshotName);
            final String generatedContents = readUtf8File(generatedName);
            assertEquals(snapshotContents, generatedContents);
        }
    }

    static void writeFile(final String fileName, final byte[] bytes) throws IOException {
        RandomAccessFile f = null;
        try {
            final File fileToWrite = new File(fileName);
            fileToWrite.getParentFile().mkdirs();
            f = new RandomAccessFile(fileToWrite, "rw");
            f.write(bytes);
            f.setLength(bytes.length);
        } finally {
            tryClose(f);
        }
    }

    static byte[] readAllBytes(final String fileName) throws IOException {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(fileName, "r");
            final byte[] ret = new byte[(int) f.length()];
            f.readFully(ret);
            return ret;
        } finally {
            tryClose(f);
        }
    }

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    static String readUtf8File(final String fileName) throws IOException {
        return new String(readAllBytes(fileName), UTF_8);
    }
}
