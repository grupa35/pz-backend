package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address implements SimpleObject{

    private String id;
    private String addressName;
    private String firstName;
    private String secondName;
    private String postalNumber;
    private String postalCity;
    private String details;

    public Address() {
    }

    public Address(String addressName, String firstName, String secondName, String postalNumber, String postalCity, String details) {
        this.addressName = addressName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.postalNumber = postalNumber;
        this.postalCity = postalCity;
        this.details = details;
    }

    public Address(String id, String addressName, String firstName, String secondName, String postalNumber, String postalCity, String details) {
        this.id = id;
        this.addressName = addressName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.postalNumber = postalNumber;
        this.postalCity = postalCity;
        this.details = details;
    }

    public void cloneProperties(Address address)
    {
        addressName = address.getAddressName();
        firstName = address.getFirstName();
        secondName = address.getSecondName();
        postalNumber = address.getPostalNumber();
        postalCity = address.getPostalCity();
        details = address.getDetails();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPostalNumber() {
        return postalNumber;
    }

    public void setPostalNumber(String postalNumber) {
        this.postalNumber = postalNumber;
    }

    public String getPostalCity() {
        return postalCity;
    }

    public void setPostalCity(String postalCity) {
        this.postalCity = postalCity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    final public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (addressName != null ? addressName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (postalNumber != null ? postalNumber.hashCode() : 0);
        result = 31 * result + (postalCity != null ? postalCity.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Address)) {
            return false;
        }

        Address address = (Address) o;

        if(id != null ? !id.equals(address.id) : address.id != null) {
            return false;
        }
        if(addressName != null ? !addressName.equals(address.addressName) : address.addressName != null) {
            return false;
        }
        if(firstName != null ? !firstName.equals(address.firstName) : address.firstName != null) {
            return false;
        }
        if(secondName != null ? !secondName.equals(address.secondName) : address.secondName != null) {
            return false;
        }
        if(postalNumber != null ? !postalNumber.equals(address.postalNumber) : address.postalNumber != null) {
            return false;
        }
        if(postalCity != null ? !postalCity.equals(address.postalCity) : address.postalCity != null) {
            return false;
        }
        return details != null ? details.equals(address.details) : address.details == null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
