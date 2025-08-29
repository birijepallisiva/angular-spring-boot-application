import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Teacher, FilterCriteria, TeacherStatistics } from '../models/teacher.model';

/**
 * Service for handling teacher-related HTTP operations
 */
@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private apiUrl = 'http://localhost:8000/api/teachers';
  
  // Subject to emit teacher list updates
  private teachersSubject = new BehaviorSubject<Teacher[]>([]);
  public teachers$ = this.teachersSubject.asObservable();
  
  // Loading state
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadTeachers();
  }

  /**
   * Load all teachers
   */
  loadTeachers(): void {
    this.loadingSubject.next(true);
    this.http.get<Teacher[]>(this.apiUrl).pipe(
      tap(teachers => {
        this.teachersSubject.next(teachers);
        this.loadingSubject.next(false);
      })
    ).subscribe({
      error: (error) => {
        console.error('Error loading teachers:', error);
        this.loadingSubject.next(false);
      }
    });
  }

  /**
   * Get all teachers
   */
  getAllTeachers(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(this.apiUrl);
  }

  /**
   * Get teacher by ID
   */
  getTeacherById(id: number): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create a new teacher
   */
  createTeacher(teacher: Teacher): Observable<Teacher> {
    return this.http.post<Teacher>(this.apiUrl, teacher).pipe(
      tap(() => this.loadTeachers())
    );
  }

  /**
   * Update an existing teacher
   */
  updateTeacher(id: number, teacher: Teacher): Observable<Teacher> {
    return this.http.put<Teacher>(`${this.apiUrl}/${id}`, teacher).pipe(
      tap(() => this.loadTeachers())
    );
  }

  /**
   * Delete a teacher
   */
  deleteTeacher(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.loadTeachers())
    );
  }

  /**
   * Search teachers by name
   */
  searchTeachers(query: string): Observable<Teacher[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<Teacher[]>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Filter teachers by criteria
   */
  filterTeachers(criteria: FilterCriteria): Observable<Teacher[]> {
    return this.http.post<Teacher[]>(`${this.apiUrl}/filter`, criteria);
  }

  /**
   * Get teachers by age range
   */
  getTeachersByAge(minAge: number, maxAge: number): Observable<Teacher[]> {
    const params = new HttpParams()
      .set('minAge', minAge.toString())
      .set('maxAge', maxAge.toString());
    return this.http.get<Teacher[]>(`${this.apiUrl}/filter/age`, { params });
  }

  /**
   * Get teachers by classes range
   */
  getTeachersByClasses(minClasses: number, maxClasses: number): Observable<Teacher[]> {
    const params = new HttpParams()
      .set('minClasses', minClasses.toString())
      .set('maxClasses', maxClasses.toString());
    return this.http.get<Teacher[]>(`${this.apiUrl}/filter/classes`, { params });
  }

  /**
   * Get teacher statistics
   */
  getStatistics(): Observable<TeacherStatistics> {
    return this.http.get<TeacherStatistics>(`${this.apiUrl}/statistics`);
  }

  /**
   * Export teachers to PDF
   */
  exportToPDF(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/pdf`, { responseType: 'blob' });
  }

  /**
   * Export teachers to Excel
   */
  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/excel`, { responseType: 'blob' });
  }

  /**
   * Download file helper
   */
  downloadFile(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
