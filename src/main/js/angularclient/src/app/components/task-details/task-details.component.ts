import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.css']
})
export class TaskDetailsComponent implements OnInit {
  currentTask = null;
  message = '';
  currentCategoryId = null;
  categories: any;

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router,
    private categoryService: CategoryService) { }

  ngOnInit() {
    this.message = '';
    this.retrieveCategories();
    this.getTask(this.route.snapshot.paramMap.get('id'));
  }

  retrieveCategories() {
    this.categoryService.getAll()
      .subscribe(
        data => {
          this.categories = data;
        },
        error => {
          console.log(error);
        });
  }

  getTask(id) {
    this.taskService.get(id)
      .subscribe(
        data => {
          this.currentTask = data;
          this.currentCategoryId = this.currentTask.category.id || null;
        },
        error => {
          console.log(error);
        });
  }

  updateTask() {
    this.taskService.update(this.currentTask.id, this.currentTask)
      .subscribe(
        response => {
          console.log(response);
          this.message = 'The task was updated successfully!';
        },
        error => {
          console.log(error);
        });
  }

  setActiveCategory(id) {
    this.currentCategoryId = id;
  }

  deleteTask() {
    this.taskService.delete(this.currentTask.id)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate(['/tasks']);
        },
        error => {
          console.log(error);
        });
  }
}
