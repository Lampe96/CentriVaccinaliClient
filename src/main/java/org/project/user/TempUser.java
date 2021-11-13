package org.project.user;

public class TempUser {

    private static String email;
    private static String nickname;
    private static boolean emailVerified;

    static String getEmail() {
        return email;
    }

    static void setEmail(String email) {
        TempUser.email = email;
    }

    static String getNickname() {
        return nickname;
    }

    static void setNickname(String nickname) {
        TempUser.nickname = nickname;
    }

    static boolean getEmailIsVerified() {
        return emailVerified;
    }

    static void setEmailIsVerified(boolean emailVerified) {
        TempUser.emailVerified = emailVerified;
    }
}
