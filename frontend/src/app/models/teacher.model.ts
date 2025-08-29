/**
 * Teacher model interface for frontend
 */
export interface Teacher {
  id?: number;
  fullName: string;
  dateOfBirth: string;
  numberOfClasses: number;
  age?: number;
}

/**
 * Filter criteria interface for teacher filtering
 */
export interface FilterCriteria {
  searchTerm?: string;
  minAge?: number;
  maxAge?: number;
  minClasses?: number;
  maxClasses?: number;
}

/**
 * Statistics interface for teacher statistics
 */
export interface TeacherStatistics {
  totalTeachers: number;
  averageClasses: number;
}
