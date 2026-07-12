-- Create the database
CREATE DATABASE IF NOT EXISTS fms_db;
USE fms_db;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Student', 'Lecturer') NOT NULL
);

-- Departments table
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    hod_name VARCHAR(100),
    no_of_staff INT DEFAULT 0
);

-- Degrees table
CREATE TABLE IF NOT EXISTS degrees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department_id INT,
    no_of_students INT DEFAULT 0,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    student_id_str VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    degree_id INT,
    email VARCHAR(100),
    mobile_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (degree_id) REFERENCES degrees(id) ON DELETE SET NULL
);

-- Lecturers table
CREATE TABLE IF NOT EXISTS lecturers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    full_name VARCHAR(100) NOT NULL,
    department_id INT,
    email VARCHAR(100),
    mobile_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(id) ON DELETE SET NULL
);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT,
    course_id INT,
    grade VARCHAR(5),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Insert a default admin user (password is 'admin123' for demonstration, hash should be implemented in Java, here we store plain or a dummy hash for now, but let's use plain 'admin123' since it's a student project unless specified otherwise. We will hash in Java later.)
-- Let's just insert 'admin123' directly and we will match it directly or assume it's hashed later. For a complete system, we should use BCrypt in Java.
INSERT IGNORE INTO users (username, password_hash, role) VALUES ('admin', 'admin123', 'Admin');

-- Dummy data for testing UI
INSERT IGNORE INTO departments (name, hod_name, no_of_staff) VALUES 
('Applied Computing', 'Kumar Sanga', 15),
('Software Engineering', 'Kumar Sanga', 17),
('Computer Systems Engineering', 'Kumar Sanga', 12);

INSERT IGNORE INTO degrees (name, department_id, no_of_students) VALUES 
('Engineering Technology', 1, 375),
('Information Technology', 2, 375),
('Computer Science', 3, 325),
('Bio Systems Technology', 1, 75);

INSERT IGNORE INTO courses (course_code, course_name, credits) VALUES
('ETEC 21062', 'OOP', 2),
('ETEC 21052', 'OOP', 2),
('ETEC 21042', 'OOP', 2),
('ETEC 21032', 'OOP', 2);
