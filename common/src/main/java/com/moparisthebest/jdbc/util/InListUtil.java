package com.moparisthebest.jdbc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InListUtil {

    private static <T> List<List<T>> split(final List<T> list, final int maxLength) {
        final int listSize = list.size();
        final List<List<T>> ret = new ArrayList<List<T>>();
        if (listSize < maxLength)
            ret.add(list);
        else
            for (int fromIndex = 0, toIndex = maxLength; fromIndex < listSize; fromIndex = toIndex, toIndex += maxLength)
                ret.add(list.subList(fromIndex, toIndex > listSize ? listSize : toIndex));
        return ret;
    }

    private static <T> StringBuilder buildCommaSeparatedList(final Iterable<T> list, final StringBuilder sb) {
        boolean notFirst = false;
        for (final T obj : list) {
            if (notFirst) sb.append(',');
            else notFirst = true;
            sb.append('?');
        }
        return sb;
    }

    private static <T> StringBuilder buildInList(final Iterable<T> list, final StringBuilder sb, final String fieldName, final String listPrefix) {
        return buildCommaSeparatedList(list, sb.append(fieldName).append(listPrefix)).append(")");
    }

    private static <T> String toInList(final String fieldName, final Collection<T> items, final int maxSize, final String listPrefix, final String listCombine) {
        final StringBuilder sb = new StringBuilder("(");

        // do it quick if it will fit in one in-list
        if (items.size() < maxSize) // 999 or less
            return buildInList(items, sb, fieldName, listPrefix).append(")").toString();

        // else we need to split lists
        boolean notFirst = false;
        for (final List<T> item : split(items instanceof List ? (List<T>) items : new ArrayList<T>(items), maxSize)) {
            if (notFirst) sb.append(listCombine);
            else notFirst = true;
            buildInList(item, sb, fieldName, listPrefix);
        }

        return sb.append(")").toString();
    }

    public static <T> String toInList(final String fieldName, final Collection<T> items, final int maxSize) {
        return toInList(fieldName, items, maxSize, " IN (", " OR ");
    }

    public static <T> String toNotInList(final String fieldName, final Collection<T> items, final int maxSize) {
        return toInList(fieldName, items, maxSize, " NOT IN (", " AND ");
    }
}
