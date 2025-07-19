import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HomeLoginCardComponent } from '../home-login-components/home-login-card.component';
import { AuthService } from '../../../../core/services/auth/auth.service';

@Component({
  selector: 'app-home-login',
  templateUrl: './home-login.component.html',
  styleUrls: ['./home-login.component.scss'],
  standalone: true,
  imports: [HomeLoginCardComponent]
})
export class HomeLoginComponent {
  errorMessage: string | null = null;
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(credentials: { email: string; password: string }) {
    this.errorMessage = null;
    this.loading = true;
    this.authService.login(credentials).subscribe({
      next: (user) => {
        this.loading = false;
        if (!user.enabled) {
          this.errorMessage = "Votre compte n'est pas activé. Veuillez vérifier votre email.";
          return;
        }
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 401) {
          this.errorMessage = 'Email ou mot de passe incorrect.';
        } else {
          this.errorMessage = 'Erreur serveur, veuillez réessayer plus tard.';
        }
      }
    });
  }
}
