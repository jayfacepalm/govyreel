"use client";
import { useUser } from "@/contexts/UserContext";
import Link from "next/link";

const backendUrl = process.env.NEXT_PUBLIC_APP_BACKEND_URL;

function AppLogo() {
    return (
        <div className="flex flex-shrink-0 items-center">
            <Link href="/" className="text-2xl font-bold">
                GoVyReel
            </Link>
        </div>
    )
}

const navigationItems = [
    { href: "/workspace", label: "Workspace" },
    { href: `${backendUrl}/api/auth/logout`, label: "Logout" }
];

const loginNavigation = { href: "/login", label: "Login" }

function NavigationItem({ href, label }: { href: string; label: string }) {
    return (
        <Link href={href} className="mx-4">
            {label}
        </Link>
    )
}

export function Header() {

    const { isLoggedIn } = useUser();

    return (
        <header className="w-full bg-white border-b shadow-sm">
            <div className="flex item-center justify-between h-16 px-4 mx-auto">
                <AppLogo />
                <nav className="flex items-center">
                    {!isLoggedIn && (
                        <NavigationItem {...loginNavigation}
                        />
                    )}
                    {isLoggedIn && (
                        navigationItems.map((item) => (
                            <NavigationItem
                                key={item.href}
                                href={item.href}
                                label={item.label}
                            />
                        ))
                    )}
                </nav>
            </div>
        </header>
    )
}