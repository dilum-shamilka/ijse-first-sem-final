package lk.ijse.pahasarastudiofp.dto.tm;

public class PaymentDetailsTM {
    private int paymentId;
    private int appointmentId;
    private double amount;
    private String appointmentName;

    public PaymentDetailsTM() {
    }

    public PaymentDetailsTM(int paymentId, int appointmentId, double amount, String appointmentName) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.appointmentName = appointmentName;
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
        this.appointmentId = appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    @Override
    public String toString() {
        return "PaymentDetailsTM{" +
                "paymentId=" + paymentId +
                ", appointmentId=" + appointmentId +
                ", amount=" + amount +
                ", appointmentName='" + appointmentName + '\'' +
                '}';
    }
}