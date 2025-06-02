package lk.ijse.pahasarastudiofp.dto.tm;

public class CustomerFeedbackTM {
    private int feedbackId;
    private int customerId;
    private int appointmentId;
    private int rating;
    private String customerName; // To display customer name
    private String appointmentName; // To display appointment name

    public CustomerFeedbackTM() {
    }

    public CustomerFeedbackTM(int feedbackId, int customerId, int appointmentId, int rating, String customerName, String appointmentName) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.customerName = customerName;
        this.appointmentName = appointmentName;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    @Override
    public String toString() {
        return "CustomerFeedbackTM{" +
                "feedbackId=" + feedbackId +
                ", customerId=" + customerId +
                ", appointmentId=" + appointmentId +
                ", rating=" + rating +
                ", customerName='" + customerName + '\'' +
                ", appointmentName='" + appointmentName + '\'' +
                '}';
    }
}