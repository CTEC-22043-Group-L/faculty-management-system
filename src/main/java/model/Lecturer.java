package model;

public class Lecturer {
    private int id;
    private int userId;
    private String fullName;
    private int departmentId;
    private String email;
    private String mobileNumber;

    // Display fields
    private String departmentName;
    private String coursesTeaching;

    public Lecturer(int id, int userId, String fullName, int departmentId, String email, String mobileNumber) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.departmentId = departmentId;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getCoursesTeaching() { return coursesTeaching; }
    public void setCoursesTeaching(String coursesTeaching) { this.coursesTeaching = coursesTeaching; }
}
