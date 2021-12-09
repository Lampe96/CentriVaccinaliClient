package org.project.utils;

import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.project.user.UserVerifyEmailController;

/**
 * Classe utilizzata per impostare la lunghezza massima accettata
 * dalle textField. Utilizzata nel {@link UserVerifyEmailController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class TextFieldLimited extends TextField {

    /**
     * Lunghezza massima accettata
     */
    private int maxLength;

    /**
     * Costruttore
     */
    public TextFieldLimited() {
        this.maxLength = 1;
    }

    /**
     * Impostazione della lunghezza massima
     *
     * @param maxLength maxLenght
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Utilizzato per mantenere la lunghezza impostata a priori,
     * se la textField e' vuota vengono accettati i caratteri
     * in caso contrario quelli presenti vengono sostituiti dai nuovi.
     *
     * @param start inizio del testo
     * @param end   fine del testo
     * @param text  testo
     */
    @Override
    public void replaceText(int start, int end, @NotNull String text) {
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxLength) {
            super.replaceText(start, end, text);
        }
    }

    /**
     * Gestisce l'eventuale copia di testo dall'esterno, andando
     * a troncarlo in caso sia piu' lungo della lunghezza massima
     *
     * @param text testo
     */
    @Override
    public void replaceSelection(@NotNull String text) {
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxLength) {
            if (text.length() > maxLength - getText().length()) {
                text = text.substring(0, maxLength - getText().length());
            }
            super.replaceSelection(text);
        }
    }
}
