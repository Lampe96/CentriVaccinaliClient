package org.project.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Classe utilizzata per generare id randomici univoci,
 * forniti in fase di vaccinazione. Viene utilizzato in
 * nella registrazione di un nuovo vaccinato
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UIdGenerator {

    /**
     * Genera un id univoco basato sul codice fiscale, andando a sommare
     * i valori ASCII di ogni carattere del codice
     *
     * @param fiscalCode codice fiscale
     * @return id univoco
     */
    public static short generateUId(@NotNull String fiscalCode) {
        short UId = 0;
        for (int i = 0; i < fiscalCode.length(); i++) {
            UId += fiscalCode.charAt(i);
        }
        return UId;
    }
}
