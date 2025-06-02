// File: src/main/java/lk/ijse/pahasarastudiofp/dto/CustomerDTO.java

package lk.ijse.pahasarastudiofp.dto;

public class CustomerDTO {
    private int customerId;
    private String name;
    private String email;
    private String contact;
    private String address;

    public CustomerDTO() {
    }

    public CustomerDTO(int customerId, String name, String email, String contact, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}