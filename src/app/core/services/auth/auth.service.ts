import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Users } from '../../models/users.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
private readonly baseUrl = environment.apiUrl.replace(/\/+$/, '') + '/auth';
  private currentUser: Users | null = null;

  constructor(private http: HttpClient) {}

  /**
   * Authentifie un utilisateur ou un admin (cookie JWT envoyé en HttpOnly).
   */
login(credentials: { email: string; password: string }): Observable<{ message: string; token: string }> {
  return this.http.post<{ message: string; token: string }>(`${this.baseUrl}/login`, credentials, {
    withCredentials: true
  }).pipe(
    tap(response => {
      // Tu peux stocker le token localement si besoin (ou juste compter sur cookie HttpOnly)
      console.log('Token reçu:', response.token);
      // Pas de check enabled ici, backend ne renvoie pas user complet
    }),
    catchError(err => {
      return throwError(() =>
        new Error(err?.error?.message || "Échec de l'authentification.")
      );
    })
  );
}

  /**
   * Récupère l'utilisateur courant via le cookie JWT.
   */
  // getCurrentUser(): Observable<Users> {
  //   return this.http.get<Users>(`${this.baseUrl}/me`, {
  //     withCredentials: true
  //   }).pipe(
  //     tap(user => {
  //       if (!user.enabled || !user.verifiedAt) {
  //         throw new Error("Email non vérifié.");
  //       }
  //       this.currentUser = user;
  //     }),
  //     catchError(err => {
  //       this.currentUser = null;
  //       return of(null as any);
  //     })
  //   );
  // }

  /**
   * Déconnexion via le backend (supprime le cookie JWT).
   */
  logout(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => this.currentUser = null)
    );
  }

  /**
   * Retourne l'utilisateur stocké localement (optionnel).
   */
  // getUser(): Users | null {
  //   return this.currentUser;
  // }

  /**
   * Demande de réinitialisation de mot de passe (envoie email).
   */
  requestPasswordReset(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password/request`, { email });
  }

  /**
   * Réinitialisation du mot de passe via token reçu par email.
   */
  resetPassword(token: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password/confirm`, {
      token,
      newPassword
    });
  }
}
