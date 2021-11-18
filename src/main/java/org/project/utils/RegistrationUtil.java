package org.project.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.project.server.ServerReference;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;

public class RegistrationUtil {

    private static final String RX_NAME = "^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$";

    private static final String RX_ADDRESS_CITY = "^[a-zA-Z]+( [a-zA-Z]+)*$";

    private static final String RX_NUMBER = "^\\d{1,4}[a-zA-Z]?$";

    private static final String RX_CAP = "^[0-9]{5}$";

    private static final String RX_FISCAL_CODE = "^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST][0-9lmnpqrstuvLMNPQRSTUV]{2}" +
            "[A-Za-z][0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]$|([0-9]{11}))$";

    private static final String RX_EMAIL = "^((?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
            "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))$";

    private static final String RX_PWD = "^((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.:;,-])[A-Za-z\\d@$!%*?&.:;,-]{6,})$";

    public static boolean checkLength(@NotNull String string) {
        return string.length() > 0;
    }

    public static boolean checkName(@NotNull String name) {
        return name.matches(RX_NAME);
    }

    public static boolean checkAddress(@NotNull String address) {
        return address.matches(RX_ADDRESS_CITY);
    }

    public static boolean checkNumberAddress(@NotNull String number) {
        return number.matches(RX_NUMBER);
    }

    public static boolean checkCityAddress(@NotNull String city) {
        return city.matches(RX_ADDRESS_CITY);
    }

    public static boolean checkCap(@NotNull String cap) {
        return cap.matches(RX_CAP);
    }

    public static boolean checkFiscalCode(@NotNull String fiscalCode) {
        return fiscalCode.matches(RX_FISCAL_CODE);
    }

    public static boolean checkEmail(@NotNull String email) {
        return email.matches(RX_EMAIL);
    }

    public static boolean checkPassword(@NotNull String password) {
        return password.matches(RX_PWD);
    }

    public static boolean checkChangePwd(@NotNull String newPwd, String oldPwd) {
        return !newPwd.equals(oldPwd);
    }

    public static boolean checkPasswordConfirmed(@NotNull String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }

    public static boolean checkDuplicateFiscalCode(@NotNull String fiscalCode) {
        try {
            return ServerReference.getServer().checkDuplicateFiscalCode(fiscalCode.toUpperCase(Locale.ROOT));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkDuplicateEmail(String email) {
        try {
            return ServerReference.getServer().checkDuplicateEmail(email);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkDuplicateNickname(String nickname) {
        try {
            return ServerReference.getServer().checkDuplicateNickname(nickname);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkDuplicateHubName(String name) {
        try {
            return ServerReference.getServer().checkDuplicateHubName(name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkDuplicateAddress(String address) {
        try {
            return ServerReference.getServer().checkDuplicateAddress(address);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
