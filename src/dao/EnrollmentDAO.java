package dao;

import model.Enrollment;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    
    // Enroll student in course
    public boolean enrollStudent(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all enrollments with student and course names
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, u.name as student_name, c.title as course_title " +
                    "FROM enrollments e " +
                    "JOIN users u ON e.student_id = u.id " +
                    "JOIN courses c ON e.course_id = c.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseTitle(rs.getString("course_title"));
                enrollments.add(enrollment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    
    // Get enrollments by student
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, c.title as course_title " +
                    "FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.id " +
                    "WHERE e.student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setCourseTitle(rs.getString("course_title"));
                enrollments.add(enrollment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    
    // Check if student is already enrolled in course
    public boolean isStudentEnrolled(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next(); // Returns true if enrollment exists
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}