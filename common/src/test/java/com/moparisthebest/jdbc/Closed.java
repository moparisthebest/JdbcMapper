package com.moparisthebest.jdbc;

import java.io.Closeable;

interface Closed extends Closeable {

    boolean isClosed();

    String name();
}
