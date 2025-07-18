export interface UserObject {
  id: number;
  name: string;
  description: string;
  type: 'PERDU' | 'TROUVE';
  reclame: boolean;
  date: string;           // ISO date string
  localisation: string;
  photoPath?: string;
  ownerId: number;        // ID du propriétaire (User)
  userId?: number;        // ID de l'utilisateur qui a retrouvé ou réclamé ?
}
