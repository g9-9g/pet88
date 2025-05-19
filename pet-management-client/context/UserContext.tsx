"use client";

import React, {
  createContext,
  useContext,
  useState,
  ReactNode,
  useEffect,
} from "react";
import { authUtils, UserData } from "@/lib/utils/auth";
import { UserRole } from "@/lib/utils/role";

export interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
}

interface UserContextType {
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>;
  isLoading: boolean;
  logout: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
  children: ReactNode;
}

export const UserProvider = ({ children }: UserProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const userData = authUtils.getUserData();
    if (userData) {
      setUser({
        id: userData.userId,
        name: userData.username,
        email: "", // Email is not stored in cookies for security
        role: userData.role as UserRole,
      });
    }
    setIsLoading(false);
  }, []);

  const logout = () => {
    authUtils.clearAuth();
    setUser(null);
  };

  return (
    <UserContext.Provider value={{ user, setUser, isLoading, logout }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};
