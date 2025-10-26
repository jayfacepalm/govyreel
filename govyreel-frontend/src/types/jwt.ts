export type JWTPayload = {
  sub: string;
  iat: number;
  exp: number;
  displayName: string;
  roles: string[];
};