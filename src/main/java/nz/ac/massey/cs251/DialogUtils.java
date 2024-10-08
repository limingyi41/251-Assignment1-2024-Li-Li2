package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;

public class DialogUtils {
    public static void showAboutDialog(Component parent) {
        JOptionPane.showMessageDialog(parent, "Text Editor\nCreated by: MingYi Li");
    }

    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
