package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Student;
import dao.StudentDAO;

public class ViewStudentsFrame extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    
    public ViewStudentsFrame() {
        studentDAO = new StudentDAO();
        initializeUI();
        loadStudentData();
    }
    
    private void initializeUI() {
        setTitle("View All Students");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("All Students", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Name", "Email", "Age", "Course"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadStudentData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadStudentData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get students from database
        List<Student> students = studentDAO.getAllStudents();
        
        // Add students to table
        for (Student student : students) {
            Object[] rowData = {
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge(),
                student.getCourse()
            };
            tableModel.addRow(rowData);
        }
    }
}