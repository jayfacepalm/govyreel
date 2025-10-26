import { Button } from "@/components/ui/button";
import { Card, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Project, ProjectListType } from "@/types/project";
import { Trash } from "lucide-react";
import Link from "next/link";

function ProjectItem({
    id,
    name,
    description,
    lastModifiedDate
}: Project) {
    return (
        <Link href={`/workspace/project/${id}`} className="hover:border-blue-500 rounded">
            <Card>
                <CardHeader>
                    <div className="flex items-center justify-between">
                        <div>
                            <CardTitle>{name}</CardTitle>
                            <CardDescription>{lastModifiedDate}</CardDescription>
                            <CardDescription><p className="truncate">{description}</p></CardDescription>
                        </div>
                        <div>
                            <Button variant={"destructive"}><Trash /></Button>
                        </div>
                    </div>
                </CardHeader>
            </Card>
        </Link>
    )
}

export function ProjectList(projectList: ProjectListType) {
    return (
        <section>
            <div className="grid gap-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-5 xl:grid-cols-5">
                {projectList.projectList.map((project) => (
                    <ProjectItem key={project.id} {...project} />
                ))}
            </div>
        </section>
    )
}