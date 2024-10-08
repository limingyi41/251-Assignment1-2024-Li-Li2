package nz.ac.massey.cs251;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyleConstants;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;

public class TextEditor extends JFrame implements ActionListener {
    private JTextPane textPane;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JMenu fileMenu, editMenu, formatMenu, helpMenu;
    private JMenuItem newItem, openItem, saveItem, saveAsItem, printItem, exitItem;
    private JMenuItem copyItem, cutItem, pasteItem, searchItem, undoItem, redoItem;
    private JMenuItem boldItem, italicItem, underlineItem, fontColorItem;
    private JMenuItem wordCountItem, aboutItem, pdfConvertItem;
    private JFileChooser fileChooser;
    private String currentFileName = null;
    private UndoManager undoManager;
    private Timer autoSaveTimer;

    private FilesOperations fileOperations;
    private TextFormatter textFormatter;
    private PDFConverter pdfConverter;
    private UndoRedoManager undoRedoManager;
    private AutoSaveManager autoSaveManager;

    public TextEditor() {
        setTitle("Advanced Text Editor");
        textPane = new JTextPane();
        fileOperations = new FilesOperations(textPane);
        textFormatter = new TextFormatter(textPane);
        pdfConverter = new PDFConverter(textPane);
        undoRedoManager = new UndoRedoManager(textPane);
        autoSaveManager = new AutoSaveManager(fileOperations);

        fileChooser = new JFileChooser();
        undoManager = undoRedoManager.getUndoManager();
        autoSaveTimer = new Timer();
        textPane.setContentType("text/plain");

        add(new JScrollPane(textPane), BorderLayout.CENTER);
        setupMenu();
        setupToolBar();
        setupShortcuts();

        autoSaveManager.startAutoSave();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        textPane.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        formatMenu = new JMenu("Format");
        helpMenu = new JMenu("Help");

        // 文件菜单项
        newItem = new JMenuItem("New");
        newItem.addActionListener(this);
        fileMenu.add(newItem);

        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);

        saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(this);
        fileMenu.add(saveAsItem);

        printItem = new JMenuItem("Print");
        printItem.addActionListener(this);
        fileMenu.add(printItem);

        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(this);
        editMenu.add(undoItem);

        redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(this);
        editMenu.add(redoItem);

        copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy");
        editMenu.add(copyItem);

        cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut");
        editMenu.add(cutItem);

        pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste");
        editMenu.add(pasteItem);

        searchItem = new JMenuItem("Search");
        searchItem.addActionListener(this);
        editMenu.add(searchItem);

        wordCountItem = new JMenuItem("Word Count");
        wordCountItem.addActionListener(this);
        editMenu.add(wordCountItem);

        boldItem = new JMenuItem("Bold");
        boldItem.addActionListener(this);
        formatMenu.add(boldItem);

        italicItem = new JMenuItem("Italic");
        italicItem.addActionListener(this);
        formatMenu.add(italicItem);

        underlineItem = new JMenuItem("Underline");
        underlineItem.addActionListener(this);
        formatMenu.add(underlineItem);

        fontColorItem = new JMenuItem("Font Color");
        fontColorItem.addActionListener(this);
        formatMenu.add(fontColorItem);

        pdfConvertItem = new JMenuItem("Convert to PDF");
        pdfConvertItem.addActionListener(this);
        formatMenu.add(pdfConvertItem);

        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setupToolBar() {
        toolBar = new JToolBar();
        addButtonToToolBar("New", newItem);
        addButtonToToolBar("Open", openItem);
        addButtonToToolBar("Save", saveItem);
        addButtonToToolBar("Undo", undoItem);
        addButtonToToolBar("Redo", redoItem);
        addButtonToToolBar("Print", printItem);
        addButtonToToolBar("Bold", boldItem);
        addButtonToToolBar("Italic", italicItem);
        addButtonToToolBar("Underline", underlineItem);
        addButtonToToolBar("Font Color", fontColorItem);
        add(toolBar, BorderLayout.NORTH);
    }

    private void addButtonToToolBar(String name, JMenuItem menuItem) {
        JButton button = new JButton(name);
        for (ActionListener al : menuItem.getActionListeners()) {
            button.addActionListener(al);
        }
        toolBar.add(button);
    }

    private void setupShortcuts() {
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        searchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source == newItem) {
                fileOperations.newFile();
            } else if (source == openItem) {
                fileOperations.openFile();
            } else if (source == saveItem) {
                fileOperations.saveFile(false);
            } else if (source == saveAsItem) {
                fileOperations.saveFile(true);
            } else if (source == printItem) {
                textPane.print();
            } else if (source == searchItem) {
                searchWord();
            } else if (source == wordCountItem) {
                wordCount();
            } else if (source == boldItem) {
                textFormatter.setTextStyle(StyleConstants.Bold, true);
            } else if (source == italicItem) {
                textFormatter.setTextStyle(StyleConstants.Italic, true);
            } else if (source == underlineItem) {
                textFormatter.setTextStyle(StyleConstants.Underline, true);
            } else if (source == fontColorItem) {
                textFormatter.setFontColor(JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK));
            } else if (source == undoItem) {
                undoRedoManager.undo();
            } else if (source == redoItem) {
                undoRedoManager.redo();
            } else if (source == aboutItem) {
                DialogUtils.showAboutDialog(this);
            } else if (source == pdfConvertItem) {
                pdfConverter.convertToPDF();
            }
        } catch (Exception ex) {
            DialogUtils.showErrorDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void searchWord() {
        String word = JOptionPane.showInputDialog(this, "Enter word to search:");
        if (word != null) {
            String content = textPane.getText().toLowerCase();
            int index = content.indexOf(word.toLowerCase());
            if (index != -1) {
                textPane.setSelectionStart(index);
                textPane.setSelectionEnd(index + word.length());
                textPane.requestFocus();
            } else {
                DialogUtils.showErrorDialog(this, "Word not found!");
            }
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
