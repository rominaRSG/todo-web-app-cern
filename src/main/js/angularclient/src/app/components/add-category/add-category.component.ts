import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent implements OnInit {
  category = {
    name: '',
    description: ''
  };
  submitted = false;

  constructor(private categoryService: CategoryService) { }

  ngOnInit() {
  }

  saveCategory() {
    const data = {
      name: this.category.name,
      description: this.category.description
    };

    this.categoryService.create(data)
      .subscribe(
        response => {
          console.log(response);
          this.submitted = true;
        },
        error => {
          console.log(error);
        });
  }

  newCategory() {
    this.submitted = false;
    this.category = {
      name: '',
      description: ''
    };
  }
}
