package ch.cern.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.exception.ResourceNotFoundException;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("/categories")
	public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String name) {
		List<Category> categories = new ArrayList<Category>();

		if (name == null)
			categoryRepository.findAll().forEach(categories::add);
    else
      categoryRepository.findByNameContaining(name).forEach(categories::add);

		if (categories.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) {
		Category categoryData = categoryRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));

    return new ResponseEntity<>(categoryData, HttpStatus.OK);
	}

	@PostMapping("/categories")
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    Set<Task> tasks = new HashSet<Task>(0);
		Category _category = categoryRepository
				.save(new Category(category.getName(), category.getDescription()));
		return new ResponseEntity<>(_category, HttpStatus.CREATED);
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable("id") long id, @RequestBody Category category) {
		Optional<Category> categoryData = categoryRepository.findById(id);

		if (categoryData.isPresent()) {
			Category _category = categoryData.get();
      if (category.getName() != null) _category.setName(category.getName());
			if (category.getDescription() != null) _category.setDescription(category.getDescription());
			return new ResponseEntity<>(categoryRepository.save(_category), HttpStatus.OK);
		} else {
      throw new ResourceNotFoundException("Not found Category with id = " + id);
		}
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") long id) {
		categoryRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/categories")
	public ResponseEntity<HttpStatus> deleteAllCategories() {
		categoryRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
