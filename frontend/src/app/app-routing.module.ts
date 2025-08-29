import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { TeacherListComponent } from './components/teacher-list/teacher-list.component';
import { TeacherFormComponent } from './components/teacher-form/teacher-form.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'teachers', component: TeacherListComponent },
  { path: 'add-teacher', component: TeacherFormComponent },
  { path: 'edit-teacher/:id', component: TeacherFormComponent },
  { path: '**', redirectTo: '/home' }
];
