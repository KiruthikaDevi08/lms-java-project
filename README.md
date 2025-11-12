# Learning Management System

A Java Swing + MySQL desktop application for managing courses and student enrollments.

## Features
- Teacher/Student role-based authentication
- Course creation and management
- Student enrollment system
- MySQL database integration

## Setup
1. Import `database/lms_database.sql` to MySQL
2. Update database credentials in `util/DatabaseConnection.java`
3. Compile: `javac -cp "lib/*" -d . src/*.java src/util/*.java src/model/*.java src/dao/*.java src/ui/*.java`
4. Run: `java -cp ".;lib/*" ui.LoginFrame`

## Technologies
- Java Swing (Frontend)
- MySQL (Database)
- JDBC Connectivity