import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../core/services/admin/admin.service';
import { Stats } from '../../../core/models/stats.model';
import { Users } from '../../../core/models/users.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule],
})
export class DashboardComponent implements OnInit {

  stats: Stats | null = null;
  users: Users[] = [];

  loadingStats = false;
  loadingUsers = false;

  errorStats: string | null = null;
  errorUsers: string | null = null;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadStats();
    this.loadUsers();
  }

  loadStats(): void {
    this.loadingStats = true;
    this.adminService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loadingStats = false;
      },
      error: () => {
        this.errorStats = 'Erreur lors du chargement des statistiques';
        this.loadingStats = false;
      }
    });
  }
  loadUsers() {
    this.loadingUsers = true;
    this.errorUsers = null;
    this.adminService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.loadingUsers = false;
      },
      error: (err) => {
        this.errorUsers = 'Erreur lors du chargement des utilisateurs';
        this.loadingUsers = false;
      }
    });
  }
}

