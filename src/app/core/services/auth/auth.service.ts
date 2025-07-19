import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Users } from '../../models/users.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly baseUrl = `${environment.apiUrl}/auth`;
  private currentUser: Users | null = null;

  constructor(private http: HttpClient) {}

  /**
   * Authentifie l'utilisateur, pose le cookie HttpOnly via le backend.
   */
  login(credentials: { email: string; password: string }): Observable<Users> {
    return this.http.post<Users>(`${this.baseUrl}/login`, credentials, {
      withCredentials: true
    }).pipe(
      tap(user => {
        // Vérification si l'email est validé
        if (!user.enabled || !user.verifiedAt) {
          throw new Error("Votre compte n'est pas encore vérifié.");
        }
        this.currentUser = user;
      }),
      catchError(err => {
        this.currentUser = null;
        return throwError(() =>
          new Error(err?.error?.message || "Échec de l'authentification.")
        );
      })
    );
  }

  /**
   * Récupère l'utilisateur courant via le cookie (si valide).
   */
  getCurrentUser(): Observable<Users> {
    return this.http.get<Users>(`${this.baseUrl}/me`, {
      withCredentials: true
    }).pipe(
      tap(user => {
        if (!user.enabled || !user.verifiedAt) {
          throw new Error("Email non vérifié.");
        }
        this.currentUser = user;
      }),
      catchError(err => {
        this.currentUser = null;
        return of(null as any);
      })
    );
  }

  /**
   * Déconnecte (le backend supprime le cookie JWT).
   */
  logout(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => this.currentUser = null)
    );
  }

  /**
   * Retourne l'utilisateur mémorisé localement.
   */
  getUser(): Users | null {
    return this.currentUser;
  }

  /**
   * Demande de réinitialisation de mot de passe (email obligatoire).
   */
  requestPasswordReset(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password/request`, { email });
  }

  /**
   * Réinitialisation avec token (par email).
   */
  resetPassword(token: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password/confirm`, {
      token,
      newPassword
    });
  }
}
