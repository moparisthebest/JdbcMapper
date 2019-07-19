package com.moparisthebest.jdbc;

class SimpleCloseable implements Closed {

    final String name;

    private boolean closed = false;

    SimpleCloseable(String name) {
        this.name = name;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void close() {
        closed = true;
    }
}
