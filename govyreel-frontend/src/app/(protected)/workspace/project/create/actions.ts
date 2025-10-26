"use server";
import { FormState } from "@/types/formstate";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import z from "zod";

const backendUrl = process.env.NEXT_PUBLIC_APP_BACKEND_URL;

const CreateProjectSchema = z.object({
  name: z.string().min(1, { message: "Name is required" }),
  description: z.string().min(1, { message: "Description is required" }),
});

// This function only runs on the server
export async function createProject(formData: FormData): Promise<FormState> {
  console.log(formData)
  const rawFormData = Object.fromEntries(formData.entries());
  const validatedFields = CreateProjectSchema.safeParse(rawFormData);
  if (!validatedFields.success) {
    return {
      success: false,
      message: "Validation Failed.",
      errors: validatedFields.error.flatten().fieldErrors,
    };
  }

  const cookieStore = cookies();
  const accessToken = (await cookieStore).get("accessToken")?.value;

  const createProjectRequest = {
    name: formData.get("name"),
    description: formData.get("description"),
  };

  console.log(createProjectRequest);

  const res = await fetch(`${backendUrl}/api/project/create`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Cookie: `accessToken=${accessToken}`,
    },
    body: JSON.stringify(createProjectRequest),
  });

  if (!res.ok) {
    if (res.status === 401 || res.status === 403) {
      //TODO: Logout for now, need to be dynamic
      redirect(`${backendUrl}/api/auth/logout`);
    } else {
      console.log("Failed to fetch data " + res.status);
    }
  }

  redirect("/workspace");
}
