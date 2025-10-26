export type FormState = {
  success: boolean;
  message: string;
  errors?: {
    name?: string[];
    description?: string[];
  }
}