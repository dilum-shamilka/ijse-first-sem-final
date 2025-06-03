package lk.ijse.pahasarastudiofp.dto;

public class ExpensesDTO {
    private int expensesId;
    private int employeeId;
    private double amount;
    private String description;

    public ExpensesDTO() {
    }

    public ExpensesDTO(int expensesId, int employeeId, double amount, String description) {
        this.expensesId = expensesId;
        this.employeeId = employeeId;
        this.amount = amount;
        this.description = description;
    }

    public int getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(int expensesId) {
        this.expensesId = expensesId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ExpensesDTO{" +
                "expensesId=" + expensesId +
                ", employeeId=" + employeeId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}