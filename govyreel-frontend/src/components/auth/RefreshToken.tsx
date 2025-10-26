"use client";
import { useEffect } from "react";
import { Spinner } from "../ui/spinner";
import { refreshToken } from "@/services/auth-service";
import { useRouter } from "next/navigation";

export function RefreshToken({redirectsTo}: {redirectsTo: string}) {
    const router = useRouter();

    useEffect(() => {
        const handleRefreshToken = async () => {
            await refreshToken().then(()=> {
                router.push(redirectsTo);
            }).catch(()=>{
                router.push("/login");
            });
        };
        handleRefreshToken();
    }, []);

    return (
        <div>
            <Spinner />
        </div>
    )
}