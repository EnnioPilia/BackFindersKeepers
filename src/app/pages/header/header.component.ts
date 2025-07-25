import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedButtonComponent } from '../../shared/components/shared-button/shared-button.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    SharedButtonComponent,
    RouterModule
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  constructor(private router: Router) { }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(['/']);
  }

  isActive(route: string): boolean {
    return this.router.url.includes(route);
  }
}
