package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerental.data.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
