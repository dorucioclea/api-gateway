package com.t1t.digipolis.util;

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
                    if (list.size() != 1) {
                        throw new IllegalStateException("Multiple results");
                    }
                    return list.get(0);
                }
        );
    }

}