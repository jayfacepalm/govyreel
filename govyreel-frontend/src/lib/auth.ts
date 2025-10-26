"use server";
import { JWTPayload } from "@/types/jwt";
import { User } from "@/types/user";
import { jwtVerify } from "jose";

const backendUrl = process.env.NEXT_PUBLIC_APP_BACKEND_URL;
export async function refreshTokenServerAction(): Promise<void> {
  try {
    const response = await fetch(`${backendUrl}/api/auth/refresh-token`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });
    if (!response.ok) {
      throw new Error(
        "Refresh token request failed with status:" + response.status
      );
      return;
    }
  } catch (error) {
    throw new Error("Refresh token request failed");
  }
}

export async function getJwtSecret(): Promise<Uint8Array> {
  const secret = process.env.JWT_SECRET_KEY;
  if (!secret) {
    throw new Error("JWT_SECRET_KEY environment variable is not set");
  }
  return new TextEncoder().encode(secret);
}

export async function getUserFromJwtPayload(
  payload: JWTPayload
): Promise<User> {
  return {
    id: payload.sub,
    displayName: payload.displayName,
    roles: payload.roles,
  };
}

export async function getUserFromToken(token: string): Promise<User | null> {
  try {
    if (!token || typeof token !== "string") {
      return null;
    }

    const jwtSecret = await getJwtSecret();
    const { payload } = await jwtVerify(token, jwtSecret);
    return await getUserFromJwtPayload(payload as JWTPayload);
  } catch (error) {
    console.error("Error verifying JWT token:", error);
    return null;
  }
}
