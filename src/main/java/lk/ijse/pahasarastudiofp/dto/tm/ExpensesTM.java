package lk.ijse.pahasarastudiofp.dto.tm;

public class ExpensesTM {
    private int expensesId;
    private int employeeId;
    private double amount;
    private String description;
    private String employeeName;
    public ExpensesTM() {
    }

    public ExpensesTM(int expensesId, int employeeId, double amount, String description, String employeeName) {
        this.expensesId = expensesId;
        this.employeeId = employeeId;
        this.amount = amount;
        this.description = description;
        this.employeeName = employeeName;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "ExpensesTM{" +
                "expensesId=" + expensesId +
                ", employeeId=" + employeeId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}