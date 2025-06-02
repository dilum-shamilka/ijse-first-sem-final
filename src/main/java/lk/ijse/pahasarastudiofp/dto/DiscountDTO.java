package lk.ijse.pahasarastudiofp.dto;

public class DiscountDTO {
    private int discountId;
    private String discountName;
    private String name;
    private Double percentage;
    private Double amount;

    public DiscountDTO() {
    }

    public DiscountDTO(int discountId, String name, Double percentage, Double amount) {
        this.discountId = discountId;
        this.name = name;
        this.percentage = percentage;
        this.amount = amount;
    }

    public DiscountDTO(int discountId, String discountName, double discountPercentage) {
        this.discountId = discountId;
        this.discountName = discountName;
    }

    // Getters and Setters
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DiscountDTO{" +
                "discountId=" + discountId +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                ", amount=" + amount +
                '}';
    }
}