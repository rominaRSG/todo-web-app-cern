import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tasks-list',
  templateUrl: './tasks-list.component.html',
  styleUrls: ['./tasks-list.component.css']
})
export class TasksListComponent implements OnInit {

  tasks: any;
  currentTask = null;
  currentIndex = -1;
  name = '';

  constructor(
    private taskService: TaskService,
    private router: Router) { }

  ngOnInit() {
    this.retrieveTasks();
  }

  retrieveTasks() {
    this.taskService.getAll()
      .subscribe(
        data => {
          this.tasks = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  refreshList() {
    this.retrieveTasks();
    this.currentTask = null;
    this.currentIndex = -1;
  }

  setActiveTask(task, index) {
    this.currentTask = task;
    this.currentIndex = index;
  }

  removeAllTasks() {
    this.taskService.deleteAll()
      .subscribe(
        response => {
          console.log(response);
          this.retrieveTasks();
        },
        error => {
          console.log(error);
        });
  }

  searchName() {
    this.taskService.findByName(this.name)
      .subscribe(
        data => {
          this.tasks = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  addTask() {
    this.router.navigate(['/add/task']);
  }
}
