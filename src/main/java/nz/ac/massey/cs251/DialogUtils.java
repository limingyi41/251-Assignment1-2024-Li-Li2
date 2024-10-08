package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.Component;
public class DialogUtils {
    public static void showAboutDialog(Component parent) {
        JOptionPane.showMessageDialog(parent, "Advanced Text Editor\nCreated by: MingYi Li");
    }

    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showAboutDialog(TextEditor textEditor) {
    }

    public static void showErrorDialog(TextEditor textEditor, String message) {
    }
}
