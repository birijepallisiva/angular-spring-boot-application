import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TeacherService } from '../../services/teacher.service';
import { Teacher } from '../../models/teacher.model';

/**
 * Component for adding/editing teacher records
 */
@Component({
  selector: 'app-teacher-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule
  ],
  templateUrl: './teacher-form.component.html',
  styleUrls: ['./teacher-form.component.css']
})
export class TeacherFormComponent implements OnInit {
  teacherForm: FormGroup;
  isEditMode = false;
  teacherId: number | null = null;
  loading = false;
  maxDate = new Date(); // Prevent future dates

  constructor(
    private fb: FormBuilder,
    private teacherService: TeacherService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.teacherForm = this.createForm();
  }

  ngOnInit(): void {
    // Check if we're in edit mode
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.teacherId = +id;
      this.loadTeacher(this.teacherId);
    }
  }

  /**
   * Create the reactive form
   */
  private createForm(): FormGroup {
    return this.fb.group({
      fullName: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(100),
        Validators.pattern(/^[a-zA-Z\s]+$/)
      ]],
      dateOfBirth: ['', [
        Validators.required,
        this.pastDateValidator
      ]],
      numberOfClasses: ['', [
        Validators.required,
        Validators.min(1),
        Validators.max(50),
        Validators.pattern(/^\d+$/)
      ]]
    });
  }

  /**
   * Custom validator for past dates
   */
  private pastDateValidator(control: any) {
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate >= today) {
      return { futureDate: true };
    }
    return null;
  }

  /**
   * Load teacher data for editing
   */
  private loadTeacher(id: number): void {
    this.loading = true;
    this.teacherService.getTeacherById(id).subscribe({
      next: (teacher) => {
        this.teacherForm.patchValue({
          fullName: teacher.fullName,
          dateOfBirth: new Date(teacher.dateOfBirth),
          numberOfClasses: teacher.numberOfClasses
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading teacher:', error);
        this.showMessage('Error loading teacher data', 'error');
        this.loading = false;
        this.router.navigate(['/teachers']);
      }
    });
  }

  /**
   * Submit the form
   */
  onSubmit(): void {
    if (this.teacherForm.valid) {
      this.loading = true;
      const formValue = this.teacherForm.value;
      
      // Format the date properly
      const teacher: Teacher = {
        fullName: formValue.fullName.trim(),
        dateOfBirth: this.formatDate(formValue.dateOfBirth),
        numberOfClasses: +formValue.numberOfClasses
      };

      if (this.isEditMode && this.teacherId) {
        // Update existing teacher
        this.teacherService.updateTeacher(this.teacherId, teacher).subscribe({
          next: (updatedTeacher) => {
            this.showMessage('Teacher updated successfully!', 'success');
            this.router.navigate(['/teachers']);
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating teacher:', error);
            this.showMessage('Error updating teacher', 'error');
            this.loading = false;
          }
        });
      } else {
        // Create new teacher
        this.teacherService.createTeacher(teacher).subscribe({
          next: (newTeacher) => {
            this.showMessage('Teacher added successfully!', 'success');
            this.router.navigate(['/teachers']);
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating teacher:', error);
            this.showMessage('Error creating teacher', 'error');
            this.loading = false;
          }
        });
      }
    } else {
      this.markFormGroupTouched();
      this.showMessage('Please correct the errors in the form', 'error');
    }
  }

  /**
   * Format date for API
   */
  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  /**
   * Mark all form controls as touched to show validation errors
   */
  private markFormGroupTouched(): void {
    Object.keys(this.teacherForm.controls).forEach(key => {
      const control = this.teacherForm.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Get error message for form field
   */
  getErrorMessage(fieldName: string): string {
    const control = this.teacherForm.get(fieldName);
    
    if (control?.hasError('required')) {
      return `${this.getFieldDisplayName(fieldName)} is required`;
    }
    
    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength'].requiredLength;
      return `${this.getFieldDisplayName(fieldName)} must be at least ${minLength} characters`;
    }
    
    if (control?.hasError('maxlength')) {
      const maxLength = control.errors?.['maxlength'].requiredLength;
      return `${this.getFieldDisplayName(fieldName)} cannot exceed ${maxLength} characters`;
    }
    
    if (control?.hasError('min')) {
      const min = control.errors?.['min'].min;
      return `${this.getFieldDisplayName(fieldName)} must be at least ${min}`;
    }
    
    if (control?.hasError('max')) {
      const max = control.errors?.['max'].max;
      return `${this.getFieldDisplayName(fieldName)} cannot exceed ${max}`;
    }
    
    if (control?.hasError('pattern')) {
      if (fieldName === 'fullName') {
        return 'Full name can only contain letters and spaces';
      }
      if (fieldName === 'numberOfClasses') {
        return 'Number of classes must be a valid number';
      }
    }
    
    if (control?.hasError('futureDate')) {
      return 'Date of birth must be in the past';
    }
    
    return '';
  }

  /**
   * Get display name for form field
   */
  private getFieldDisplayName(fieldName: string): string {
    const displayNames: { [key: string]: string } = {
      'fullName': 'Full Name',
      'dateOfBirth': 'Date of Birth',
      'numberOfClasses': 'Number of Classes'
    };
    return displayNames[fieldName] || fieldName;
  }

  /**
   * Check if field has error
   */
  hasError(fieldName: string): boolean {
    const control = this.teacherForm.get(fieldName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  /**
   * Cancel form and navigate back
   */
  onCancel(): void {
    this.router.navigate(['/teachers']);
  }

  /**
   * Reset form
   */
  onReset(): void {
    this.teacherForm.reset();
    this.markFormGroupTouched();
  }

  /**
   * Show message using snack bar
   */
  private showMessage(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      panelClass: type === 'success' ? 'success-snackbar' : 'error-snackbar'
    });
  }
}
