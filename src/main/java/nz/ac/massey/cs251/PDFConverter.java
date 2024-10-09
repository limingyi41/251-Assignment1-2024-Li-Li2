package nz.ac.massey.cs251;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

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
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            StyledDocument doc = textPane.getStyledDocument();
            int length = doc.getLength();
            int offset = 0;
            float yPosition = 725;
            float leading = 14.5f;
            float margin = 50;
            float pageWidth = PDRectangle.A4.getWidth() - 2 * margin; // Page width minus margins
            float currentLineWidth = 0; // To track the current line width

            contentStream.beginText();
            contentStream.setLeading(leading);
            contentStream.newLineAtOffset(margin, yPosition);

            while (offset < length) {
                Element element = doc.getCharacterElement(offset);
                AttributeSet as = element.getAttributes();
                String text = doc.getText(offset, element.getEndOffset() - offset);


                PDType1Font font = PDType1Font.HELVETICA;
                if (StyleConstants.isBold(as)) {
                    font = PDType1Font.HELVETICA_BOLD;
                } else if (StyleConstants.isItalic(as)) {
                    font = PDType1Font.HELVETICA_OBLIQUE;
                }
                contentStream.setFont(font, 12);

                Color textColor = StyleConstants.getForeground(as);
                contentStream.setNonStrokingColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue());

                // Handle line breaks in text
                String[] lines = text.split("\n");
                for (String line : lines)                {
                    float lineWidth = font.getStringWidth(line) / 1000 * 12; // Calculate line width

                    if (currentLineWidth + lineWidth > pageWidth) {
                        // If the current line exceeds the page width, move to the next line
                        contentStream.newLine();
                        currentLineWidth = 0;
                    }

                    contentStream.showText(line); // Write text to the page
                    currentLineWidth += lineWidth;

                    // If the line ends with a line break, move to the next line
                    contentStream.newLine();
                    currentLineWidth = 0;

                    yPosition -= leading;
                    if (yPosition < margin) {
                        // If we're out of space on the page, create a new page
                        contentStream.endText();
                        contentStream.close();

                        PDPage newPage = new PDPage(PDRectangle.A4);
                        document.addPage(newPage);
                        contentStream = new PDPageContentStream(document, newPage);
                        contentStream.beginText();
                        contentStream.setLeading(leading);
                        contentStream.newLineAtOffset(margin, PDRectangle.A4.getHeight() - margin);

                        yPosition = PDRectangle.A4.getHeight() - margin;
                    }
                }
                offset = element.getEndOffset();
            }

            contentStream.endText();
            contentStream.close();

            // Save the file with a file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF File");
            fileChooser.setSelectedFile(new File("output.pdf"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                document.save(saveFile);
                JOptionPane.showMessageDialog(null, "PDF file saved successfully at: " + saveFile.getAbsolutePath());
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
