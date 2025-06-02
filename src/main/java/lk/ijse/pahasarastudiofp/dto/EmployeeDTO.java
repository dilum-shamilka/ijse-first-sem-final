package lk.ijse.pahasarastudiofp.dto;

public class EmployeeDTO {
    private int employeeId;
    private String employeeName;
    private String name;
    private String description;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int employeeId, String name, String description) {
        this.employeeId = employeeId;
        this.name = name;
        this.description = description;
    }

    public EmployeeDTO(int employeeId, String employeeName, String employeeAddress, String employeeContact, String employeeNic) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
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
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}