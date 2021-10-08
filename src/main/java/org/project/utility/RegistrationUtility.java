package org.project.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RegistrationUtility {

    @NotNull
    @Contract(pure = true)
    public static Boolean checkName(@NotNull String name) {
        return name.length() > 0;
    }

    @NotNull
    public static Boolean checkPassword(@NotNull String password) {
        return password.matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                        "(?=.*[@$!%*?&.:;,-])[A-Za-z\\d@$!%*?&.:;,-]{6,}$");
    }

    @NotNull
    public static Boolean checkPasswordConfirmed(@NotNull String password, String confirmedPassword) {
        return password.equals(confirmedPassword) && confirmedPassword.length() > 0;
    }

    @NotNull
    @Contract(pure = true)
    public static Boolean checkAddress(@NotNull String address) {
        return address.matches("(Via|via|Corso|corso|Viale|viale|Piazza|piazza) [a-zA-Z ]+?,? \\d+, \\w+ (\\w+)");
    }

    @NotNull
    @Contract(pure = true)
    public static Boolean checkCode(@NotNull String code) {
        return code.matches("^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1} " +
                "[0-9lmnpqrstuvLMNPQRSTUV] {2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})$|([0-9]{11})$");
    }

    @NotNull
    @Contract(pure = true)
    public static Boolean checkEmail(@NotNull String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }

    //Controllare su DB
    /*public static Boolean checkNickname(String nickname){
        return nickname.length()>0 && nickname.ESISTEGIADB;
    }*/
}
