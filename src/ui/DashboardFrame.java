package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import dao.CourseDAO;
import dao.EnrollmentDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Course;
import model.Enrollment;
import model.User;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Learning Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Navigation and content area
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        // Show appropriate dashboard based on role
        showRoleBasedDashboard();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Learning Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Navigation panel (left sidebar)
        JPanel navPanel = createNavigationPanel();
        contentPanel.add(navPanel, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(240, 240, 240));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setPreferredSize(new Dimension(200, 0));

        if (currentUser.getRole().equals("teacher")) {
            // Teacher navigation
            addNavButton(navPanel, "My Courses", "TEACHER_COURSES");
            addNavButton(navPanel, "Create Course", "CREATE_COURSE");
            addNavButton(navPanel, "View Enrollments", "VIEW_ENROLLMENTS");
        } else {
            // Student navigation
            addNavButton(navPanel, "Available Courses", "AVAILABLE_COURSES");
            addNavButton(navPanel, "My Enrollments", "MY_ENROLLMENTS");
        }

        // Common buttons
        navPanel.add(Box.createVerticalStrut(20));
        addNavButton(navPanel, "Logout", "LOGOUT");

        return navPanel;
    }

    private void addNavButton(JPanel panel, String text, String action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 35));
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNavigation(action);
            }
        });

        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }

    private void handleNavigation(String action) {
        switch (action) {
            case "LOGOUT":
                logout();
                break;
            case "TEACHER_COURSES":
                showTeacherCourses();
                break;
            case "CREATE_COURSE":
                showCreateCourse();
                break;
            case "VIEW_ENROLLMENTS":
                showAllEnrollments();
                break;
            case "AVAILABLE_COURSES":
                showAvailableCourses();
                break;
            case "MY_ENROLLMENTS":
                showMyEnrollments();
                break;
        }
    }

    private void showRoleBasedDashboard() {
        if (currentUser.getRole().equals("teacher")) {
            showTeacherCourses();
        } else {
            showAvailableCourses();
        }
    }

    // Placeholder methods for different views
    private void showTeacherCourses() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel titleLabel = new JLabel("My Courses", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Get courses from database
    CourseDAO courseDAO = new CourseDAO();
    java.util.List<Course> courses = courseDAO.getCoursesByTeacher(currentUser.getId());

    // Create table
    String[] columnNames = {"ID", "Title", "Description", "Students"};
    Object[][] data = new Object[courses.size()][4];
    
    for (int i = 0; i < courses.size(); i++) {
        Course course = courses.get(i);
        data[i][0] = course.getId();
        data[i][1] = course.getTitle();
        data[i][2] = course.getDescription();
        data[i][3] = "View"; // Placeholder for student count
    }

    JTable courseTable = new JTable(data, columnNames);
    courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    JScrollPane scrollPane = new JScrollPane(courseTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Refresh button
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showTeacherCourses(); // Refresh the view
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(refreshButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.add(panel, "TEACHER_COURSES");
    cardLayout.show(mainPanel, "TEACHER_COURSES");
}

    private void showCreateCourse() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Title
    JLabel titleLabel = new JLabel("Create New Course", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Form panel
    JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    JTextField titleField = new JTextField();
    JTextArea descriptionArea = new JTextArea(3, 20);
    JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

    formPanel.add(new JLabel("Course Title:"));
    formPanel.add(titleField);
    formPanel.add(new JLabel("Description:"));
    formPanel.add(descriptionScroll);
    
    // Teacher ID is automatically set to current user
    formPanel.add(new JLabel("Teacher:"));
    formPanel.add(new JLabel(currentUser.getName()));

    JButton createButton = new JButton("Create Course");
    formPanel.add(new JLabel(""));
    formPanel.add(createButton);

    panel.add(formPanel, BorderLayout.CENTER);

    // Create button action
    createButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(DashboardFrame.this, 
                    "Please enter course title!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create course object
            Course course = new Course(title, description, currentUser.getId());
            
            // Save to database
            CourseDAO courseDAO = new CourseDAO();
            boolean success = courseDAO.addCourse(course);
            
            if (success) {
                JOptionPane.showMessageDialog(DashboardFrame.this, 
                    "Course created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                titleField.setText("");
                descriptionArea.setText("");
            } else {
                JOptionPane.showMessageDialog(DashboardFrame.this, 
                    "Failed to create course!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    mainPanel.add(panel, "CREATE_COURSE");
    cardLayout.show(mainPanel, "CREATE_COURSE");
}

    private void showAllEnrollments() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel titleLabel = new JLabel("All Course Enrollments", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Get all enrollments from database
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    java.util.List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();

    // Create table
    String[] columnNames = {"Enrollment ID", "Student Name", "Course Title", "Enrollment Date"};
    Object[][] data = new Object[enrollments.size()][4];
    
    for (int i = 0; i < enrollments.size(); i++) {
        Enrollment enrollment = enrollments.get(i);
        data[i][0] = enrollment.getId();
        data[i][1] = enrollment.getStudentName();
        data[i][2] = enrollment.getCourseTitle();
        data[i][3] = "Recent"; // Simplified for now
    }

    JTable enrollmentsTable = new JTable(data, columnNames);
    enrollmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Stats panel
    JPanel statsPanel = new JPanel(new FlowLayout());
    statsPanel.add(new JLabel("Total Enrollments: " + enrollments.size()));
    
    panel.add(statsPanel, BorderLayout.NORTH);

    // Refresh button
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAllEnrollments(); // Refresh the view
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(refreshButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.add(panel, "VIEW_ENROLLMENTS");
    cardLayout.show(mainPanel, "VIEW_ENROLLMENTS");
}
    private void showAvailableCourses() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel titleLabel = new JLabel("Available Courses", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Get all courses from database
    CourseDAO courseDAO = new CourseDAO();
    java.util.List<Course> courses = courseDAO.getAllCourses();
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    // Create a panel to hold course cards instead of table
    JPanel coursesPanel = new JPanel();
    coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

    for (Course course : courses) {
        JPanel courseCard = createCourseCard(course, enrollmentDAO);
        coursesPanel.add(courseCard);
        coursesPanel.add(Box.createVerticalStrut(10));
    }

    JScrollPane scrollPane = new JScrollPane(coursesPanel);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Refresh button
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAvailableCourses(); // Refresh the view
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(refreshButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.add(panel, "AVAILABLE_COURSES");
    cardLayout.show(mainPanel, "AVAILABLE_COURSES");
}

private JPanel createCourseCard(Course course, EnrollmentDAO enrollmentDAO) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    card.setBackground(Color.WHITE);

    // Course info
    JPanel infoPanel = new JPanel(new GridLayout(3, 1));
    infoPanel.setBackground(Color.WHITE);
    
    JLabel titleLabel = new JLabel(course.getTitle());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    
    JLabel descLabel = new JLabel("<html>" + course.getDescription() + "</html>");
    JLabel teacherLabel = new JLabel("Teacher: " + course.getTeacherName());

    infoPanel.add(titleLabel);
    infoPanel.add(descLabel);
    infoPanel.add(teacherLabel);

    card.add(infoPanel, BorderLayout.CENTER);

    // Enroll button
    boolean isEnrolled = enrollmentDAO.isStudentEnrolled(currentUser.getId(), course.getId());
    JButton enrollButton = new JButton(isEnrolled ? "Already Enrolled" : "Enroll");
    enrollButton.setEnabled(!isEnrolled);
    
    enrollButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enrollInCourse(course);
        }
    });

    card.add(enrollButton, BorderLayout.EAST);

    return card;
}
    private void showMyEnrollments() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel titleLabel = new JLabel("My Enrollments", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Get student's enrollments
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    java.util.List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudent(currentUser.getId());

    // Create table
    String[] columnNames = {"Course ID", "Course Title", "Enrollment Date"};
    Object[][] data = new Object[enrollments.size()][3];
    
    for (int i = 0; i < enrollments.size(); i++) {
        Enrollment enrollment = enrollments.get(i);
        data[i][0] = enrollment.getCourseId();
        data[i][1] = enrollment.getCourseTitle();
        data[i][2] = "Recent"; // Simplified for now
    }

    JTable enrollmentsTable = new JTable(data, columnNames);
    enrollmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Refresh button
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMyEnrollments(); // Refresh the view
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(refreshButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.add(panel, "MY_ENROLLMENTS");
    cardLayout.show(mainPanel, "MY_ENROLLMENTS");
}

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    
    }
    
// Helper method to handle enrollment
private void enrollInCourse(Course course) {
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    
    // Check if already enrolled
    if (enrollmentDAO.isStudentEnrolled(currentUser.getId(), course.getId())) {
        JOptionPane.showMessageDialog(this, 
            "You are already enrolled in this course!", "Info", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    // Enroll student
    Enrollment enrollment = new Enrollment(currentUser.getId(), course.getId());
    boolean success = enrollmentDAO.enrollStudent(enrollment);
    
    if (success) {
        JOptionPane.showMessageDialog(this, 
            "Successfully enrolled in: " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
        showAvailableCourses(); // Refresh the view
    } else {
        JOptionPane.showMessageDialog(this, 
            "Failed to enroll in course!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}
