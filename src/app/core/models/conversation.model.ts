export interface Conversation {
  id: number;
  nom: string;
  user1Id: number;
  user2Id: number;
  partagee: boolean;
  // messages?: Message[]; // à charger séparément si besoin
}
