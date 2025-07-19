import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-home-login-card',
  templateUrl: './home-login-card.component.html',
  styleUrls: ['./home-login-card.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class HomeLoginCardComponent {
  @Output() loginSubmit = new EventEmitter<{ email: string; password: string }>();

  loginForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loginSubmit.emit(this.loginForm.value);
    }
  }
}
