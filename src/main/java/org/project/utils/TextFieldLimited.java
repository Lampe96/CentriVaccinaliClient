package org.project.utils;

import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

public class TextFieldLimited extends TextField {
    private int maxLength;

    public TextFieldLimited() {
        this.maxLength = 1;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void replaceText(int start, int end, @NotNull String text) {
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxLength) {
            super.replaceText(start, end, text);
        }
    }

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
