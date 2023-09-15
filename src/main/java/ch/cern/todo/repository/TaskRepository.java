package ch.cern.todo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ch.cern.todo.model.Task;
import javax.transaction.Transactional;
//import javax.persistence.*;
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByCategoryId(Long categoryId);

  List<Task> findByNameContaining(String name);
}
