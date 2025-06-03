package lk.ijse.pahasarastudiofp.dto;

public class CustomerFeedbackDTO {
    private int feedbackId;
    private int customerId;
    private int appointmentId;
    private int rating;

    public CustomerFeedbackDTO() {
    }

    public CustomerFeedbackDTO(int feedbackId, int customerId, int appointmentId, int rating) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.appointmentId = appointmentId;
        this.rating = rating;
    }

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

    @Override
    public String toString() {
        return "CustomerFeedbackDTO{" +
                "feedbackId=" + feedbackId +
                ", customerId=" + customerId +
                ", appointmentId=" + appointmentId +
                ", rating=" + rating +
                '}';
    }
}