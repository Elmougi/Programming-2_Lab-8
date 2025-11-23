package GUI;

import CourseManagement.Course;
import UserManagement.Certificate;
import UserManagement.Student;

import javax.swing.*;
import java.io.*;

// Apache PDFBox imports
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class CertificateDetailsWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JSeparator separatorTop;
    private JLabel certifiesLabel;
    private JLabel studentNameLabel;
    private JLabel studentIdLabel;
    private JLabel completedLabel;
    private JLabel courseTitleLabel;
    private JSeparator separatorBottom;
    private JLabel certificateIdLabel;
    private JLabel issueDateLabel;
    private JLabel courseIdLabel;
    private JButton closeButton;
    private JButton downloadJSONButton;
    private JButton downloadPDFButton;

    private Certificate certificate;
    private Course course;
    private Student student;

    public CertificateDetailsWindow(JFrame parent, Certificate certificate, Course course, Student student) {
        this.certificate = certificate;
        this.course = course;
        this.student = student;

        setTitle("Certificate Details");
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        studentNameLabel.setText(student.getName());
        studentIdLabel.setText("Student ID: " + certificate.getStudentId());

        String courseTitle = (course != null) ? course.getTitle() : "Course (ID: " + certificate.getCourseId() + ")";
        courseTitleLabel.setText(courseTitle);

        certificateIdLabel.setText("Certificate ID: " + certificate.getCertificateId());
        issueDateLabel.setText("Issue Date: " + certificate.getIssueDate());
        courseIdLabel.setText("Course ID: " + certificate.getCourseId());

        closeButton.addActionListener(e -> dispose());
        downloadJSONButton.addActionListener(e -> downloadAsJSON());
        downloadPDFButton.addActionListener(e -> downloadAsPDF());

        setVisible(true);
    }

    private void downloadAsJSON() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Certificate as JSON");
        fileChooser.setSelectedFile(new File("Certificate_" + certificate.getCertificateId() + ".json"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(fileToSave)) {
                StringBuilder json = new StringBuilder();
                json.append("{\n");
                json.append("  \"certificateId\": \"").append(certificate.getCertificateId()).append("\",\n");
                json.append("  \"courseId\": \"").append(certificate.getCourseId()).append("\",\n");
                json.append("  \"courseTitle\": \"").append(course != null ? course.getTitle() : "N/A").append("\",\n");
                json.append("  \"studentId\": \"").append(certificate.getStudentId()).append("\",\n");
                json.append("  \"studentName\": \"").append(student.getName()).append("\",\n");
                json.append("  \"issueDate\": \"").append(certificate.getIssueDate()).append("\"\n");
                json.append("}\n");

                writer.write(json.toString());

                JOptionPane.showMessageDialog(this,
                        "Certificate saved successfully as JSON!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving certificate: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void downloadAsPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Certificate as PDF");
        fileChooser.setSelectedFile(new File("Certificate_" + certificate.getCertificateId() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    float pageWidth = page.getMediaBox().getWidth();
                    float pageHeight = page.getMediaBox().getHeight();
                    float margin = 50;
                    float yPosition = pageHeight - margin;

                    // Title
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                    String title = "CERTIFICATE OF COMPLETION";
                    float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * 24;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, yPosition);
                    contentStream.showText(title);
                    contentStream.endText();

                    yPosition -= 40;

                    // Draw decorative line
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(pageWidth - margin, yPosition);
                    contentStream.stroke();

                    yPosition -= 60;


                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    String certifies = "This certifies that";
                    float certifiesWidth = PDType1Font.HELVETICA.getStringWidth(certifies) / 1000 * 14;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - certifiesWidth) / 2, yPosition);
                    contentStream.showText(certifies);
                    contentStream.endText();

                    yPosition -= 40;


                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                    String studentName = student.getName();
                    float nameWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(studentName) / 1000 * 20;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - nameWidth) / 2, yPosition);
                    contentStream.showText(studentName);
                    contentStream.endText();

                    yPosition -= 30;


                    contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
                    String studentId = "Student ID: " + certificate.getStudentId();
                    float studentIdWidth = PDType1Font.HELVETICA_OBLIQUE.getStringWidth(studentId) / 1000 * 12;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - studentIdWidth) / 2, yPosition);
                    contentStream.showText(studentId);
                    contentStream.endText();

                    yPosition -= 50;


                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    String completed = "has successfully completed";
                    float completedWidth = PDType1Font.HELVETICA.getStringWidth(completed) / 1000 * 14;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - completedWidth) / 2, yPosition);
                    contentStream.showText(completed);
                    contentStream.endText();

                    yPosition -= 40;


                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    String courseTitle = (course != null) ? course.getTitle() : "Course (ID: " + certificate.getCourseId() + ")";
                    float courseTitleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(courseTitle) / 1000 * 18;
                    contentStream.beginText();
                    contentStream.newLineAtOffset((pageWidth - courseTitleWidth) / 2, yPosition);
                    contentStream.showText(courseTitle);
                    contentStream.endText();

                    yPosition -= 50;


                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(pageWidth - margin, yPosition);
                    contentStream.stroke();

                    yPosition -= 60;


                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("Certificate ID: " + certificate.getCertificateId());
                    contentStream.endText();

                    yPosition -= 15;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("Issue Date: " + certificate.getIssueDate());
                    contentStream.endText();

                    yPosition -= 15;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("Course ID: " + certificate.getCourseId());
                    contentStream.endText();
                }

                document.save(fileToSave);

                JOptionPane.showMessageDialog(this,
                        "Certificate saved successfully as PDF!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving certificate: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}