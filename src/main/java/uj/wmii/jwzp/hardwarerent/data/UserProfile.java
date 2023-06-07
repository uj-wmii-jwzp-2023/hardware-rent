package uj.wmii.jwzp.hardwarerent.data;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
public class UserProfile {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private String email;

    @Getter @Setter
    private BigDecimal cash = new BigDecimal("0.00");
    public UserProfile(String username, String firstName, String lastName, String email, BigDecimal cash) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cash = cash;
    }

}
