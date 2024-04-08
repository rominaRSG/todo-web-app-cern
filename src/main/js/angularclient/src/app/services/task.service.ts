import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const taskBaseUrl = 'http://localhost:8080/api/tasks';
const categoryBaseUrl = 'http://localhost:8080/api/categories';

@Injectable()
export class TaskService {

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get(taskBaseUrl);
  }

  get(id) {
    return this.http.get(`${taskBaseUrl}/${id}`);
  }

  create(id, data) {
    return this.http.post(`${categoryBaseUrl}/${id}/tasks`, data);
  }

  update(id, data) {
    return this.http.put(`${taskBaseUrl}/${id}`, data);
  }

  delete(id) {
    return this.http.delete(`${taskBaseUrl}/${id}`);
  }

  deleteAll() {
    return this.http.delete(taskBaseUrl);
  }

  findByName(name) {
    return this.http.get(`${taskBaseUrl}?name=${name}`);
  }
}
