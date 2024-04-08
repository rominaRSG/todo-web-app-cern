import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TasksListComponent } from './components/tasks-list/tasks-list.component';
import { TaskDetailsComponent } from './components/task-details/task-details.component';
import { AddTaskComponent } from './components/add-task/add-task.component';
import { CategoriesListComponent } from './components/categories-list/categories-list.component';
import { CategoryDetailsComponent } from './components/category-details/category-details.component';
import { AddCategoryComponent } from './components/add-category/add-category.component';

const routes: Routes = [
  { path: '', redirectTo: 'tasks', pathMatch: 'full' },
  { path: 'tasks', component: TasksListComponent },
  { path: 'tasks/:id', component: TaskDetailsComponent },
  { path: 'add/task', component: AddTaskComponent },
  { path: 'categories', component: CategoriesListComponent },
  { path: 'categories/:id', component: CategoryDetailsComponent },
  { path: 'add/category', component: AddCategoryComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
