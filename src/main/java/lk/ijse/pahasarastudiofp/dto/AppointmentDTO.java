

package lk.ijse.pahasarastudiofp.dto;

public class AppointmentDTO {
    private int appointmentId;
    private int customerId;
    private int packageId;
    private String name; // This is the specific name/description of the appointment

    public AppointmentDTO() {
    }

    public AppointmentDTO(int appointmentId, int customerId, int packageId, String name) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.name = name;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "appointmentId=" + appointmentId +
                ", customerId=" + customerId +
                ", packageId=" + packageId +
                ", name='" + name + '\'' +
                '}';
    }

    // This method seems redundant based on your DTO structure, but was in your original code.
    // If 'name' is the appointment's name, then getName() already provides it.
    public String getAppointmentName() {
        return this.name; // Assuming 'name' field is the appointment name
    }
}