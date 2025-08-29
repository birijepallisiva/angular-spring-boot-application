import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TeacherService } from '../../services/teacher.service';
import { TeacherStatistics } from '../../models/teacher.model';

/**
 * Home component - Landing page with navigation options and statistics
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  statistics: TeacherStatistics | null = null;
  loading = false;

  constructor(private teacherService: TeacherService) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  /**
   * Load teacher statistics
   */
  loadStatistics(): void {
    this.loading = true;
    this.teacherService.getStatistics().subscribe({
      next: (stats) => {
        this.statistics = stats;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading statistics:', error);
        this.loading = false;
      }
    });
  }

  /**
   * Export data to PDF
   */
  exportToPDF(): void {
    this.teacherService.exportToPDF().subscribe({
      next: (blob) => {
        this.teacherService.downloadFile(blob, 'teachers.pdf');
      },
      error: (error) => {
        console.error('Error exporting to PDF:', error);
      }
    });
  }

  /**
   * Export data to Excel
   */
  exportToExcel(): void {
    this.teacherService.exportToExcel().subscribe({
      next: (blob) => {
        this.teacherService.downloadFile(blob, 'teachers.xlsx');
      },
      error: (error) => {
        console.error('Error exporting to Excel:', error);
      }
    });
  }
}
