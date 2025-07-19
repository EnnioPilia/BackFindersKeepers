// import { CanActivateFn, Router } from '@angular/router';
// import { inject } from '@angular/core';
// import { AuthService } from '../../core/services/auth/auth.service';
// import { Observable, of } from 'rxjs';
// import { tap, map, catchError } from 'rxjs/operators';

// export const authGuard: CanActivateFn = (route, state) => {
//   const authService = inject(AuthService);
//   const router = inject(Router);

//   return authService.getCurrentUser().pipe(
//     map(user => {
//       if (user) {
//         return true; // utilisateur connecté, on autorise l’accès
//       } else {
//         router.navigate(['/login']); // redirection vers login si non connecté
//         return false;
//       }
//     }),
//     catchError(() => {
//       router.navigate(['/login']);
//       return of(false);
//     })
//   );
// };
