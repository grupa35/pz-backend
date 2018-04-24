package pl.shopgen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Document
public class Product extends SimpleObject {

    private String name;

    private BigDecimal price;

    private String imgUrl;

    private Category category;

    private Map<String, Integer> sizeToAmountMap = new HashMap<>();

    private Description description;

    public Product() {}

    public Product(Product other) {
        if(other != null) {
            setId(other.getId());
            this.name = other.name;
            this.price = other.price;
            this.imgUrl = other.imgUrl;
            this.category = other.category;
            this.sizeToAmountMap = other.sizeToAmountMap;
            this.description = other.description;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, Integer> getSizeToAmountMap() {
        return sizeToAmountMap;
    }

    public void setSizeToAmountMap(Map<String, Integer> sizeToAmountMap) {
        this.sizeToAmountMap = sizeToAmountMap;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Product)) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }

        Product product = (Product) o;

        if(name != null ? !name.equals(product.name) : product.name != null) {
            return false;
        }
        if(price != null ? !price.equals(product.price) : product.price != null) {
            return false;
        }
        if(imgUrl != null ? !imgUrl.equals(product.imgUrl) : product.imgUrl != null) {
            return false;
        }
        if(category != null ? !category.equals(product.category) : product.category != null) {
            return false;
        }
        if(!sizeToAmountMap.equals(product.sizeToAmountMap)) {
            return false;
        }
        return description != null ? description.equals(product.description) : product.description == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + sizeToAmountMap.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}

