
"use client";
import { createProject } from "@/app/(protected)/workspace/project/create/actions"
import { Button } from "@/components/ui/button"
import {
    Field,
    FieldDescription,
    FieldGroup,
    FieldLabel,
    FieldLegend,
    FieldSeparator,
    FieldSet,
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { FormState } from "@/types/formstate";
import Link from "next/link"
import { useActionState } from "react";

export function CreateProjectForm() {
    const initialState: FormState = {
        success: false,
        message: "",
    };

    const wrappedCreateProject = (state: FormState, payload: unknown) => {
        return createProject(payload as FormData);
    };

    const [state, formAction] = useActionState(wrappedCreateProject, initialState);

    return (
        <section>
            <div className="w-full">
                <form action={formAction}>
                    <FieldGroup>
                        <FieldSet>
                            <FieldLegend>Create Your Next Viral Video</FieldLegend>
                            <FieldDescription>
                                Give your project a punchy name and a one-line hook — make it irresistible.
                            </FieldDescription>
                            <FieldGroup>
                                <Field>
                                    <FieldLabel htmlFor="name">
                                        Name
                                    </FieldLabel>
                                    <Input
                                        id="name"
                                        placeholder="Type your project name here."
                                        required
                                        name="name"
                                    />
                                    {state.errors?.name && (
                                        <div className="text-red-500">
                                            {state.errors.name.join(', ')}
                                        </div>
                                    )}
                                </Field>
                                <Field>
                                    <FieldLabel htmlFor="description">
                                        Description
                                    </FieldLabel>
                                    <Textarea id="description" name="description" placeholder="Type your project description here." />
                                    {state.errors?.description && (
                                        <div className="text-red-500">
                                            {state.errors.description.join(', ')}
                                        </div>
                                    )}
                                </Field>
                            </FieldGroup>
                        </FieldSet>
                        <FieldSeparator />
                        <Field orientation="horizontal">
                            <Button type="submit">Submit</Button>
                            <Button asChild variant="outline" type="button">
                                <Link href="/workspace">Cancel</Link>
                            </Button>
                        </Field>
                    </FieldGroup>
                </form>
            </div>
        </section>
    )
}