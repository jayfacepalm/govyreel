import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { Button } from "@/components/ui/button"
import {
    Field,
    FieldDescription,
    FieldGroup,
    FieldLabel,
    FieldLegend,
    FieldSet
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Plus } from "lucide-react"

export function CreateProject() {
    return (
        <AlertDialog>
            <AlertDialogTrigger>
                <Button><Plus /> Create Project</Button>
            </AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>
                        <div className="flex items-center gap-0.5"><Plus /> Create Project</div>
                    </AlertDialogTitle>
                    <AlertDialogDescription>

                        <FieldSet>
                            <FieldLegend>Project</FieldLegend>
                            <FieldDescription></FieldDescription>
                            <FieldGroup>
                                <Field>
                                    <FieldLabel htmlFor="name">Name</FieldLabel>
                                    <Input id="name" autoComplete="off" />
                                </Field>
                                <Field>
                                    <FieldLabel htmlFor="description">Description</FieldLabel>
                                    <Input id="description" autoComplete="off" />
                                </Field>
                            </FieldGroup>
                        </FieldSet>
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction>Continue</AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}