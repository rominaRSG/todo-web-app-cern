package ch.cern.todo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolationException;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.CategoryRepository;

@DataJpaTest
public class JPAUnitTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  TaskRepository taskRepository;
  @Autowired
  CategoryRepository categoryRepository;

  @Test
  public void should_find_no_categories_if_repository_is_empty() {
    Iterable categories = categoryRepository.findAll();

    assertThat(categories).isEmpty();
  }

  @Test
  public void should_find_no_tasks_if_repository_is_empty() {
    Iterable tasks = taskRepository.findAll();

    assertThat(tasks).isEmpty();
  }

  @Test
  public void should_store_a_category() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));

    assertThat(category).hasFieldOrPropertyWithValue("name", "category1");
    assertThat(category).hasFieldOrPropertyWithValue("description", "category1desc");
  }

  @Test
  public void should_store_a_category_with_empty_description() {
    Category category = categoryRepository.save(new Category("category1", null));

    assertThat(category).hasFieldOrPropertyWithValue("name", "category1");
    assertThat(category).hasFieldOrPropertyWithValue("description", null);
  }

  @Test
  public void should_store_a_task() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task = taskRepository.save(new Task("task1", "task1desc", "13/10/2023", category));

    assertThat(task).hasFieldOrPropertyWithValue("name", "task1");
    assertThat(task).hasFieldOrPropertyWithValue("description", "task1desc");
    assertThat(task).hasFieldOrPropertyWithValue("deadline", "13/10/2023");
    assertThat(task).hasFieldOrPropertyWithValue("category", category);
  }

  @Test
  public void should_store_a_task_with_empty_description() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task = taskRepository.save(new Task("task1", null, "13/10/2023", category));

    assertThat(task).hasFieldOrPropertyWithValue("name", "task1");
    assertThat(task).hasFieldOrPropertyWithValue("description", null);
    assertThat(task).hasFieldOrPropertyWithValue("deadline", "13/10/2023");
    assertThat(task).hasFieldOrPropertyWithValue("category", category);
  }

  @Test
  public void should_not_store_a_task_with_empty_name() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));

    Assertions.assertThrows(Exception.class, () -> {
      Task task = taskRepository.save(new Task(null, "task1desc", "13/10/2023", category));
      Iterable tasks = taskRepository.findAll();
		});
  }

  @Test
  public void should_not_store_a_task_with_empty_deadline() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));

    Assertions.assertThrows(Exception.class, () -> {
      Task task = taskRepository.save(new Task("task1", "task1desc", null, category));
      Iterable tasks = taskRepository.findAll();
		});
  }

  @Test
  public void should_not_store_a_task_with_empty_category() {
    Assertions.assertThrows(Exception.class, () -> {
      Task task = taskRepository.save(new Task("task1", "task1desc", "13/10/2023", null));
      Iterable tasks = taskRepository.findAll();
		});
  }

  @Test
  public void should_not_store_a_task_with_non_existing_category() {
    Assertions.assertThrows(Exception.class, () -> {
      Category category = new Category("category2", "category2desc");
      Task task = taskRepository.save(new Task("task1", "task1desc", "13/10/2023", category));
      Iterable tasks = taskRepository.findAll();
		});
  }

  @Test
  public void should_not_store_a_category_with_empty_name() {
    Assertions.assertThrows(Exception.class, () -> {
      Category category = categoryRepository.save(new Category(null, "category1desc"));
      Iterable categories = categoryRepository.findAll();
		});
  }

  @Test
  public void should_find_all_tasks() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task1 = new Task("task1", "task1desc", "13/10/2023", category);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category);
    entityManager.persist(task2);

    Task task3 = new Task("task3", "task3desc", "13/10/2023", category);
    entityManager.persist(task3);

    Iterable tasks = taskRepository.findAll();

    assertThat(tasks).hasSize(3).contains(task1, task2, task3);
  }

  @Test
  public void should_find_task_by_id() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task1 = new Task("task1", "task1desc", "13/10/2023", category);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category);
    entityManager.persist(task2);

    Task foundTask = taskRepository.findById(task2.getId()).get();

    assertThat(foundTask).isEqualTo(task2);
  }

  @Test
  public void should_find_tasks_by_categoryId() {
    Category category1 = new Category("category1", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2", "category2desc");
    entityManager.persist(category2);

    Task task1 = new Task("task1", "task1desc", "13/10/2023", category2);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category1);
    entityManager.persist(task2);

    Task task3 = new Task("task3", "task3desc", "13/10/2023", category2);
    entityManager.persist(task3);

    Iterable foundTasks = taskRepository.findByCategoryId(category2.getId());

    assertThat(foundTasks).hasSize(2).contains(task1, task3);
  }

  @Test
  public void should_find_category_by_id() {
    Category category1 = new Category("category1", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2", "category2desc");
    entityManager.persist(category2);

    Category foundCategory = categoryRepository.findById(category2.getId()).get();

    assertThat(foundCategory).isEqualTo(category2);
  }

  @Test
  public void should_find_tasks_by_name_containing_string() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task1 = new Task("task1 example name", "task1desc", "13/10/2023", category);
    entityManager.persist(task1);

    Task task2 = new Task("task2 example name", "task2desc", "13/10/2023", category);
    entityManager.persist(task2);

    Task task3 = new Task("task3", "task3desc", "13/10/2023", category);
    entityManager.persist(task3);

    Iterable tasks = taskRepository.findByNameContaining("example");

    assertThat(tasks).hasSize(2).contains(task1, task2);
  }

  @Test
  public void should_find_categories_by_name_containing_string() {
    Category category1 = new Category("category1 example name", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2 example name", "category2desc");
    entityManager.persist(category2);

    Category category3 = new Category("category3 name", "category3desc");
    entityManager.persist(category3);

    Iterable categories = categoryRepository.findByNameContaining("example");

    assertThat(categories).hasSize(2).contains(category1, category2);
  }

  @Test
  public void should_update_task_by_id() {
    Category category1 = new Category("category1 example name", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2 example name", "category2desc");
    entityManager.persist(category2);

    Task task1 = new Task("task1", "task1desc", "13/10/2023", category1);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category1);
    entityManager.persist(task2);

    Task updatedTask = new Task("updated task2", "updated task2desc", "20/10/2023", category2);

    Task task = taskRepository.findById(task2.getId()).get();
    task.setName(updatedTask.getName());
    task.setDescription(updatedTask.getDescription());
    task.setDeadline(updatedTask.getDeadline());
    task.setCategory(updatedTask.getCategory());
    taskRepository.save(task);

    Task checkTask = taskRepository.findById(task2.getId()).get();

    assertThat(checkTask.getId()).isEqualTo(task2.getId());
    assertThat(checkTask.getName()).isEqualTo(updatedTask.getName());
    assertThat(checkTask.getDescription()).isEqualTo(updatedTask.getDescription());
    assertThat(checkTask.getDeadline()).isEqualTo(updatedTask.getDeadline());
    assertThat(checkTask.getCategory()).isEqualTo(updatedTask.getCategory());
  }

  @Test
  public void should_update_category_by_id() {
    Category category1 = new Category("category1", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2", "category2desc");
    entityManager.persist(category2);

    Category updatedCategory = new Category("updated category2", "updated category2desc");

    Category category = categoryRepository.findById(category2.getId()).get();
    category.setName(updatedCategory.getName());
    category.setDescription(updatedCategory.getDescription());
    categoryRepository.save(category);

    Category checkCategory = categoryRepository.findById(category2.getId()).get();

    assertThat(checkCategory.getId()).isEqualTo(category2.getId());
    assertThat(checkCategory.getName()).isEqualTo(updatedCategory.getName());
    assertThat(checkCategory.getDescription()).isEqualTo(updatedCategory.getDescription());
  }

  @Test
  public void should_delete_task_by_id() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    Task task1 = new Task("task1", "task1desc", "13/10/2023", category);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category);
    entityManager.persist(task2);

    Task task3 = new Task("task3", "task3desc", "13/10/2023", category);
    entityManager.persist(task3);

    taskRepository.deleteById(task2.getId());

    Iterable tasks = taskRepository.findAll();

    assertThat(tasks).hasSize(2).contains(task1, task3);
  }

  @Test
  public void should_delete_category_by_id_and_all_related_tasks() {
    Category category1 = new Category("category1", "category1desc");
    entityManager.persist(category1);

    Category category2 = new Category("category2", "category2desc");
    entityManager.persist(category2);

    Task task1 = new Task("task1", "task1desc", "13/10/2023", category1);
    entityManager.persist(task1);

    Task task2 = new Task("task2", "task2desc", "13/10/2023", category2);
    entityManager.persist(task2);

    Task task3 = new Task("task3", "task3desc", "13/10/2023", category2);
    entityManager.persist(task3);

    categoryRepository.deleteById(category2.getId());

    Iterable categories = categoryRepository.findAll();
    Iterable tasks = taskRepository.findAll();

    assertThat(categories).hasSize(1).contains(category1);
    assertThat(tasks).hasSize(1).contains(task1);
  }

  @Test
  public void should_delete_all_tasks() {
    Category category = categoryRepository.save(new Category("category1", "category1desc"));
    entityManager.persist(new Task("task1", "task1desc", "13/10/2023", category));
    entityManager.persist(new Task("task2", "task2desc", "13/10/2023", category));

    taskRepository.deleteAll();

    assertThat(taskRepository.findAll()).isEmpty();
  }
}