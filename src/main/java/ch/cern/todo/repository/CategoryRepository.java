package ch.cern.todo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ch.cern.todo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByNameContaining(String name);
}
