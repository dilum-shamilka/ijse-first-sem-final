package lk.ijse.pahasarastudiofp.dto.tm;

public class ServiceTM {
    private int serviceId;
    private int packageId;
    private String name;
    private double price;

    public ServiceTM() {
    }

    public ServiceTM(int serviceId, int packageId, String name, double price) {
        this.serviceId = serviceId;
        this.packageId = packageId;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters (same as ServiceDTO for simplicity in table view)
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
        return "ServiceTM{" +
                "serviceId=" + serviceId +
                ", packageId=" + packageId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}