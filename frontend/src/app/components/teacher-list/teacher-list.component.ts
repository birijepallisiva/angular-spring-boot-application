import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TeacherService } from '../../services/teacher.service';
import { Teacher, FilterCriteria } from '../../models/teacher.model';

/**
 * Component for displaying and managing the list of teachers
 */
@Component({
  selector: 'app-teacher-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './teacher-list.component.html',
  styleUrls: ['./teacher-list.component.css']
})
export class TeacherListComponent implements OnInit {
  teachers: Teacher[] = [];
  filteredTeachers: Teacher[] = [];
  loading = false;
  
  // Search and filter properties
  searchTerm = '';
  minAge: number | null = null;
  maxAge: number | null = null;
  minClasses: number | null = null;
  maxClasses: number | null = null;
  
  // Table columns
  displayedColumns: string[] = ['id', 'fullName', 'age', 'dateOfBirth', 'numberOfClasses', 'actions'];

  constructor(
    private teacherService: TeacherService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadTeachers();
  }

  /**
   * Load all teachers
   */
  loadTeachers(): void {
    this.loading = true;
    this.teacherService.getAllTeachers().subscribe({
      next: (teachers) => {
        this.teachers = teachers;
        this.filteredTeachers = teachers;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading teachers:', error);
        this.showMessage('Error loading teachers', 'error');
        this.loading = false;
      }
    });
  }

  /**
   * Search teachers by name
   */
  onSearch(): void {
    if (this.searchTerm.trim()) {
      this.teacherService.searchTeachers(this.searchTerm).subscribe({
        next: (teachers) => {
          this.filteredTeachers = teachers;
        },
        error: (error) => {
          console.error('Error searching teachers:', error);
          this.showMessage('Error searching teachers', 'error');
        }
      });
    } else {
      this.filteredTeachers = this.teachers;
    }
  }

  /**
   * Apply filters
   */
  applyFilters(): void {
    const criteria: FilterCriteria = {
      searchTerm: this.searchTerm || undefined,
      minAge: this.minAge || undefined,
      maxAge: this.maxAge || undefined,
      minClasses: this.minClasses || undefined,
      maxClasses: this.maxClasses || undefined
    };

    this.teacherService.filterTeachers(criteria).subscribe({
      next: (teachers) => {
        this.filteredTeachers = teachers;
        this.showMessage(`Found ${teachers.length} teachers`, 'success');
      },
      error: (error) => {
        console.error('Error filtering teachers:', error);
        this.showMessage('Error filtering teachers', 'error');
      }
    });
  }

  /**
   * Clear all filters
   */
  clearFilters(): void {
    this.searchTerm = '';
    this.minAge = null;
    this.maxAge = null;
    this.minClasses = null;
    this.maxClasses = null;
    this.filteredTeachers = this.teachers;
    this.showMessage('Filters cleared', 'success');
  }

  /**
   * Delete a teacher
   */
  deleteTeacher(id: number): void {
    if (confirm('Are you sure you want to delete this teacher?')) {
      this.teacherService.deleteTeacher(id).subscribe({
        next: () => {
          this.loadTeachers();
          this.showMessage('Teacher deleted successfully', 'success');
        },
        error: (error) => {
          console.error('Error deleting teacher:', error);
          this.showMessage('Error deleting teacher', 'error');
        }
      });
    }
  }

  /**
   * Export to PDF
   */
  exportToPDF(): void {
    this.teacherService.exportToPDF().subscribe({
      next: (blob) => {
        this.teacherService.downloadFile(blob, 'teachers.pdf');
        this.showMessage('PDF exported successfully', 'success');
      },
      error: (error) => {
        console.error('Error exporting to PDF:', error);
        this.showMessage('Error exporting to PDF', 'error');
      }
    });
  }

  /**
   * Export to Excel
   */
  exportToExcel(): void {
    this.teacherService.exportToExcel().subscribe({
      next: (blob) => {
        this.teacherService.downloadFile(blob, 'teachers.xlsx');
        this.showMessage('Excel exported successfully', 'success');
      },
      error: (error) => {
        console.error('Error exporting to Excel:', error);
        this.showMessage('Error exporting to Excel', 'error');
      }
    });
  }

  /**
   * Show message using snack bar
   */
  private showMessage(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: type === 'success' ? 'success-snackbar' : 'error-snackbar'
    });
  }
}
