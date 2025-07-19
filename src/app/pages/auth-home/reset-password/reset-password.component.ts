import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { AuthService } from '../../../core/services/auth/auth.service'; // adapte le chemin
import { SharedInputComponent } from '../../../shared/components/shared-input/shared-input.component';
import { SharedButtonComponent } from '../../../shared/components/shared-button/shared-button.component';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedInputComponent,
    SharedButtonComponent
  ],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  token: string = '';
  loading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get passwordControl(): FormControl {
    return this.resetForm.get('newPassword') as FormControl;
  }

  ngOnInit(): void {
    // Récupérer le token depuis l'URL
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    
      console.log("🔐 Token récupéré depuis l'URL :", this.token);


    if (!this.token) {
      this.errorMessage = "Token de réinitialisation manquant dans l'URL.";
    }
  }

  onSubmit(): void {
    if (this.resetForm.invalid || !this.token) {
      this.resetForm.markAllAsTouched();
      if (!this.token) {
        this.errorMessage = "Token invalide ou manquant.";
      }
      return;
    }

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.authService.resetPassword({
      token: this.token,
      newPassword: this.passwordControl.value
    }).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage = "Mot de passe réinitialisé avec succès. Vous pouvez maintenant vous connecter.";
        // Optionnel : rediriger vers login après un délai
        setTimeout(() => {
          this.router.navigate(['/home-login']);
        }, 3000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error || "Erreur lors de la réinitialisation.";
      }
    });
  }
}
