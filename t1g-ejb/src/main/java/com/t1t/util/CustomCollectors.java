package com.t1t.util;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class CustomCollectors {

    public static <T> Collector<T, ?, T> getSingleResult() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() == 1) {
                        return list.get(0);
                    }
                    if (list.size() == 0) {
                        return null;
                    }
                    throw new IllegalStateException("Multiple results");
                }
        );
    }

    public static <T> Collector<T, ?, T> getFirstResult() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() < 1) {
                        return null;
                    }
                    return list.get(0);
                }
        );
    }

}