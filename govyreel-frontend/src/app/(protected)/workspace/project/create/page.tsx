import { CreateProjectForm } from "@/components/workspace/project/CreateProjectForm";

export default function CreateProjectPage() {
  return (
    <div>
      <div className="w-full py-2">
        <div className="flex item-center justify-between h-16 mx-auto">
          <h1 className="text-2xl font-bold">Create Project</h1>
          <div>

          </div>
        </div>
      </div>
      <CreateProjectForm />
    </div>
  )
}