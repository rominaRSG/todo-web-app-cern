import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-category-details',
  templateUrl: './category-details.component.html',
  styleUrls: ['./category-details.component.css']
})
export class CategoryDetailsComponent implements OnInit {
  currentCategory = null;
  message = '';

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.message = '';
    this.getCategory(this.route.snapshot.paramMap.get('id'));
  }

  getCategory(id) {
    this.categoryService.get(id)
      .subscribe(
        data => {
          this.currentCategory = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  updatePublished(status) {
    const data = {
      name: this.currentCategory.name,
      description: this.currentCategory.description,
      published: status
    };

    this.categoryService.update(this.currentCategory.id, data)
      .subscribe(
        response => {
          this.currentCategory.published = status;
          console.log(response);
        },
        error => {
          console.log(error);
        });
  }

  updateCategory() {
    this.categoryService.update(this.currentCategory.id, this.currentCategory)
      .subscribe(
        response => {
          console.log(response);
          this.message = 'The category was updated successfully!';
        },
        error => {
          console.log(error);
        });
  }

  deleteCategory() {
    this.categoryService.delete(this.currentCategory.id)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate(['/categorys']);
        },
        error => {
          console.log(error);
        });
  }
}
