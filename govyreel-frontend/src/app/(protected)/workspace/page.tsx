import { Button } from "@/components/ui/button";
import { ProjectList } from "@/components/workspace/project/ProjectList";
import { Plus } from "lucide-react";
import { cookies } from "next/headers";
import Link from "next/link";
import { redirect } from "next/navigation";
import { toast } from "sonner";

const backendUrl = process.env.NEXT_PUBLIC_APP_BACKEND_URL;

async function getProjectList() {
  const cookieStore = cookies();
  const accessToken = (await cookieStore).get("accessToken")?.value;

  const res = await fetch(`${backendUrl}/api/project/list`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Cookie": `accessToken=${accessToken}`
    },
    cache: "no-store"
  });
  if (!res.ok) {
    // throw new Error("Failed to fetch data " + res.status);
    if (res.status === 401 || res.status === 403) {
      //TODO: Logout for now, need to be dynamic 
      redirect(`${backendUrl}/api/auth/logout`);
    } else {
      toast.error("Failed to fetch data " + res.status);
    }
  }
  const response = await res.json();
  const { data } = response;
  if (!data) {
    throw new Error("No data found");
  }
  return data;
}

export default async function WorkspacePage() {

  const projects = await getProjectList();
  return (
    <div>
      <div className="w-full py-2">
        <div className="flex item-center justify-between h-16 mx-auto">
          <h1 className="text-2xl font-bold">Project List</h1>
          <div>
            <Button asChild>
              <Link href="/workspace/project/create"><Plus />Create Project</Link>
            </Button>
          </div>
        </div>
      </div>
      <ProjectList projectList={projects} />
    </div>
  )
}