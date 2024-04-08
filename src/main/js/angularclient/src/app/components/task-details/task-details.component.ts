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
  currentCategory = null;
  currentCategoryId = null;
  categories: any;
  isNameEmpty = false; 
  isDeadlineEmpty = false; 

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
          this.currentCategory = this.currentTask.category || null;
          this.currentCategoryId = this.currentTask.category.id || null;
        },
        error => {
          console.log(error);
        });
  }

  updateTask() {
    if (this.currentTask.name == '') {
      this.isNameEmpty = true;
    }
    if (this.currentTask.deadline == '') {
      this.isDeadlineEmpty = true;
    }
    
    if (this.isNameEmpty || this.isDeadlineEmpty) {
        return;
    }

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

  onNameChange() {
    this.isNameEmpty = false;
  }

  onDeadlineChange() {
    this.isDeadlineEmpty = false;
  }

  onisDeadlineEmptyChange() {
    if (this.currentTask.deadline == '') this.isDeadlineEmpty = true;
  }

  setActiveCategory(index) {
    console.log(444, index, this.categories[index]);
    this.currentTask.category = this.categories[index];
    this.currentCategoryId = this.categories[index].id;
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
