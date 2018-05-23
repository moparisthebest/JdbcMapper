package com.moparisthebest.jdbc.util;

import java.util.*;

public class InListUtil {

    public static final int defaultMaxSize = Integer.parseInt(System.getProperty("QueryMapper.BindInList.defaultMaxSize", "999"));
    public static final String inEmpty = "(0=1)", notInEmpty = "(1=1)";

    // fieldName + listPrefix + 3 parens + ((number expected which is less than maxSize * 2) - 1) assuming 20 items = 39 + 3 = 42
    public static final int defaultInListPreallocLength = Integer.parseInt(System.getProperty("QueryMapper.BindInList.defaultInListPreallocLength", "42"));

    private static <T> String toNotInListNonEmpty(final String fieldName, final Iterator<T> items, final int maxSize, final String listPrefix, final String listCombine) {
        final StringBuilder sb = new StringBuilder(fieldName.length() + listPrefix.length() + defaultInListPreallocLength);
        sb.append('(');

        // else we need to split lists
        int count;
        while(true) {

            sb.append(fieldName).append(listPrefix);
            count = 1;
            while(true) {
                items.next();
                sb.append('?');
                if (!items.hasNext() || ++count > maxSize)
                    break;
                sb.append(',');
            }
            sb.append(')');

            if(!items.hasNext())
                break;
            sb.append(listCombine);
        }

        return sb.append(')').toString();
    }

    private static <T> String toInListNonEmpty(final String fieldName, final Iterator<T> items, final int maxSize) {
        return toNotInListNonEmpty(fieldName, items, maxSize, " IN (", " OR ");
    }

    private static <T> String toNotInListNonEmpty(final String fieldName, final Iterator<T> items, final int maxSize) {
        return toNotInListNonEmpty(fieldName, items, maxSize, " NOT IN (", " AND ");
    }

    public static <T> String toInList(final String fieldName, final Iterator<T> items, final int maxSize) {
        return items == null || !items.hasNext() ? inEmpty : toInListNonEmpty(fieldName, items, maxSize);
    }

    public static <T> String toNotInList(final String fieldName, final Iterator<T> items, final int maxSize) {
        return items == null || !items.hasNext() ? notInEmpty : toNotInListNonEmpty(fieldName, items, maxSize);
    }

    public static <T> String toInList(final String fieldName, final Iterable<T> items, final int maxSize) {
        final Iterator<T> itemIt;
        return items == null || !(itemIt = items.iterator()).hasNext() ? inEmpty : toInListNonEmpty(fieldName, itemIt, maxSize);
    }

    public static <T> String toNotInList(final String fieldName, final Iterable<T> items, final int maxSize) {
        final Iterator<T> itemIt;
        return items == null || !(itemIt = items.iterator()).hasNext() ? notInEmpty : toNotInListNonEmpty(fieldName, itemIt, maxSize);
    }

    public static <T> String toInList(final String fieldName, final T[] items, final int maxSize) {
        // is Arrays.asList(items).iterator() the best iterator for Array ? why aren't they Iterable again...
        return items == null || items.length == 0 ? inEmpty : toInListNonEmpty(fieldName, Arrays.asList(items).iterator(), maxSize);
    }

    public static <T> String toNotInList(final String fieldName, final T[] items, final int maxSize) {
        return items == null || items.length == 0 ? notInEmpty : toNotInListNonEmpty(fieldName, Arrays.asList(items).iterator(), maxSize);
    }

    public static <T> String toInList(final String fieldName, final Iterator<T> items) {
        return toInList(fieldName, items, defaultMaxSize);
    }

    public static <T> String toNotInList(final String fieldName, final Iterator<T> items) {
        return toNotInList(fieldName, items, defaultMaxSize);
    }

    public static <T> String toInList(final String fieldName, final Iterable<T> items) {
        return toInList(fieldName, items, defaultMaxSize);
    }

    public static <T> String toNotInList(final String fieldName, final Iterable<T> items) {
        return toNotInList(fieldName, items, defaultMaxSize);
    }

    public static <T> String toInList(final String fieldName, final T[] items) {
        return toInList(fieldName, items, defaultMaxSize);
    }

    public static <T> String toNotInList(final String fieldName, final T[] items) {
        return toNotInList(fieldName, items, defaultMaxSize);
    }
}
