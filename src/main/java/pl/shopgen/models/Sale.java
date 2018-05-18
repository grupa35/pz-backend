package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
public class Sale implements SimpleObject {

    private String id;

    private String code;

    private String productId;

    private double percentValue;

    private BigDecimal nominalValue = new BigDecimal(0.0);

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean active;

    private SaleType saleType;

    public Sale() {
    }

    public Sale(Sale other) {
        if(other != null) {
            id = other.id;
            code = other.code;
            productId = other.productId;
            percentValue = other.percentValue;
            nominalValue = other.nominalValue;
            startDate = other.startDate;
            endDate = other.endDate;
            active = other.active;
            saleType = other.saleType;
        }
    }

    @Override
    final public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        temp = Double.doubleToLongBits(percentValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (nominalValue != null ? nominalValue.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (saleType != null ? saleType.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Sale)) {
            return false;
        }

        Sale sale = (Sale) o;

        if(Double.compare(sale.percentValue, percentValue) != 0) {
            return false;
        }
        if(active != sale.active) {
            return false;
        }
        if(id != null ? !id.equals(sale.id) : sale.id != null) {
            return false;
        }
        if(code != null ? !code.equals(sale.code) : sale.code != null) {
            return false;
        }
        if(productId != null ? !productId.equals(sale.productId) : sale.productId != null) {
            return false;
        }
        if(nominalValue != null ? !nominalValue.equals(sale.nominalValue) : sale.nominalValue != null) {
            return false;
        }
        if(startDate != null ? !startDate.equals(sale.startDate) : sale.startDate != null) {
            return false;
        }
        if(endDate != null ? !endDate.equals(sale.endDate) : sale.endDate != null) {
            return false;
        }
        return saleType == sale.saleType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(double percentValue) {
        this.percentValue = percentValue;
    }

    public BigDecimal getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(BigDecimal nominalValue) {
        this.nominalValue = nominalValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }
}
