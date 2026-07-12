package model;

public class Department {
    private int id;
    private String name;
    private String hodName;
    private int noOfStaff;

    public Department(int id, String name, String hodName, int noOfStaff) {
        this.id = id;
        this.name = name;
        this.hodName = hodName;
        this.noOfStaff = noOfStaff;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHodName() { return hodName; }
    public void setHodName(String hodName) { this.hodName = hodName; }
    public int getNoOfStaff() { return noOfStaff; }
    public void setNoOfStaff(int noOfStaff) { this.noOfStaff = noOfStaff; }
}