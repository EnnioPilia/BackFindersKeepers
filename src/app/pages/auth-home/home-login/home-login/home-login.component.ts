import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HomeLoginCardComponent } from '../home-login-components/home-login-card.component';
import { AuthService } from '../../../../core/services/auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-login',
  templateUrl: './home-login.component.html',
  styleUrls: ['./home-login.component.scss'],
  standalone: true,
  imports: [HomeLoginCardComponent,CommonModule]
})
export class HomeLoginComponent {
  errorMessage: string | null = null;
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}
onLogin(credentials: { email: string; password: string }) {
  this.errorMessage = null;
  this.loading = true;

  this.authService.login(credentials).subscribe({
    next: (response) => {
      this.loading = false;
      // Ici tu n'as pas user.enabled, donc pas de check
      // Tu peux juste rediriger directement
      this.router.navigate(['/dashboard']);
    },
    error: (err) => {
      this.loading = false;
      this.errorMessage = err.message || 'Erreur serveur, veuillez réessayer plus tard.';
    }
  });
}


}
