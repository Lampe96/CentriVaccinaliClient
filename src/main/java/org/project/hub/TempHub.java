package org.project.hub;

public class TempHub {

    private static String hubName;

    static String getHubName() {
        return hubName;
    }

    public static void setHubName(String hubName) {
        TempHub.hubName = hubName;
    }
}
