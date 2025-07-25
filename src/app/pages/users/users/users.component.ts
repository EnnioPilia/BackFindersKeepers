import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdminService } from '../../../core/services/admin/admin.service';
import { Users } from '../../../core/models/users.model';

type SortableField = 'nom' | 'email' | 'role';

@Component({
  selector: 'app-users',
  standalone: true,
  templateUrl: './users.component.html',
  imports: [CommonModule, FormsModule],
})
export class UsersComponent implements OnInit {
  users: Users[] = [];
  filteredUsers: Users[] = [];

  loadingUsers = false;
  errorUsers: string | null = null;

  searchTerm = '';
  sortField: SortableField = 'nom';
  statusFilter = 'all';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loadingUsers = true;
    this.errorUsers = null;
    this.adminService.getUsers().subscribe({
      next: (data: Users[]) => {
        this.users = data;
        this.applyFilters();
        this.loadingUsers = false;
      },
      error: () => {
        this.errorUsers = 'Erreur lors du chargement des utilisateurs.';
        this.loadingUsers = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.users];

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.trim().toLowerCase();
      filtered = filtered.filter(user =>
        user.nom.toLowerCase().includes(term) ||
        user.email.toLowerCase().includes(term)
      );
    }

    if (this.statusFilter === 'active') {
      filtered = filtered.filter(user => user.actif);
    } else if (this.statusFilter === 'inactive') {
      filtered = filtered.filter(user => !user.actif);
    }

    filtered.sort((a, b) => {
      const aValue = (a[this.sortField] as string)?.toLowerCase() || '';
      const bValue = (b[this.sortField] as string)?.toLowerCase() || '';
      return aValue.localeCompare(bValue);
    });

    this.filteredUsers = filtered;
  }

  toggleUserStatus(user: Users): void {
    const updatedUser = { ...user, actif: !user.actif };
    this.adminService.updateUserStatus(updatedUser.id, { actif: updatedUser.actif }).subscribe({
      next: () => {
        user.actif = updatedUser.actif;
        this.applyFilters();
      },
      error: () => {
        alert('Erreur lors de la mise à jour du statut.');
      }
    });
  }

  deleteUser(id: number): void {
    if (confirm('Confirmer la suppression de cet utilisateur ?')) {
      this.adminService.deleteUser(id).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.id !== id);
          this.applyFilters();
        },
        error: () => {
          alert('Erreur lors de la suppression.');
        }
      });
    }
  }
}
