package uj.wmii.jwzp.hardwarerent.data;

import lombok.Getter;
import lombok.Setter;

public class RegistrationForm {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private String email;

    public RegistrationForm(String username,
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationForm that = (RegistrationForm) o;

        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "RegistrationForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public MyUser toUserWithoutRoles() {
        return new MyUser(
                username,
                password,
                firstName,
                lastName,
                email);
    }
}
