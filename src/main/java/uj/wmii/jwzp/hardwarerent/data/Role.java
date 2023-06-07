package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import java.util.Collection;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @Getter @Setter
    private Collection<MyUser> users;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    @Getter @Setter
    private Collection<Privilege> privileges;


    public Role(String name) {
        this.name = name;
    }
}
