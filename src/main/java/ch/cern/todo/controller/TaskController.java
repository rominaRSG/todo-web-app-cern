package ch.cern.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.exception.ResourceNotFoundException;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;
  @Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/tasks")
	public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) String name) {
		List<Task> tasks = new ArrayList<Task>();

		if (name == null)
			taskRepository.findAll().forEach(tasks::add);
    else
      taskRepository.findByNameContaining(name).forEach(tasks::add);

		if (tasks.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}

	@GetMapping("/tasks/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
		Task taskData = taskRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Not found Task with id = " + id));

    return new ResponseEntity<>(taskData, HttpStatus.OK);
	}

  @PostMapping("/categories/{categoryId}/tasks")
  public ResponseEntity<Task> createTask(@PathVariable(value = "categoryId") Long categoryId,
  @RequestBody Task taskRequest) {
    Task task = categoryRepository.findById(categoryId).map(category -> {
      taskRequest.setCategory(category);
      return taskRepository.save(taskRequest);
    }).orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + categoryId));

    return new ResponseEntity<>(task, HttpStatus.CREATED);
  }

  @GetMapping("/categories/{categoryId}/tasks")
  public ResponseEntity<List<Task>> getAllTasksByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new ResourceNotFoundException("Not found Category with id = " + categoryId);
    }

    List<Task> tasks = taskRepository.findByCategoryId(categoryId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

	@PutMapping("/tasks/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
		Optional<Task> taskData = taskRepository.findById(id);

		if (taskData.isPresent()) {
			Task _task = taskData.get();
			if (task.getName() != null) _task.setName(task.getName());
			if (task.getDescription() != null) _task.setDescription(task.getDescription());
			if (task.getDeadline() != null) _task.setDeadline(task.getDeadline());
			if (task.getCategory() != null) _task.setCategory(task.getCategory());
			return new ResponseEntity<>(taskRepository.save(_task), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
		taskRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tasks")
	public ResponseEntity<HttpStatus> deleteAllTasks() {
		taskRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
