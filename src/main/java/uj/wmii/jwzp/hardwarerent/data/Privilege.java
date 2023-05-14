package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "privileges")
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter @Getter
    private Long id;
    @Getter @Setter
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @Getter @Setter
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }

}
