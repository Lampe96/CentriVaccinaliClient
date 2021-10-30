package org.project.user;

public class TempUser {
    private static String email;

    public static void setEmail(String email) {
        TempUser.email = email;
    }

    public static String getEmail() {
        return email;
    }
}
