package pl.shopgen.models;

public class RegistrationCredentialsDTO {

    private String name;

    private String surname;

    private String email;

    private String password;

    private String rePassword;

    private String roleName;

    public RegistrationCredentialsDTO() {

    }

    public RegistrationCredentialsDTO(RegistrationCredentialsDTO other) {
        if (other != null) {
            name = other.name;
            surname = other.surname;
            email = other.email;
            password = other.password;
            rePassword = other.rePassword;
            roleName = other.roleName;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    final public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (rePassword != null ? rePassword.hashCode() : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof RegistrationCredentialsDTO)) {
            return false;
        }

        RegistrationCredentialsDTO that = (RegistrationCredentialsDTO) o;

        if(name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if(surname != null ? !surname.equals(that.surname) : that.surname != null) {
            return false;
        }
        if(email != null ? !email.equals(that.email) : that.email != null) {
            return false;
        }
        if(password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }
        if(rePassword != null ? !rePassword.equals(that.rePassword) : that.rePassword != null) {
            return false;
        }
        return roleName != null ? roleName.equals(that.roleName) : that.roleName == null;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    private static final class Builder {

        private RegistrationCredentialsDTO registrationCredentialsDTO;

        private Builder() {
            registrationCredentialsDTO = new RegistrationCredentialsDTO();
        }

        public Builder name(String name) {
            registrationCredentialsDTO.setName(name);
            return this;
        }

        public Builder surname(String surname) {
            registrationCredentialsDTO.setSurname(surname);
            return this;
        }

        public Builder email(String email) {
            registrationCredentialsDTO.setEmail(email);
            return this;
        }

        public Builder password(String password) {
            registrationCredentialsDTO.setPassword(password);
            return this;
        }

        public Builder rePassword(String rePassword) {
            registrationCredentialsDTO.setRePassword(rePassword);
            return this;
        }

        public Builder roleName(String roleName) {
            registrationCredentialsDTO.setRoleName(roleName);
            return this;
        }

        public RegistrationCredentialsDTO build() {
            return registrationCredentialsDTO;
        }
    }
}
