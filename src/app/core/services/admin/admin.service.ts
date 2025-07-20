import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../environments/environment.prod';
import { Stats } from '../../models/stats.model'; 
import { Users } from '../../models/users.model';  // adapte le chemin

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly baseUrl = environment.apiUrl.replace(/\/+$/, '') + '/admin';

  constructor(private http: HttpClient) {}

getStats(): Observable<Stats> {
  return this.http.get<Stats>(`${this.baseUrl}/stats`, { withCredentials: true }).pipe(
    catchError(err => {
      console.error('Erreur lors du chargement des stats', err);
      return throwError(() => new Error('Erreur lors du chargement des statistiques'));
    })
  );
}

getUsers(): Observable<Users[]> {
  return this.http.get<Users[]>(`${this.baseUrl}/users`, { withCredentials: true }).pipe(
    catchError(err => {
      console.error('Erreur lors du chargement des utilisateurs', err);
      return throwError(() => new Error('Erreur lors du chargement des utilisateurs'));
    })
  );
}

}
