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

    public void setTextStyle(Object bold, boolean value) {
    }
}
