package pl.shopgen.models;

public class Role implements SimpleObject {

    private String id;

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    final public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Role)) {
            return false;
        }

        Role role = (Role) o;

        if(id != null ? !id.equals(role.id) : role.id != null) {
            return false;
        }
        return name != null ? name.equals(role.name) : role.name == null;
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
}
