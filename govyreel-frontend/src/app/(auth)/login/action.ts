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

  redirect("/workspace");
}



export async function logoutAction(): Promise<void> {
  try {
    const response = await fetch(`${backendUrl}/api/auth/logout`, {
      method: "GET",
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