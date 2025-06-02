package lk.ijse.pahasarastudiofp.dto;

public class PaymentDetailsDTO {
    private int paymentId;
    private int appointmentId;
    private double amount;
    // Removed: private String status;

    public PaymentDetailsDTO() {
    }

    public PaymentDetailsDTO(int paymentId, int appointmentId, double amount) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        // Removed: this.status = status;
    }

    // public PaymentDetailsDTO(int paymentId, int appointmentId, double amount) {    }  <--  Redundant Constructor

    // public PaymentDetailsDTO(int paymentId, String status) {  }  <-- Removed Constructor

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.paymentId = appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Removed getStatus()
    // Removed setStatus()

    public Integer getPaymentDetailsId() {
        return paymentId; // Return the paymentId (which seems to be the ID)
    }

    @Override
    public String toString() {
        return "PaymentDetailsDTO{" +
                "paymentId=" + paymentId +
                ", appointmentId=" + appointmentId +
                ", amount=" + amount +
                // Removed: ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return "";
    }
}
