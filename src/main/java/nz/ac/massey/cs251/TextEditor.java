package nz.ac.massey.cs251;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
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

    public TextEditor() {

        setTitle("Advanced Text Editor");
        textPane = new JTextPane();
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Documents", "txt", "html", "md"));
        undoManager = new UndoManager();
        autoSaveTimer = new Timer();
        textPane.setContentType("text/plain");


        add(new JScrollPane(textPane), BorderLayout.CENTER);
        setupMenu();
        setupToolBar();
        setupShortcuts();


        autoSaveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSave();
            }
        }, 60000, 60000);

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

        // 将菜单栏添加到窗口
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(Action action, String name, JMenu menu) {
        JMenuItem item = new JMenuItem(action);
        item.setText(name);
        menu.add(item);
        return item;
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

    private JMenuItem createMenuItem(String name, JMenu menu) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        menu.add(item);
        return item;
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
                newFile();
            } else if (source == openItem) {
                openFile();
            } else if (source == saveItem) {
                saveFile(false);
            } else if (source == saveAsItem) {
                saveFile(true);
            } else if (source == printItem) {
                printFile();
            } else if (source == exitItem) {
                System.exit(0);
            } else if (source == searchItem) {
                searchWord();
            } else if (source == wordCountItem) {
                wordCount();
            } else if (source == boldItem) {
                setTextStyle(StyleConstants.Bold, true);
            } else if (source == italicItem) {
                setTextStyle(StyleConstants.Italic, true);
            } else if (source == underlineItem) {
                setTextStyle(StyleConstants.Underline, true);
            } else if (source == fontColorItem) {
                setFontColor();
            } else if (source == undoItem) {
                undoAction();
            } else if (source == redoItem) {
                redoAction();
            } else if (source == aboutItem) {
                showAboutDialog();
            } else if (source == pdfConvertItem) {
                convertToPDF();
            }
        } catch (Exception ex) {
            showErrorDialog("Error: " + ex.getMessage());
        }
    }

    private void newFile() {
        textPane.setText("");
        currentFileName = null;
    }

    private void openFile() throws IOException {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
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
            } catch (Exception ex) {
                showErrorDialog("Error opening file: " + ex.getMessage());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    private void saveFile(boolean saveAs) throws IOException {
        if (currentFileName == null || saveAs) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                currentFileName = file.getName();
            }
        }
        if (currentFileName != null) {
            Writer writer = null;
            try {
                writer = new FileWriter(new File(fileChooser.getCurrentDirectory(), currentFileName));
                textPane.write(writer);
            } catch (Exception ex) {
                showErrorDialog("Error saving file: " + ex.getMessage());
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    private void printFile() {
        try {
            textPane.print();
        } catch (PrinterException ex) {
            showErrorDialog("Error printing file: " + ex.getMessage());
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
                showErrorDialog("Word not found!");
            }
        }
    }

    private void wordCount() {
        String content = textPane.getText().trim();
        String[] words = content.split("\\s+");
        showInfoDialog("Word Count: " + words.length);
    }

    private void setTextStyle(Object style, boolean value) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, false);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setUnderline(attr, false);

        if (StyleConstants.Bold.equals(style)) {
            StyleConstants.setBold(attr, value);
        } else if (StyleConstants.Italic.equals(style)) {
            StyleConstants.setItalic(attr, value);
        } else if (StyleConstants.Underline.equals(style)) {
            StyleConstants.setUnderline(attr, value);
        }

        textPane.setCharacterAttributes(attr, false);
    }

    private void setFontColor() {
        Color color = JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK);
        if (color != null) {
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, color);
            textPane.setCharacterAttributes(attr, false);
        }
    }

    private void undoAction() {
        try {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        } catch (CannotUndoException e) {
            showErrorDialog("Cannot undo: " + e.getMessage());
        }
    }

    private void redoAction() {
        try {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        } catch (CannotRedoException e) {
            showErrorDialog("Cannot redo: " + e.getMessage());
        }
    }

    private void convertToPDF() {
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            StyledDocument doc = textPane.getStyledDocument();
            int length = doc.getLength();
            int offset = 0;
            float yPosition = 725;
            float leading = 14.5f;

            contentStream.beginText();
            contentStream.setLeading(leading);
            contentStream.newLineAtOffset(25, yPosition);

            while (offset < length) {
                Element element = doc.getCharacterElement(offset);
                AttributeSet as = element.getAttributes();

                String fontFamily = StyleConstants.getFontFamily(as);
                int fontSize = StyleConstants.getFontSize(as);
                Color color = StyleConstants.getForeground(as);

                if ("Serif".equalsIgnoreCase(fontFamily)) {
                    contentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                } else if ("SansSerif".equalsIgnoreCase(fontFamily)) {
                    contentStream.setFont(PDType1Font.HELVETICA, fontSize);
                } else {
                    contentStream.setFont(PDType1Font.COURIER, fontSize);
                }

                contentStream.setNonStrokingColor(color.getRed(), color.getGreen(), color.getBlue());


                String text = doc.getText(offset, element.getEndOffset() - offset);
                contentStream.showText(text);
                contentStream.newLine();


                offset = element.getEndOffset();
            }

            contentStream.endText();
            contentStream.close();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF File");
            fileChooser.setSelectedFile(new File("output.pdf"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                document.save(saveFile);
                showInfoDialog("PDF file saved successfully at: " + saveFile.getAbsolutePath());
            }
        } catch (IOException | BadLocationException ex) {
            showErrorDialog("Error converting to PDF: " + ex.getMessage());
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void autoSave() {
        if (currentFileName != null) {
            try {
                saveFile(false);
                System.out.println("Auto saved at: " + System.currentTimeMillis());
            } catch (IOException ex) {
                System.err.println("Auto save failed: " + ex.getMessage());
            }
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "Advanced Text Editor\nCreated by: MingYi Li");
    }

    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
    }
}








