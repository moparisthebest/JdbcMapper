package com.moparisthebest.jdbc.util;

import static com.moparisthebest.jdbc.util.PreparedStatementUtil.noBind;

public interface Bindable {

    Bindable empty = new Bindable() {
        @Override
        public Object getBindObject() {
            return noBind;
        }

        @Override
        public String toString() {
            return "";
        }
    };

    /**
     * This returns raw SQL to be included in a query, can contain bind params as standard ?
     * @return
     */
    String toString();

    /**
     * This returns an object (or list of objects, or list of list etc) to bind to the SQL snippet returned by toString()
     * PreparedStatementUtil must know how to bind this
     * @return
     */
    Object getBindObject();
}
