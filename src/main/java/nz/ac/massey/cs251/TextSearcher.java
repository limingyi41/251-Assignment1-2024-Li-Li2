package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextSearcher {
    private JTextPane textPane;

    public TextSearcher(JTextPane textPane) {
        this.textPane = textPane;
    }

    public void searchAndHighlight(String word) {
        try {

            Highlighter highlighter = textPane.getHighlighter();
            highlighter.removeAllHighlights();


            String content = textPane.getDocument().getText(0, textPane.getDocument().getLength());
            String lowerCaseContent = content.toLowerCase();
            String lowerCaseWord = word.toLowerCase();

            int index = 0;
            while ((index = lowerCaseContent.indexOf(lowerCaseWord, index)) >= 0) {
                int wordEnd = index + word.length();

                highlighter.addHighlight(index, wordEnd, new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                index = wordEnd;
            }
            if (lowerCaseContent.indexOf(lowerCaseWord) == -1) {
                JOptionPane.showMessageDialog(null, "Word not found!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error while searching!" + e.getMessage());
        }
    }
}
