import { Routes } from '@angular/router';
import { HomeLoginComponent } from './pages/auth-home/home-login/home-login/home-login.component';
import { DashboardComponent } from './pages/dashboard/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home-login', pathMatch: 'full' },
  { path: 'home-login', component: HomeLoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'login' }
];
