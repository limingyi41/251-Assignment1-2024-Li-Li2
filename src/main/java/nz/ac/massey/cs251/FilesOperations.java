package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FilesOperations {
    private JTextPane textPane;
    private JFileChooser fileChooser;
    private String currentFileName = null;

    public FilesOperations(JTextPane textPane) {
        this.textPane = textPane;
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Text Documents", "txt", "html", "md"));
    }

    public void newFile() {
        textPane.setText("");
        currentFileName = null;
    }

    public void openFile() throws IOException {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFileName = file.getName();
            FileReader reader = null;
            try {
                if (file.getName().endsWith(".html") || file.getName().endsWith(".htm")) {
                    textPane.setContentType("text/html");
                    textPane.read(new FileReader(file), null);
                } else if (file.getName().endsWith(".md")) {
                    textPane.setContentType("text/plain");
                    reader = new FileReader(file);
                    textPane.read(reader, null);
                } else {
                    textPane.setContentType("text/plain");
                    reader = new FileReader(file);
                    textPane.read(reader, null);
                }
            } finally {
                if (reader != null) reader.close();
            }
        }
    }

    public void saveFile(boolean saveAs) throws IOException {
        if (currentFileName == null || saveAs) {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                currentFileName = file.getName();
            }
        }
        if (currentFileName != null) {
            FileWriter writer = new FileWriter(new File(fileChooser.getCurrentDirectory(), currentFileName));
            textPane.write(writer);
            writer.close();
        }
    }

    public String getCurrentFileName() {
        return currentFileName;
    }
}
