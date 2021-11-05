package org.project.user;

public class TempUser {

    private static String email;
    private static String nickname;
    private static boolean emailVerified;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        TempUser.email = email;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nickname) {
        TempUser.nickname = nickname;
    }

    public static boolean getEmailIsVerified() {
        return emailVerified;
    }

    public static void setEmailIsVerified(boolean emailVerified) {
        TempUser.emailVerified = emailVerified;
    }
}
