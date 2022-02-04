package org.project.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Classe utilizzata per generare id randomici univoci,
 * forniti in fase di vaccinazione. Viene utilizzato
 * nella registrazione di un nuovo vaccinato.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UIdGenerator {

    /**
     * Genera un id univoco basato sul codice fiscale, andando a sommare
     * i valori ASCII di ogni carattere del codice.
     *
     * @param fiscalCode codice fiscale
     * @return id univoco
     */
    public static short generateUId(@NotNull String fiscalCode) {
        int UId = 0;
        Random rnd = new Random();
        for (int i = 0; i < 16; i++) {
            if (i != 15) {
                UId += fiscalCode.charAt(i) * fiscalCode.charAt(i + 1);
            } else {
                UId += fiscalCode.charAt(i) * fiscalCode.charAt(rnd.nextInt(16));
            }
            UId = UId % Short.MAX_VALUE;
        }
        return (short) UId;
    }
}
