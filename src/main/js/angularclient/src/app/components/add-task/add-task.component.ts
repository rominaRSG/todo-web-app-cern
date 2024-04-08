import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-add-task',
  templateUrl: './add-task.component.html',
  styleUrls: ['./add-task.component.css']
})
export class AddTaskComponent implements OnInit {
  task = {
    name: '',
    description: '',
    deadline: ''
  };
  currentCategoryId = null;
  currentIndex = -1;
  submitted = false;
  categoriesNotFound = false;
  categories: any;
  loading = true;

  constructor(
    private taskService: TaskService,
    private categoryService: CategoryService) { }

  ngOnInit() {
    this.retrieveCategories();
  }

  retrieveCategories() {
    this.categoryService.getAll()
      .subscribe(
        data => {
          this.categories = data;
          this.currentCategoryId = data ? data[0].id || null : null;
          this.categoriesNotFound = !this.categories;
          this.loading = false;
          console.log(data);
          console.log(this.categoriesNotFound);
        },
        error => {
          console.log(error);
        });
  }

  setActiveCategory(id) {
    this.currentCategoryId = id;
  }

  saveTask() {
    const data = {
      name: this.task.name,
      description: this.task.description,
      deadline: this.task.deadline
    };

    this.taskService.create(this.currentCategoryId, data)
      .subscribe(
        response => {
          console.log(response);
          this.submitted = true;
        },
        error => {
          console.log(error);
        });
  }

  newTask() {
    this.submitted = false;
    this.task = {
      name: '',
      description: '',
      deadline: ''
    };
  }
}
