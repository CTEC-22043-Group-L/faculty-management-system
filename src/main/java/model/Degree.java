package model;

public class Degree {
    private int id;
    private String name;
    private int departmentId;
    private int noOfStudents;
    
    // Additional field for display in UI
    private String departmentName;

    public Degree(int id, String name, int departmentId, int noOfStudents) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.noOfStudents = noOfStudents;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public int getNoOfStudents() { return noOfStudents; }
    public void setNoOfStudents(int noOfStudents) { this.noOfStudents = noOfStudents; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
