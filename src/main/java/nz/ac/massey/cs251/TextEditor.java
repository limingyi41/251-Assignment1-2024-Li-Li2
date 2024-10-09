package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.io.IOException;
import javax.swing.text.StyleConstants;

public class TextEditor extends JFrame {
    private JTextPane textPane;
    private FilesOperations filesOperations;
    private UndoRedoManager undoRedoManager;
    private AutoSaveManager autoSaveManager;
    private TextFormatter textFormatter;
    private PDFConverter pdfConverter;

    public TextEditor() {
        setTitle("Advanced Text Editor");

        textPane = new JTextPane();
        filesOperations = new FilesOperations(textPane);
        undoRedoManager = new UndoRedoManager(textPane);
        autoSaveManager = new AutoSaveManager(filesOperations);
        textFormatter = new TextFormatter(textPane);
        pdfConverter = new PDFConverter(textPane);
        autoSaveManager.startAutoSave();
        setupMenuBar();
        setupUI();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu formatMenu = new JMenu("Format");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> filesOperations.newFile());
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> {
            try {
                filesOperations.openFile();
            } catch (IOException ex) {
                DialogUtils.showErrorDialog(this, "Error opening file: " + ex.getMessage());
            }
        });
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            try {
                filesOperations.saveFile(false);
            } catch (IOException ex) {
                DialogUtils.showErrorDialog(this, "Error saving file: " + ex.getMessage());
            }
        });
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(e -> {
            try {
                filesOperations.saveFile(true);
            } catch (IOException ex) {
                DialogUtils.showErrorDialog(this, "Error saving file: " + ex.getMessage());
            }
        });
        fileMenu.add(saveAsItem);

        JMenuItem printItem = new JMenuItem("Print");
        printItem.addActionListener(e -> {
            try {
                textPane.print();
            } catch (Exception ex) {
                DialogUtils.showErrorDialog(this, "Error printing file: " + ex.getMessage());
            }
        });
        fileMenu.add(printItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> undoRedoManager.undo());
        editMenu.add(undoItem);
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> undoRedoManager.redo());
        editMenu.add(redoItem);

        JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy");
        editMenu.add(copyItem);

        JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut");
        editMenu.add(cutItem);
        JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste");
        editMenu.add(pasteItem);
        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.addActionListener(e -> searchWord());
        editMenu.add(searchItem);
        JMenuItem wordCountItem = new JMenuItem("Word Count");
        wordCountItem.addActionListener(e -> wordCount());
        editMenu.add(wordCountItem);
        JMenuItem boldItem = new JMenuItem("Bold");
        boldItem.addActionListener(e -> textFormatter.setTextStyle(StyleConstants.Bold, true));
        formatMenu.add(boldItem);
        JMenuItem italicItem = new JMenuItem("Italic");
        italicItem.addActionListener(e -> textFormatter.setTextStyle(StyleConstants.Italic, true));
        formatMenu.add(italicItem);
        JMenuItem underlineItem = new JMenuItem("Underline");
        underlineItem.addActionListener(e -> textFormatter.setTextStyle(StyleConstants.Underline, true));
        formatMenu.add(underlineItem);
        JMenuItem fontColorItem = new JMenuItem("Font Color");
        fontColorItem.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK);
            if (color != null) {
                textFormatter.setFontColor(color);
            }
        });
        formatMenu.add(fontColorItem);
        JMenuItem pdfConvertItem = new JMenuItem("Convert to PDF");
        pdfConvertItem.addActionListener(e -> pdfConverter.convertToPDF());
        formatMenu.add(pdfConvertItem);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> DialogUtils.showAboutDialog(this));
        helpMenu.add(aboutItem);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    private void setupUI() {
        add(new JScrollPane(textPane), BorderLayout.CENTER);
    }

    private void searchWord() {
        String word = JOptionPane.showInputDialog(this, "Enter word to search:");
        if (word != null && !word.trim().isEmpty()) {
            TextSearcher textSearcher = new TextSearcher(textPane);
            textSearcher.searchAndHighlight(word);
        }
    }
    
    private void wordCount() {
        String content = textPane.getText().trim();
        String[] words = content.split("\\s+");
        DialogUtils.showInfoDialog(this, "Word Count: " + words.length);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
    }
}
