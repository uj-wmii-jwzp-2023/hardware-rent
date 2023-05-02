package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users", schema = "myschema")
public class MyUser {

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
    @OneToMany(mappedBy="user")
    private Set<Orders> orders;
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @Getter @Setter private Collection<Role> roles;
    public MyUser() {

    }
    public MyUser(MyUser user) {
        this.username = user.username;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
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
    }
}
