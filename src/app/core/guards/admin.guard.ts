import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('authToken');

  if (token) {
    try {
      const decodedToken: any = jwtDecode(token);
      const userRole = decodedToken.role; // Assuming 'role' is the key in your JWT payload

      if (userRole === 'ROLE_ADMIN') {
        return true;
      } else {
        // Redirect to a forbidden page or login if not admin
        router.navigate(['/login']);
        return false;
      }
    } catch (error) {
      console.error('Error decoding token:', error);
      router.navigate(['/login']);
      return false;
    }
  } else {
    // No token found, redirect to login
    router.navigate(['/login']);
    return false;
  }
};