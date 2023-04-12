package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.wmii.jwzp.hardwarerent.data.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByUsername(String username);
}
