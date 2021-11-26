package org.project.utils;

import org.jetbrains.annotations.NotNull;

public class UIdGenerator {

    public static short generateUId(@NotNull String fiscalCode) {
        short UId = 0;
        for (int i = 0; i < fiscalCode.length(); i++) {
            UId += fiscalCode.charAt(i);
        }
        return UId;
    }
}
