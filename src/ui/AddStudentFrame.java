
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Student;
import dao.StudentDAO;

public class AddStudentFrame extends JFrame {
    private JTextField nameField, emailField, ageField, courseField;
    private JButton addButton, clearButton;
    private StudentDAO studentDAO;
    
    public AddStudentFrame() {
        studentDAO = new StudentDAO();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Add New Student");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Add New Student", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Form fields
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);
        
        formPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        formPanel.add(courseField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Student");
        clearButton = new JButton("Clear");
        
        addButton.addActionListener(new AddStudentListener());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        ageField.setText("");
        courseField.setText("");
    }
    
    private class AddStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String ageText = ageField.getText().trim();
                String course = courseField.getText().trim();
                
                // Validation
                if (name.isEmpty() || email.isEmpty() || ageText.isEmpty() || course.isEmpty()) {
                    JOptionPane.showMessageDialog(AddStudentFrame.this, 
                        "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int age = Integer.parseInt(ageText);
                
                // Create student object
                Student student = new Student(name, email, age, course);
                
                // Add to database
                boolean success = studentDAO.addStudent(student);
                
                if (success) {
                    JOptionPane.showMessageDialog(AddStudentFrame.this, 
                        "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(AddStudentFrame.this, 
                        "Failed to add student. Email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddStudentFrame.this, 
                    "Please enter a valid age!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}