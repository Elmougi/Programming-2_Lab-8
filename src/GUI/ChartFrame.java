
package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartFrame extends JFrame {

    private String courseTitle;
    private Map<String, Double> lessonAverages;
    private Map<String, Double> studentScores;
    private double completionPercentage;

    public ChartFrame(String courseTitle, Map<String, Double> lessonAverages,
                      double completionPercentage, Map<String, Double> studentScores) {

        this.courseTitle = courseTitle;
        this.lessonAverages = lessonAverages;
        this.studentScores = studentScores;
        this.completionPercentage = completionPercentage;

        setTitle("Analytics - " + courseTitle);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));

        if (lessonAverages != null && !lessonAverages.isEmpty()) {
            LessonAveragesPanel lessonPanel = new LessonAveragesPanel(lessonAverages);
            tabbedPane.addTab("Quiz Averages per Lesson", lessonPanel);
        }

        if (studentScores != null && !studentScores.isEmpty()) {
            StudentPerformancePanel studentPanel = new StudentPerformancePanel(studentScores);
            tabbedPane.addTab("Student Performance", studentPanel);
        }

        CompletionChartPanel completionPanel = new CompletionChartPanel(completionPercentage);
        tabbedPane.addTab("Course Completion", completionPanel);

        add(tabbedPane);
        setVisible(true);

    }


    class LessonAveragesPanel extends JPanel {
        private Map<String, Double> data;

        public LessonAveragesPanel(Map<String, Double> data) {
            this.data = data;
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(1000, 700));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(new Color(77, 100, 255));
            g2d.drawString("Quiz Averages per Lesson", 400, 40);

            if (data.isEmpty()) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                g2d.drawString("No data available", 450, 400);
                return;
            }

            int fromX = 100;
            int fromY = 100;
            int chartWidth = 900;
            int chartHeight = 500;

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(fromX, fromY + chartHeight, fromX + chartWidth, fromY + chartHeight);
            g2d.drawLine(fromX, fromY, fromX, fromY + chartHeight);

            g2d.setColor(new Color(220, 20, 60));
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Lessons", fromX + chartWidth / 2 - 30, fromY + chartHeight + 50);

            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(-Math.PI / 2);
            g2dRotated.drawString("Average Score (%)", -(fromY + chartHeight / 2 + 50), fromX - 60);
            g2dRotated.dispose();

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 15));
            for (int i = 0; i <= 10; i++) {
                int y = fromY + chartHeight - (i * chartHeight / 10);
                int value = i * 10;

                g2d.drawString(String.valueOf(value), fromX - 30, y + 5);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(fromX, y, fromX + chartWidth, y); // Grid lines
                g2d.setColor(Color.WHITE);
            }

            int barCount = data.size();
            int barWidth = (chartWidth - 800) / barCount;
            int spacing = barWidth / 4;
            int actualBarWidth = barWidth - spacing;
            int x = fromX + 50;
            int index = 0;


            for (Map.Entry<String, Double> entry : data.entrySet()) {
                String lessonName = entry.getKey();
                double score = entry.getValue();
                int barHeight = (int) ((score / 100.0) * chartHeight);
                int barY = fromY + chartHeight - barHeight;

                g2d.setColor(new Color(77, 10, 255));
                g2d.fillRect(x, barY, actualBarWidth, barHeight);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(x, barY, actualBarWidth, barHeight);

                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                String scoreText = String.format("%.1f%%", score);
                int textWidth = g2d.getFontMetrics().stringWidth(scoreText);
                g2d.drawString(scoreText, x + (actualBarWidth - textWidth) / 2, barY - 5);

                g2d.setFont(new Font("Arial", Font.PLAIN, 11));

                if (lessonName.length() > 10) {
                    lessonName = lessonName.substring(0, 8) + "..";
                }

                int labelWidth = g2d.getFontMetrics().stringWidth(lessonName);
                g2d.drawString(lessonName, x + (actualBarWidth - labelWidth) / 2,
                        fromY + chartHeight + 20);
                x += barWidth;
                index++;
            }
        }
    }


    class StudentPerformancePanel extends JPanel {
        private Map<String, Double> data;

        public StudentPerformancePanel(Map<String, Double> data) {
            this.data = data;
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(1000, 700));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(new Color(80, 100, 255));
            g2d.drawString("Student Performance (Total Scores)", 380, 40);

            if (data.isEmpty()) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                g2d.drawString("No data available", 450, 400);
                return;
            }

            int chartX = 100;
            int chartY = 100;
            int chartWidth = 900;
            int chartHeight = 500;

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight);
            g2d.drawLine(chartX, chartY, chartX, chartY + chartHeight);

            g2d.setColor(new Color(220, 20, 60));
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Student ID", chartX + chartWidth / 2 - 30, chartY + chartHeight + 50);

            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(-Math.PI / 2);
            g2dRotated.drawString("Total Score", -(chartY + chartHeight / 2 + 30), chartX - 60);
            g2dRotated.dispose();

            double maxScore = 0;
            for (double score : data.values()) {
                if (score > maxScore) maxScore = score;
            }

            maxScore = Math.ceil(maxScore / 100) * 100;
            if (maxScore == 0) maxScore = 100;

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 15));
            for (int i = 0; i <= 10; i++) {
                int y = chartY + chartHeight - (i * chartHeight / 10);
                double value = (maxScore / 10) * i;

                g2d.drawString(String.format("%.0f", value), chartX - 35, y + 5);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(chartX, y, chartX + chartWidth, y);
                g2d.setColor(Color.WHITE);
            }

            int barCount = data.size();
            int barWidth = (chartWidth - 700) / barCount;
            int spacing = barWidth / 4;
            int actualBarWidth = barWidth - spacing;
            int x = chartX + 50;

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                String studentId = entry.getKey();
                double score = entry.getValue();

                int barHeight = (int) ((score / maxScore) * chartHeight);
                int barY = chartY + chartHeight - barHeight;

                g2d.setColor(new Color(77, 10, 255)); // Forest Green
                g2d.fillRect(x, barY, actualBarWidth, barHeight);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(x, barY, actualBarWidth, barHeight);

                g2d.setFont(new Font("Arial", Font.BOLD, 11));
                String scoreText = String.format("%.1f", score);
                int textWidth = g2d.getFontMetrics().stringWidth(scoreText);
                g2d.drawString(scoreText, x + (actualBarWidth - textWidth) / 2, barY - 5);

                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                int labelWidth = g2d.getFontMetrics().stringWidth(studentId);
                g2d.drawString(studentId, x + (actualBarWidth - labelWidth) / 2,
                        chartY + chartHeight + 20);

                x += barWidth;
            }
        }
    }


    class CompletionChartPanel extends JPanel {
        private double completionPercentage;

        public CompletionChartPanel(double completionPercentage) {
            this.completionPercentage = completionPercentage;
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(1000, 700));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(new Color(70, 100, 255));
            g2d.drawString("Course Completion Rate", 430, 40);

            int centerX = 500;
            int centerY = 350;
            int diameter = 450;

            int completedAngle = (int) (completionPercentage * 3.6); // Convert % to degrees
            int notCompletedAngle = 360 - completedAngle;

            g2d.setColor(new Color(80, 10, 255));
            g2d.fillArc(centerX - diameter/2, centerY - diameter/2,
                    diameter, diameter, 0, completedAngle);

            g2d.setColor(new Color(220, 20, 60)); // Red
            g2d.fillArc(centerX - diameter/2, centerY - diameter/2,
                    diameter, diameter, completedAngle, notCompletedAngle);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(centerX - diameter/2, centerY - diameter/2, diameter, diameter);

            int legendX = 750;
            int legendY = 300;

            g2d.setColor(new Color(80, 10, 255));
            g2d.fillRect(legendX, legendY, 30, 30);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(legendX, legendY, 30, 30);
            g2d.setFont(new Font("Arial", Font.PLAIN, 15));
            g2d.drawString(String.format("Completed: %.1f%%", completionPercentage),
                    legendX + 40, legendY + 20);

            g2d.setColor(new Color(220, 20, 60));
            g2d.fillRect(legendX, legendY + 50, 30, 30);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(legendX, legendY + 50, 30, 30);
            g2d.drawString(String.format("Not Completed: %.1f%%", 100 - completionPercentage),
                    legendX + 40, legendY + 70);

            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String percentText = String.format("%.1f%%", completionPercentage);
            int textWidth = g2d.getFontMetrics().stringWidth(percentText);
            g2d.setColor(Color.WHITE);

            g2d.fillOval(centerX - 50, centerY - 25, 100, 50);

            g2d.setColor(Color.BLACK);
            g2d.drawString(percentText, centerX - textWidth/2, centerY + 8);
        }
    }
}


