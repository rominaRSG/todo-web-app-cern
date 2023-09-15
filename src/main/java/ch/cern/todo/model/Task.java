package ch.cern.todo.model;

import javax.persistence.*;
import ch.cern.todo.model.Category;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tasks", indexes = {
  @Index(name = "task_id", columnList = "task_id")
})
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
  @Column(name = "task_id")
  private long id;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Category category;

  @Column(name = "task_name")
  @NotNull(message = "Name is mandatory") @Size(min=2, max=30)
  private String name;

  @Column(name = "task_description")
  private String description;

  @Column(name = "task_deadline")
  @NotNull(message = "Deadline is mandatory")
  private Timestamp deadline;

  public Task() {
    super();
  }

  public Task(String name, String description, String deadline, Category category) {
    this.name = name;
    this.description = description;
    this.deadline = convertStringToTimestamp(deadline);
    this.category = category;
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

  public String getDeadline() {
    Date date = new Date(deadline.getTime());
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    String deadlineString = formatter.format(date);

    return deadlineString;
  }

  public void setDeadline(String deadline) {
    this.deadline = convertStringToTimestamp(deadline);
  }

  public void setCategory(Category category) {
      this.category = category;
  }

  public Category getCategory() {
    return category;
  }

  public static Timestamp convertStringToTimestamp(String strDate) {
    try {
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       // you can change format of date
      Date date = formatter.parse(strDate);
      Timestamp timeStampDate = new Timestamp(date.getTime());

      return timeStampDate;
    } catch (ParseException e) {
      System.out.println("Exception :" + e);
      return null;
    }
  }
}