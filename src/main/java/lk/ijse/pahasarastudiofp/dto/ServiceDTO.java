package lk.ijse.pahasarastudiofp.dto;

public class ServiceDTO {
    private int serviceId;
    private int packageId; // You might manage package services within the package management
    private String name;
    private double price;

    public ServiceDTO() {
    }

    public ServiceDTO(int serviceId, int packageId, String name, double price) {
        this.serviceId = serviceId;
        this.packageId = packageId;
        this.name = name;
        this.price = price;
    }

    public ServiceDTO(int serviceId, String serviceName, String serviceDescription, double servicePrice) {
    }


    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ServiceDTO{" +
                "serviceId=" + serviceId +
                ", packageId=" + packageId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}