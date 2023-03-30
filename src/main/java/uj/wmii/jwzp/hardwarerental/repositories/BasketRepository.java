package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.wmii.jwzp.hardwarerental.data.Basket;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query(value = "SELECT COUNT(B.id) FROM Basket B")
    Long lastBasket();

}
