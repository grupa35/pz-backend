package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;

    public UserDTO(String id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof UserDTO)) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;

        if(id != null ? !id.equals(userDTO.id) : userDTO.id != null) {
            return false;
        }
        if(name != null ? !name.equals(userDTO.name) : userDTO.name != null) {
            return false;
        }
        if(surname != null ? !surname.equals(userDTO.surname) : userDTO.surname != null) {
            return false;
        }
        return email != null ? email.equals(userDTO.email) : userDTO.email == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
