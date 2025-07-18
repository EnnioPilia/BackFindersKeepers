export interface Users {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: string;        // ex: 'USER', 'ADMIN'
  enabled: boolean;
  age?: number;
  dateCreation: string;    // ISO date string
  dateAcceptationCGU?: string;
  verifiedAt?: string;
  actif: boolean;
}
