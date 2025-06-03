package lk.ijse.pahasarastudiofp.dto.tm;

public class DiscountTM {
    private int discountId;
    private String name;
    private Double percentage;
    private Double amount;

    public DiscountTM() {
    }

    public DiscountTM(int discountId, String name, Double percentage, Double amount) {
        this.discountId = discountId;
        this.name = name;
        this.percentage = percentage;
        this.amount = amount;
    }


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
        return "DiscountTM{" +
                "discountId=" + discountId +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                ", amount=" + amount +
                '}';
    }
}