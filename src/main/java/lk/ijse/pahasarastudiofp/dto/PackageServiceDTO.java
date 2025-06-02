package lk.ijse.pahasarastudiofp.dto;

public class PackageServiceDTO {
    private int packageServiceId;
    private int packageId;
    private int serviceId;
    // Add fields for additional attributes if any

    public PackageServiceDTO() {
    }

    public PackageServiceDTO(int packageServiceId, int packageId, int serviceId) {
        this.packageServiceId = packageServiceId;
        this.packageId = packageId;
        this.serviceId = serviceId;
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

    @Override
    public String toString() {
        return "PackageServiceDTO{" +
                "packageServiceId=" + packageServiceId +
                ", packageId=" + packageId +
                ", serviceId=" + serviceId +
                '}';
    }
}