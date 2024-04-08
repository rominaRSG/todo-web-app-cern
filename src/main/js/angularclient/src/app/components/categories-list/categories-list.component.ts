import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories-list',
  templateUrl: './categories-list.component.html',
  styleUrls: ['./categories-list.component.css']
})
export class CategoriesListComponent implements OnInit {

  categories: any;
  currentCategory = null;
  currentIndex = -1;
  name = '';

  constructor(
    private categoryService: CategoryService,
    private router: Router) { }

  ngOnInit() {
    this.retrieveCategories();
  }

  retrieveCategories() {
    this.categoryService.getAll()
      .subscribe(
        data => {
          this.categories = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  refreshList() {
    this.retrieveCategories();
    this.currentCategory = null;
    this.currentIndex = -1;
  }

  setActiveCategory(category, index) {
    this.currentCategory = category;
    this.currentIndex = index;
  }

  removeAllCategories() {
    this.categoryService.deleteAll()
      .subscribe(
        response => {
          console.log(response);
          this.retrieveCategories();
        },
        error => {
          console.log(error);
        });
  }

  searchName() {
    this.categoryService.findByName(this.name)
      .subscribe(
        data => {
          this.categories = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  addCategory() {
    this.router.navigate(['/add/category']);
  }
}