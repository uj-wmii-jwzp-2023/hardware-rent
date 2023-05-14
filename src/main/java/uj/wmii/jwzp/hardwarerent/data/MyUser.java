package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class MyUser implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long id;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String username;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String password;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String firstName;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String lastName;
    @Column(name = "email_address",nullable = false, length = 100)
    @Getter @Setter
    private String email;
    @Getter @Setter
    private boolean enabled;
    private boolean tokenExpired;
    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Order> orders;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @Getter @Setter
    private Collection<Role> roles;
    public MyUser() {

    }

    public MyUser(MyUser user) {
        this.username = user.username;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.enabled = user.enabled;
        this.tokenExpired = user.tokenExpired;
        this.roles = user.roles;
    }

    public MyUser(String username,
                  String password,
                  String firstName,
                  String lastName,
                  String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = true;
        this.tokenExpired = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(roles));
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
    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
