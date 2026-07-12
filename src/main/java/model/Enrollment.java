package model;

public class Enrollment {
    private int studentId;
    private int courseId;
    private String grade;

    // Display fields
    private String courseCode;
    private String courseName;
    private int credits;

    public Enrollment(int studentId, int courseId, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}
