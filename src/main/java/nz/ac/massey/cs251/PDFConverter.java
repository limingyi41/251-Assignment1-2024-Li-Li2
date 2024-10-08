package nz.ac.massey.cs251;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDFConverter {
    private JTextPane textPane;

    public PDFConverter(JTextPane textPane) {
        this.textPane = textPane;
    }

    public void convertToPDF() {
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
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                document.save(saveFile);
            }
        } catch (IOException | BadLocationException ex) {
            ex.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
