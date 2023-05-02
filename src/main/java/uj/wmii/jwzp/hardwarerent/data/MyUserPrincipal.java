package uj.wmii.jwzp.hardwarerent.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class MyUserPrincipal implements UserDetails {
    MyUser user;
    public MyUserPrincipal(MyUser user)
    {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyUser myUser = (MyUser) o;

        return user.getUsername().equals(myUser.getUsername());
    }

    @Override
    public int hashCode() {
        return user.getUsername().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + user.getId() +
                ", username='" + user.getUsername() + '\'' +
                ", password='" + user.getPassword() + '\'' +
                ", firstName='" + user.getFirstName() + '\'' +
                ", lastName='" + user.getLastName() + '\'' +
                ", email='" + user.getEmail() + '\'' +
                '}';
    }
}
