package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentManagementSystem extends JFrame {
    
    public StudentManagementSystem() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Student Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JButton addStudentBtn = new JButton("Add Student");
        JButton viewStudentsBtn = new JButton("View Students");
        
        addStudentBtn.setPreferredSize(new Dimension(150, 40));
        viewStudentsBtn.setPreferredSize(new Dimension(150, 40));
        
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(viewStudentsBtn);
        
        addStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddStudentWindow();
            }
        });
        
        viewStudentsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewStudentsWindow();
            }
        });
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void openAddStudentWindow() {
        AddStudentFrame addStudentFrame = new AddStudentFrame();
        addStudentFrame.setVisible(true);
    }
    
    private void openViewStudentsWindow() {
        ViewStudentsFrame viewStudentsFrame = new ViewStudentsFrame();
        viewStudentsFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        // Simple main method without look and feel
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementSystem().setVisible(true);
            }
        });
    }
}