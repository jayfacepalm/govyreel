"use client";
import { redirect } from "next/navigation";
import { toast } from "sonner";

const backendUrl = process.env.NEXT_PUBLIC_APP_BACKEND_URL;

export async function loginAction(
  data: FormData
): Promise<void> {
   
  const loginRequest = {
    email: data.get("email"),
    password: data.get("password"),
  }

  try {
    const response = await fetch(`${backendUrl}/api/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginRequest),
      credentials: "include",
    });

    if (!response.ok) {
      console.error("Login request failed with status:", response.status);
      toast.error("Login failed. Please check your credentials.");
      return;
    }
  } catch (error) {
    console.error("Login failed:", error);
    return;
  }

  redirect("/dashboard");
}

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
      console.error("Refresh token request failed with status:", response.status);
      toast.error("Refresh token failed. Please log in again.");
      return;
    }
  } catch (error) {
    console.error("Refresh token failed:", error);
    return;
  }
}

export async function logoutAction(): Promise<void> {
  try {
    const response = await fetch(`${backendUrl}/api/auth/logout`, {
      method: "POST",
      credentials: "include",
    });
    if (!response.ok) {
      console.error("Logout request failed with status:", response.status);
      toast.error("Logout failed. Please try again.");
      return;
    }

    toast.success("Logged out successfully.");
    redirect("/login");
  } catch (error) {
    console.error("Logout failed:", error);
    return;
  }
}