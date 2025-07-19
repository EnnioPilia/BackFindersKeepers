import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { AuthService } from '../../../core/services/auth/auth.service'; // adapte le chemin
import { SharedInputComponent } from '../../../shared/components/shared-input/shared-input.component';
import { SharedButtonComponent } from '../../../shared/components/shared-button/shared-button.component';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedInputComponent,
    SharedButtonComponent
  ],
})
export class ForgotPasswordComponent implements OnInit {
  forgotForm: FormGroup;
  loading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  get emailControl(): FormControl {
    return this.forgotForm.get('email') as FormControl;
  }

  ngOnInit(): void { }

  onSubmit(): void {
    if (this.forgotForm.invalid) {
      this.forgotForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.authService.requestPasswordReset(this.emailControl.value).subscribe({
      next: () => {
            this.successMessage = 'Un lien vous a été envoyé sur votre boite mail';

        // Redirection après 3 secondes ca marche pas 
        setTimeout(() => {
          console.log("⏳ Redirection vers /login en cours...");
          this.router.navigate(['/home-login']);
        }, 3000);

        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error || "Erreur lors de la demande.";
      }
    });
  }
}
