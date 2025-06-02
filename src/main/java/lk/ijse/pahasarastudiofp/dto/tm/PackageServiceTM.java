package lk.ijse.pahasarastudiofp.dto.tm;

public class PackageServiceTM {
    private int packageServiceId;
    private int packageId;
    private int serviceId;
    private String packageName;   // For display
    private String serviceName;   // For display


    public PackageServiceTM() {
    }

    public PackageServiceTM(int packageServiceId, int packageId, int serviceId, String packageName, String serviceName) {
        this.packageServiceId = packageServiceId;
        this.packageId = packageId;
        this.serviceId = serviceId;
        this.packageName = packageName;
        this.serviceName = serviceName;
    }

    // Getters and Setters
    public int getPackageServiceId() {
        return packageServiceId;
    }

    public void setPackageServiceId(int packageServiceId) {
        this.packageServiceId = packageServiceId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "PackageServiceTM{" +
                "packageServiceId=" + packageServiceId +
                ", packageId=" + packageId +
                ", serviceId=" + serviceId +
                ", packageName='" + packageName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}