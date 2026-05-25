package com.example.postgres.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateService {

    private static final String TEMPLATE_PATH = "src/main/resources/certificates/1st.pdf";

    public byte[] generateCertificate(String studentName, String level, Integer marks, String teacherName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Determine which template to use based on level
            String templateFile = getTemplateForLevel(level);
            if (templateFile == null) {
                throw new RuntimeException("No certificate template available for level: " + level);
            }

            PdfReader reader = new PdfReader(new FileInputStream(templateFile));
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);

            // Get first page
            PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.getFirstPage());
            Canvas canvas = new Canvas(pdfCanvas, new Rectangle(0, 0, 595, 842));

            // Use Times Italic font - more italic than Helvetica
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);

            // Line 1: "This is to certify that," - CENTERED
            Paragraph certifyText = new Paragraph("This is to certify that,")
                    .setFont(regularFont)
                    .setFontSize(18)
                    .setFontColor(new DeviceRgb(0, 0, 0))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(50, 310, 495);
            canvas.add(certifyText);

            // Main paragraph: Name in RED + flowing text - JUSTIFIED (fills line completely)
            // Text flows naturally and fills each line from edge to edge
            // Increased width to use more space, centered on page
            Paragraph mainParagraph = new Paragraph()
                    .add(new Text(studentName).setFont(regularFont).setFontColor(new DeviceRgb(231, 76, 60)))
                    .add(new Text(" Has successfully Completed the " + level + " level in Mental Arithmetic programme of Everest    Abacus Academy. We wish  your grand success in your career.")
                            .setFont(regularFont).setFontColor(new DeviceRgb(0, 0, 0)))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFixedPosition(72, 160, 650)
                    .setHeight(140);
            canvas.add(mainParagraph);

            // Date - left side
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            Paragraph datePara = new Paragraph("Date - " + currentDate)
                    .setFont(regularFont)
                    .setFontSize(13)
                    .setFontColor(new DeviceRgb(0, 0, 0))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFixedPosition(160, 150, 200);
            canvas.add(datePara);

            // Marks - right side, RED
            Paragraph marksPara = new Paragraph("Marks - " + marks)
                    .setFont(regularFont)
                    .setFontSize(13)
                    .setFontColor(new DeviceRgb(231, 76, 60))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFixedPosition(360, 150, 200);
            canvas.add(marksPara);

            // Teacher name - CENTERED, half of main font size (18÷2 = 9)
            Paragraph teacherPara = new Paragraph(teacherName)
                    .setFont(regularFont)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(0, 0, 0))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(50, 70, 495);
            canvas.add(teacherPara);

            canvas.close();
            pdfDoc.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating certificate: " + e.getMessage(), e);
        }
    }

    public String getLevelText(Integer levelNumber) {
        String[] levels = {
            "", "First", "Second", "Third", "Fourth", "Fifth",
            "Sixth", "Seventh", "Eighth", "Ninth", "Tenth"
        };
        
        if (levelNumber != null && levelNumber > 0 && levelNumber < levels.length) {
            return levels[levelNumber];
        }
        return "First";
    }

    /**
     * Get the template file path based on level
     * @param level - Level text ("First", "Second", etc.)
     * @return Template file path, or null if not available
     */
    private String getTemplateForLevel(String level) {
        // Map level text to template file
        switch (level) {
            case "First":
                return "src/main/resources/certificates/1st.pdf";
            case "Second":
                return "src/main/resources/certificates/2nd.pdf";
            case "Third":
                return "src/main/resources/certificates/3rd.pdf";
            case "Fourth":
                return "src/main/resources/certificates/4th.pdf";
            case "Fifth":
                return "src/main/resources/certificates/5th.pdf";
            case "Sixth":
                return "src/main/resources/certificates/6th.pdf";
            case "Seventh":
                return "src/main/resources/certificates/7th.pdf";
            case "Eighth":
                return "src/main/resources/certificates/8th.pdf";
            case "Ninth":
                return "src/main/resources/certificates/9th.pdf";
            default:
                // For Foundation (level 0) and Tenth (level 10), return null
                return null;
        }
    }
}
