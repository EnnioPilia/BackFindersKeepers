export interface Message {
  id: number;
  contenu: string;
  conversationId: number;
  senderId: number;
  dateEnvoi: string;      // ISO date string
}
