package com.dorvak.das.utils;

import com.dorvak.das.exceptions.ApplicationException;

import java.util.Objects;

public class Checks {

    private Checks() {
    }

    public static void notNull(Object object, String name) {
        if (Objects.isNull(object)) {
            throw new ApplicationException(name + " cannot be null");
        }
    }
}
