package org.project.utils;

import org.jetbrains.annotations.NotNull;

public class UIdGenerator {

    public static short generateUId(@NotNull String fiscalCode) {
        short uid = 0;
        for (int i = 0; i < fiscalCode.length(); i++) {
            uid += fiscalCode.charAt(i);
        }
        return uid;
    }
}
