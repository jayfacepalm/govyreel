"use client";

import { User } from "@/types/user";
import React, { createContext, ReactNode, useContext, useState } from "react";

interface UserContextType {
    isLoggedIn: boolean;
    currentUser?: User | null | undefined;
}

const UserContext = createContext<UserContextType | null | undefined>(null);

export function UserProvider({ children, user }: { children: ReactNode, user?: User | null | undefined }) {

    const isLoggedIn = !!user;
    const [currentUser] = useState<User | null | undefined >(user);


    const userContextValue: UserContextType = {
        isLoggedIn: isLoggedIn,
        currentUser: currentUser,
    };

    return (
        <UserContext.Provider value={userContextValue}>
            {children}
        </UserContext.Provider>
    );
}

export function useUser() {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUser must be used within a UserProvider");
    }

    return context;
}