package lk.ijse.pahasarastudiofp.dto.tm;

public class EmployeeTM {
    private int employeeId;
    private String name;
    private String description;

    public EmployeeTM() {
    }

    public EmployeeTM(int employeeId, String name, String description) {
        this.employeeId = employeeId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EmployeeTM{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}