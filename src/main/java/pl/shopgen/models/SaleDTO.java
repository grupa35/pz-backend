package pl.shopgen.models;

import java.math.BigDecimal;

public class SaleDTO {

    private boolean isSale;

    private String code;

    private SaleType saleType;

    private BigDecimal saleNominalValue;

    private double salePercentValue;

    private BigDecimal priceAfterSale;

    private BigDecimal saleValue;

    public SaleDTO() {
    }

    public SaleDTO(SaleDTO other) {
        if(other != null) {
            isSale = other.isSale;
            code = other.code;
            saleType = other.saleType;
            saleNominalValue = other.saleNominalValue;
            salePercentValue = other.salePercentValue;
            priceAfterSale = other.priceAfterSale;
            saleValue = other.saleValue;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (isSale ? 1 : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (saleType != null ? saleType.hashCode() : 0);
        result = 31 * result + (saleNominalValue != null ? saleNominalValue.hashCode() : 0);
        temp = Double.doubleToLongBits(salePercentValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (priceAfterSale != null ? priceAfterSale.hashCode() : 0);
        result = 31 * result + (saleValue != null ? saleValue.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof SaleDTO)) {
            return false;
        }

        SaleDTO saleDTO = (SaleDTO) o;

        if(isSale != saleDTO.isSale) {
            return false;
        }
        if(Double.compare(saleDTO.salePercentValue, salePercentValue) != 0) {
            return false;
        }
        if(code != null ? !code.equals(saleDTO.code) : saleDTO.code != null) {
            return false;
        }
        if(saleType != saleDTO.saleType) {
            return false;
        }
        if(saleNominalValue != null
                ? !saleNominalValue.equals(saleDTO.saleNominalValue)
                : saleDTO.saleNominalValue != null) {
            return false;
        }
        if(priceAfterSale != null ? !priceAfterSale.equals(saleDTO.priceAfterSale) : saleDTO.priceAfterSale != null) {
            return false;
        }
        return saleValue != null ? saleValue.equals(saleDTO.saleValue) : saleDTO.saleValue == null;
    }

    public boolean getIsSale() {
        return isSale;
    }

    public void setIsSale(boolean isSale) {
        this.isSale = isSale;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public BigDecimal getSaleNominalValue() {
        return saleNominalValue;
    }

    public void setSaleNominalValue(BigDecimal saleNominalValue) {
        this.saleNominalValue = saleNominalValue;
    }

    public double getSalePercentValue() {
        return salePercentValue;
    }

    public void setSalePercentValue(double salePercentValue) {
        this.salePercentValue = salePercentValue;
    }

    public BigDecimal getPriceAfterSale() {
        return priceAfterSale;
    }

    public void setPriceAfterSale(BigDecimal priceAfterSale) {
        this.priceAfterSale = priceAfterSale;
    }

    public BigDecimal getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(BigDecimal saleValue) {
        this.saleValue = saleValue;
    }

    public static final class Builder {
        private boolean isSale;
        private String code;
        private SaleType saleType;
        private BigDecimal saleNominalValue;
        private double salePercentValue;
        private BigDecimal priceAfterSale;
        private BigDecimal saleValue;

        private Builder() {
        }

        public Builder isSale(boolean isSale) {
            this.isSale = isSale;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder saleType(SaleType saleType) {
            this.saleType = saleType;
            return this;
        }

        public Builder saleNominalValue(BigDecimal saleNominalValue) {
            this.saleNominalValue = saleNominalValue;
            return this;
        }

        public Builder salePercentValue(double salePercentValue) {
            this.salePercentValue = salePercentValue;
            return this;
        }

        public Builder priceAfterSale(BigDecimal priceAfterSale) {
            this.priceAfterSale = priceAfterSale;
            return this;
        }

        public Builder saleValue(BigDecimal saleValue) {
            this.saleValue = saleValue;
            return this;
        }

        public SaleDTO build() {
            SaleDTO saleDTO = new SaleDTO();
            saleDTO.setIsSale(isSale);
            saleDTO.setCode(code);
            saleDTO.setSaleType(saleType);
            saleDTO.setSaleNominalValue(saleNominalValue);
            saleDTO.setSalePercentValue(salePercentValue);
            saleDTO.setPriceAfterSale(priceAfterSale);
            saleDTO.setSaleValue(saleValue);
            return saleDTO;
        }
    }
}
