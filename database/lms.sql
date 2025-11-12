CREATE DATABASE learning_management_system;

USE learning_management_system;

-- Users table (students and teachers)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('student', 'teacher') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    teacher_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);

-- Enrollments table
CREATE TABLE enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE KEY unique_enrollment (student_id, course_id)
);
USE learning_management_system;

-- Add test users
INSERT INTO users (username, password, name, email, role) VALUES 
('teacher1', 'password', 'Dr. Smith', 'smith@lms.com', 'teacher'),
('student1', 'password', 'John Doe', 'john@lms.com', 'student'),
('student2', 'password', 'Jane Smith', 'jane@lms.com', 'student');

-- Add test courses
INSERT INTO courses (title, description, teacher_id) VALUES 
('Java Programming', 'Learn Java fundamentals', 1),
('Database Systems', 'SQL and database design', 1);
USE learning_management_system;

-- Enroll students in courses
INSERT INTO enrollments (student_id, course_id) VALUES 
(2, 1), -- John in Java
(3, 1), -- Jane in Java
(2, 2); -- John in Database

-- Add more courses if needed
INSERT INTO courses (title, description, teacher_id) VALUES 
('Web Development', 'Learn HTML, CSS, JavaScript', 1),
('Data Structures', 'Algorithms and data structures', 1);
select*from enrollments;
