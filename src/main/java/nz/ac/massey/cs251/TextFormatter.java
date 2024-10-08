package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TextFormatter {
    private JTextPane textPane;

    public TextFormatter(JTextPane textPane) {
        this.textPane = textPane;
    }

    public void setFontColor(Color color) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, color);
        textPane.setCharacterAttributes(attr, false);
    }

    public void setTextStyle(Object style, boolean value) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        if (StyleConstants.Bold.equals(style)) {
            StyleConstants.setBold(attr, value);
        } else if (StyleConstants.Italic.equals(style)) {
            StyleConstants.setItalic(attr, value);
        } else if (StyleConstants.Underline.equals(style)) {
            StyleConstants.setUnderline(attr, value);
        }
        textPane.setCharacterAttributes(attr, false);
    }
}
