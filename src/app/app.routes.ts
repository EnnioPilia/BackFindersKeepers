import { Routes } from '@angular/router';
import { HomeLoginComponent } from './pages/auth-home/home-login/home-login/home-login.component';
import { DashboardComponent } from './pages/dashboard/dashboard/dashboard.component';
import { ResetPasswordComponent } from './pages/auth-home/reset-password/reset-password.component';
import { ForgotPasswordComponent } from './pages/auth-home/forgot-password/forgot-password.component';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home-login', pathMatch: 'full' },
  { path: 'home-login', component: HomeLoginComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [adminGuard] },
  { path: '**', redirectTo: 'home-login' }
];
