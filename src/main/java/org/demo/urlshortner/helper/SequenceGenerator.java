package org.demo.urlshortner.helper;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator {

    private static final AtomicLong counter = new AtomicLong(1);

    public static Long generatePrimaryKey() {
        return counter.getAndIncrement();
    }
}
