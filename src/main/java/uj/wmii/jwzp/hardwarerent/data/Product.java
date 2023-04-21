package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long product_id;
    private String companyName;
    private String model;
    private BigDecimal price;
    private int availableQuantity;
    private int overallQuantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    public Product() {}
    public Product(String companyName,
                   String model,
                   Category category,
                   int price,
                   int quantity) {
        this.companyName = companyName;
        this.model = model;
        this.price = new BigDecimal(price);
        this.category = category;
        this.availableQuantity = quantity;
        this.overallQuantity = quantity;
    }



    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long id) {
        this.product_id = id;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getOverallQuantity() {
        return overallQuantity;
    }

    public void setOverallQuantity(int overallQuantity) {
        this.overallQuantity = overallQuantity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(Category _category)
    {
        this.category = _category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return product_id.equals(product.product_id);
    }

    @Override
    public int hashCode() {
        return product_id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + product_id +
                ", companyName='" + companyName + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", available=" + availableQuantity +
                ", overall=" + overallQuantity +
                '}';
    }

}
