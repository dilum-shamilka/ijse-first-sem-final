

package lk.ijse.pahasarastudiofp.dto.tm;

public class AppointmentTM {
    private int appointmentId;
    private int customerId;
    private int packageId;
    private String name;
    private String customerName;
    private String packageName;

    public AppointmentTM() {
    }

    public AppointmentTM(int appointmentId, int customerId, int packageId, String name, String customerName, String packageName) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.name = name;
        this.customerName = customerName;
        this.packageName = packageName;
    }


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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppointmentTM{" +
                "appointmentId=" + appointmentId +
                ", customerId=" + customerId +
                ", packageId=" + packageId +
                ", name='" + name + '\'' +
                ", customerName='" + customerName + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}