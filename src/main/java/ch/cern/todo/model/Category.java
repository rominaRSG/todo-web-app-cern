package ch.cern.todo.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.cern.todo.model.Task;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories", indexes = {
  @Index(name = "category_id", columnList = "category_id"),
  @Index(name = "category_name", columnList = "category_name")
})
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
  @Column(name = "category_id")
  private long id;

  @Column(name = "category_name", unique = true)
  @NotNull(message = "Name is mandatory") @Size(min=2, max=30)
  private String name;

  @Column(name = "category_description")
  private String description;

  public Category() {
    super();
  }

  public Category(String name, String description) {
    super();
    this.name = name;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}