package lk.ijse.pahasarastudiofp.dto;

public class PackageDTO {
    private int packageId;
    private String name;
    private String description;

    public PackageDTO() {
    }

    public PackageDTO(int packageId, String name, String description) {
        this.packageId = packageId;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PackageDTO{" +
                "packageId=" + packageId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getPackageName() {
        return "";
    }
}