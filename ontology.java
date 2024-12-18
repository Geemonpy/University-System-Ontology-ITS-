package com.packages;

import net.sf.clipsrules.jni.CLIPSException;
import net.sf.clipsrules.jni.CaptureRouter;
import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.Router;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class project {
    static JFrame mainFrame = null;
    static String depName = "";
    static String stuName = "";
    static String fc = "";
    static String fl = "";
    static String univ = "";

    public static void startGUI() {
        // Initialize the main frame
        mainFrame = new JFrame("University Information System");
        mainFrame.setSize(800, 600); // Set the window size
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation

        // Use a layout manager for better UI rendering
        JPanel panel = new JPanel();
        panel.setLayout(null); // Absolute positioning
        panel.setBackground(new Color(211, 211, 211)); // Light grey background

        // Input fields and labels
        JLabel i1 = new JLabel("Enter Department Name:");
        i1.setBounds(50, 50, 200, 30);
        i1.setForeground(Color.BLACK); // Black text
        JTextField tf1 = new JTextField();
        tf1.setBounds(300, 50, 400, 30);

        JLabel i2 = new JLabel("Enter Student Name:");
        i2.setBounds(50, 110, 200, 30);
        i2.setForeground(Color.BLACK); // Black text
        JTextField tf2 = new JTextField();
        tf2.setBounds(300, 110, 400, 30);

        JLabel i3 = new JLabel("Enter Faculty Name to get its courses:");
        i3.setBounds(50, 170, 300, 30);
        i3.setForeground(Color.BLACK); // Black text
        JTextField tf3 = new JTextField();
        tf3.setBounds(300, 170, 400, 30);

        JLabel i4 = new JLabel("Enter Faculty Name to get its lecturers:");
        i4.setBounds(50, 230, 300, 30);
        i4.setForeground(Color.BLACK); // Black text
        JTextField tf4 = new JTextField();
        tf4.setBounds(300, 230, 400, 30);

        JLabel i5 = new JLabel("Enter University Name to get its faculties:");
        i5.setBounds(50, 290, 300, 30);
        i5.setForeground(Color.BLACK); // Black text
        JTextField tf5 = new JTextField();
        tf5.setBounds(300, 290, 400, 30);

        // Start button
        JButton startButton = new JButton("Start");
        startButton.setBounds(350, 370, 100, 40);
        startButton.setBackground(new Color(0, 102, 204)); // Blue background
        startButton.setForeground(Color.WHITE); // White text

        // Action listener for the start button
        startButton.addActionListener(e -> {
            depName = tf1.getText();
            stuName = tf2.getText();
            fc = tf3.getText();
            fl = tf4.getText();
            univ = tf5.getText();

            try {
                start();
            } catch (CLIPSException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage());
            }
        });

        // Add components to panel
        panel.add(i1);
        panel.add(tf1);
        panel.add(i2);
        panel.add(tf2);
        panel.add(i3);
        panel.add(tf3);
        panel.add(i4);
        panel.add(tf4);
        panel.add(i5);
        panel.add(tf5);
        panel.add(startButton);

        // Add panel to frame and make it visible
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    public static void start() throws CLIPSException {
        Environment clips = new Environment();
        clips.clear();
        CaptureRouter router = new CaptureRouter(clips, new String[]{Router.STDOUT});

        clips.loadFromString(
                "(defclass department (is-a USER) (role concrete) (multislot departmentName (type STRING)))\n" +
                        "(defclass faculty (is-a USER) (role concrete) (multislot facultyName (type STRING)) (multislot offerCourses (type STRING)))\n" +
                        "(defclass student (is-a USER) (role concrete) (multislot personName (type STRING)) (multislot study (type STRING)))\n" +
                        "(defclass university (is-a USER) (role concrete) (multislot universityName (type STRING)) (multislot consistsFaculties (type STRING)))\n" +

                        "(definstances MAIN::facts \n" +
                        "   ([CS] of department (departmentName \"CS\"))\n" +
                        "   ([Jovan] of student (personName \"Jovan\") (study \"ML, WD\"))\n" +
                        "   ([ComputersandArtificialIntelligence] of faculty \n" +
                        "       (facultyName \"ComputersandArtificialIntelligence\") \n" +
                        "       (offerCourses \"ML, WD,\"))\n" +
                        "   ([YorkStJohnUniversity] of university \n" +
                        "       (universityName \"YorkStJohnUniversity\") \n" +
                        "       (consistsFaculties \"CS, AI,MBA,HEALTH\")))\n" +

                        "(defrule list-tas-for-department \n" +
                        "   (object (is-a department) (departmentName \"" + depName + "\"))\n" +
                        "   => (printout t \"David for department \" \"" + depName + "\" crlf))\n" +

                        "(defrule list-courses-for-student \n" +
                        "   (object (is-a student) (personName \"" + stuName + "\") (study ?courses))\n" +
                        "   => (printout t \"" + stuName + "\" \" studies \" ?courses crlf))\n" +

                        "(defrule list-faculty-offers \n" +
                        "   (object (is-a faculty) (facultyName \"" + fc + "\") (offerCourses ?courses))\n" +
                        "   => (printout t \"" + fc + "\" \" offers courses: \" ?courses crlf))\n" +

                        "(defrule list-university-faculties \n" +
                        "   (object (is-a university) (universityName \"" + univ + "\") (consistsFaculties ?faculties))\n" +
                        "   => (printout t \"" + univ + "\" \" consists of faculties: \" ?faculties crlf))");

        clips.reset();
        clips.run();

        String output = router.getOutput();
        displayOutput(output);
    }

    private static void displayOutput(String output) {
        JFrame outputFrame = new JFrame("Output");
        outputFrame.setSize(600, 400);
        outputFrame.getContentPane().setBackground(new Color(211, 211, 211)); // Light grey background
        JTextArea outputArea = new JTextArea(output);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(245, 245, 245)); // Slightly lighter grey for text area
        outputArea.setForeground(Color.BLACK); // Black text
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputFrame.add(scrollPane);
        outputFrame.setVisible(true);
    }

    public static void main(String[] args) {
        startGUI();
    }
}
