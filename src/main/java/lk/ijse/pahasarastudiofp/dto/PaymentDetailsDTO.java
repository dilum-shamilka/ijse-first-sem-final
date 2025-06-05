package lk.ijse.pahasarastudiofp.dto;

public class PaymentDetailsDTO {
    private int paymentId;
    private int appointmentId;
    private double amount;


    public PaymentDetailsDTO() {
    }

    public PaymentDetailsDTO(int paymentId, int appointmentId, double amount) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.amount = amount;

    }

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



    public Integer getPaymentDetailsId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return "PaymentDetailsDTO{" +
                "paymentId=" + paymentId +
                ", appointmentId=" + appointmentId +
                ", amount=" + amount +
                '}';
    }

    public String getStatus() {
        return "";
    }
}
