import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Users } from '../../models/users.model';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../../environments/environment.prod';

interface PasswordResetRequestDto {
  email: string;
}

interface PasswordResetDto {
  token: string;
  newPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = environment.apiUrl.replace(/\/+$/, '') + '/auth';
  private currentUser: Users | null = null;

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<{ message: string; token: string }> {
    return this.http.post<{ message: string; token: string }>(`${this.baseUrl}/login`, credentials, {
      withCredentials: true
    }).pipe(
      catchError(err => {
        return throwError(() => new Error(err?.error?.message || "Échec de l'authentification."));
      })
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => this.currentUser = null)
    );
  }

  requestPasswordReset(email: string): Observable<string> {
    const body: PasswordResetRequestDto = { email };
    return this.http.post<string>(`${this.baseUrl}/request-reset`, body);
  }

  resetPassword(data: PasswordResetDto): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/reset-password`, data);
  }
  
}
