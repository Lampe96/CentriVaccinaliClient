package org.project.utils;

import org.jetbrains.annotations.NotNull;
import org.project.hub.HubSignUpController;
import org.project.server.ServerReference;
import org.project.user.UserSignUpController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;

/**
 * In questa classe vengono gestite tutte le regex e i metodi
 * di controllo utilizzati in fase di registrazione dalle schermate
 * controllate da {@link UserSignUpController} e {@link HubSignUpController}.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class RegistrationUtil {

    /**
     * Regex utilizzata per verificare il nome dei cittadini e dei centri vaccinali.
     * E' possibile inserire lettere minuscole, maiuscole, numeri, apostrofi, punti
     * e molteplici nomi.
     */
    private static final String RX_NAME = "^[a-zA-Z0-9.']+( [a-zA-Z0-9.']+)*$";

    /**
     * Regex utilizzata per verificare la citta', consentendo l'inserimento
     * sole lettere e spazi se queste prevedono piu' parole.
     */
    private static final String RX_ADDRESS_CITY = "^[a-zA-Z]+( [a-zA-Z]+)*$";

    /**
     * Regex utilizzata per verificare il numero nell'indirizzo, consentendo l'inserimento di
     * 4 numeri consecutivi a una lettera opzionale.
     */
    private static final String RX_NUMBER = "^\\d{1,4}[a-zA-Z]?$";

    /**
     * Regex utilizzata per verificare il CAP, consentendo l'inserimento di
     * soli numeri e con un massimo di 5.
     */
    private static final String RX_CAP = "^[0-9]{5}$";

    /**
     * Regex utilizzata per verificare il codice fiscale italiano rispettando tutti i vincoli.
     */
    private static final String RX_FISCAL_CODE = "^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST][0-9lmnpqrstuvLMNPQRSTUV]{2}" +
            "[A-Za-z][0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]$|([0-9]{11}))$";

    /**
     * Regex utilizzata per verificare il controllo della email rispettando lo standard RFC 5322.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc5322.txt">Documentazione standard RFC 5322</a>
     * @see <a href="https://emailregex.com/">Descrizione della regex</a>
     */
    private static final String RX_EMAIL = "^((?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
            "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))$";

    /**
     * Regex utilizzata per verificare la password. Per rispettare questa e' necessario inserire almeno 6 caratteri
     * tra cui 1 lettera minuscola, 1 lettera maiuscola, 1 carattere speciale e 1 numero.
     */
    private static final String RX_PWD = "^((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.:;,-])[A-Za-z\\d@$!%*?&.:;,-]{6,})$";

    /**
     * Verifica se il campo ha almeno un carattere.
     *
     * @param string stringa in ingresso
     * @return true se e' superiore a 0, false in caso contrario
     */
    public static boolean checkLength(@NotNull String string) {
        return string.length() > 0;
    }

    /**
     * Verifica se il nome del cittadino o del centro vaccinale inserito
     * rispetta i vincoli della regex.
     *
     * @param name nome del centro/cittadino
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkName(@NotNull String name) {
        return name.matches(RX_NAME);
    }

    /**
     * Verifica se la via inserita rispetta i vincoli della regex.
     *
     * @param address nome della via
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkAddress(@NotNull String address) {
        return address.matches(RX_ADDRESS_CITY);
    }

    /**
     * Verifica se il numero civico inserito rispetta i vincoli della regex.
     *
     * @param number numero civico
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkNumberAddress(@NotNull String number) {
        return number.matches(RX_NUMBER);
    }

    /**
     * Verifica se il comune inserito rispetta i vincoli della regex.
     *
     * @param city comune
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkCityAddress(@NotNull String city) {
        return city.matches(RX_ADDRESS_CITY);
    }

    /**
     * Verifica se il cap inserito rispetta i vincoli della regex.
     *
     * @param cap cap
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkCap(@NotNull String cap) {
        return cap.matches(RX_CAP);
    }

    /**
     * Verifica se il codice fiscale inserito rispetta i vincoli della regex.
     *
     * @param fiscalCode codice fiscale
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkFiscalCode(@NotNull String fiscalCode) {
        return fiscalCode.matches(RX_FISCAL_CODE);
    }

    /**
     * Verifica se l'email inserita rispetta i vincoli della regex.
     *
     * @param email email
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkEmail(@NotNull String email) {
        return email.matches(RX_EMAIL);
    }

    /**
     * Verifica se la password inserita rispetta i vincoli della regex.
     *
     * @param password password
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkPassword(@NotNull String password) {
        return password.matches(RX_PWD);
    }

    /**
     * Utilizzato in fase di modifica della password, per verificare
     * che la nuova sia differente dalla precedente.
     *
     * @param newPwd nuova password
     * @param oldPwd vecchia password
     * @return false se non coincide, true in caso contrario
     */
    public static boolean checkChangePwd(@NotNull String newPwd, String oldPwd) {
        return !newPwd.equals(oldPwd);
    }

    /**
     * Utilizzato per verificare se le due password inserite coincidono.
     *
     * @param password          password
     * @param confirmedPassword password confermata
     * @return true se coincide, false in caso contrario
     */
    public static boolean checkPasswordConfirmed(@NotNull String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }

    /**
     * Utilizzato per verificare se un determinato codice fiscale
     * e' gia' presente o meno nel DB.
     *
     * @param fiscalCode codice fiscale
     * @return true se non esiste ancora, false in caso contrario
     * @see org.project.server.Server#checkDuplicateFiscalCode(String)
     */
    public static boolean checkDuplicateFiscalCode(@NotNull String fiscalCode) {
        try {
            return ServerReference.getServer().checkDuplicateFiscalCode(fiscalCode.toUpperCase(Locale.ROOT));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per verificare se una determinata email
     * e' gia' presente o meno nel DB.
     *
     * @param email email
     * @return true se  non esiste ancora, false in caso contrario
     * @see org.project.server.Server#checkDuplicateEmail(String)
     */
    public static boolean checkDuplicateEmail(String email) {
        try {
            return ServerReference.getServer().checkDuplicateEmail(email);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per verificare se un determinato nickname
     * e' gia' presente o meno nel DB.
     *
     * @param nickname nickname
     * @return true se non esiste ancora, false in caso contrario
     * @see org.project.server.Server#checkDuplicateHubName(String)
     */
    public static boolean checkDuplicateNickname(String nickname) {
        try {
            return ServerReference.getServer().checkDuplicateNickname(nickname);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per verificare se un determinato nome di un centro vaccinale
     * e' gia' presente o meno nel DB.
     *
     * @param name nome del centro
     * @return true se non esiste ancora, false in caso contrario
     * @see org.project.server.Server#checkDuplicateHubName(String)
     */
    public static boolean checkDuplicateHubName(String name) {
        try {
            return ServerReference.getServer().checkDuplicateHubName(name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per verificare se un indirizzo e' gia' presente o meno
     * nel DB.
     *
     * @param address indirizzo completo
     * @return true se non esiste ancora, false in caso contrario
     * @see org.project.server.Server#checkDuplicateAddress(String)
     */
    public static boolean checkDuplicateAddress(String address) {
        try {
            return ServerReference.getServer().checkDuplicateAddress(address);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
