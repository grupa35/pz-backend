package pl.shopgen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Description extends SimpleObject{

    String name;

    String value;

    public Description() {}

    public Description(Description other) {
        if(other != null) {
            setId(other.getId());
            name = other.name;
            value = other.value;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Description)) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }

        Description that = (Description) o;

        if(name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
