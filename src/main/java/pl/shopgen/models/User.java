package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Document
public class User implements SimpleObject, UserDetails {
    private String id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private Role role;
    private boolean enabled = true;

    public User() {
    }

    public User(String name, String surname, String password, String email, Role role) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof User)) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }

        User user = (User) o;

        if(enabled != user.enabled) {
            return false;
        }
        if(name != null ? !name.equals(user.name) : user.name != null) {
            return false;
        }
        if(surname != null ? !surname.equals(user.surname) : user.surname != null) {
            return false;
        }
        if(password != null ? !password.equals(user.password) : user.password != null) {
            return false;
        }
        if(email != null ? !email.equals(user.email) : user.email != null) {
            return false;
        }
        return role != null ? role.equals(user.role) : user.role == null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
