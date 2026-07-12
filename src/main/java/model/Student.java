package model;

public class Student {
    private int id;
    private int userId;
    private String studentIdStr;
    private String fullName;
    private int degreeId;
    private String email;
    private String mobileNumber;

    //for display field
    private String degreeName;
    public Student(int id, int userId, String studentIdStr, String fullName, int degreeId, String email, String mobileNumber) {
        this.id = id;
        this.userId = userId;
        this.studentIdStr = studentIdStr;
        this.fullName = fullName;
        this.degreeId = degreeId;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getStudentIdStr() { return studentIdStr; }
    public void setStudentIdStr(String studentIdStr) { this.studentIdStr = studentIdStr; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }
}
