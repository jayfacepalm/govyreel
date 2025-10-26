import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Download, Save } from "lucide-react";

export default function ProjectPage() {
  return (
    <div>
      <div className="w-full py-2">
        <div className="flex item-center justify-between h-16 mx-auto">
          <h1 className="text-2xl font-bold">Workspace</h1>
          <div className="flex gap-2">
            <Button><Save /> Save</Button>
            <Button><Download /> Export</Button>
          </div>
        </div>
      </div>
      <div>
        <div className="grid grid-flow-col grid-rows-10 gap-2">
          <Card className="row-span-10">01</Card>
          <Card className="col-span-4 row-span-7">02</Card>
          <Card className="col-span-4 row-span-3">03</Card>
        </div>
      </div>
    </div>
  )
} 