package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Description implements SimpleObject {

    private String id;

    private String name;

    private String value;

    public Description() {}

    public Description(Description other) {
        if(other != null) {
            id = other.id;
            name = other.name;
            value = other.value;
        }
    }

    @Override
    final public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Description)) {
            return false;
        }

        Description that = (Description) o;

        if(id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if(name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
}
