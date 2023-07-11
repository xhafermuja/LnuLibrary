package com.example.lnulibrary;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    public NumberTextField() {
        super();
        this.setPromptText("Personal number"); // default value
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9]") || text.equals(".")) {
            super.replaceText(start, end, text);
            formatText();
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (text.matches("[0-9]") || text.equals(".")) {
            super.replaceSelection(text);
            formatText();
        }
    }

    private void formatText() {
        String text = this.getText();
        if (!text.matches("[0-9]*\\.?[0-9]{0,2}")) {
            this.setText(text.substring(0, text.length() - 1));
            this.positionCaret(this.getText().length());
        }
    }

    public int getValue() {
        String text = this.getText();
        if (text.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }
}
