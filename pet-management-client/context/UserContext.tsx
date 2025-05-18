"use client";

import React, {
  createContext,
  useContext,
  useState,
  ReactNode,
  useEffect,
} from "react";

// Define the user roles - adjust these to match your actual roles
export type UserRole =
  | "ROLE_PET_OWNER"
  | "ROLE_DOCTOR"
  | "ROLE_STAFF"
  | "ROLE_ADMIN"
  | "GUEST";

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  // Add any other user-specific fields you need
}

interface UserContextType {
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>; // To allow updating the user, e.g., after login
  isLoading: boolean; // To handle async user fetching
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
  children: ReactNode;
}

export const UserProvider = ({ children }: UserProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true); // Start with loading true

  // In a real app, you'd fetch the user session here, e.g., from an API or cookies
  useEffect(() => {
    // Simulate fetching user data
    // Replace this with your actual user fetching logic
    const fetchUser = async () => {
      setIsLoading(true);
      // Try to get user from localStorage or an API endpoint
      // For now, let's mock a logged-in user (e.g., a Pet Owner)
      // You can change this to test different roles
      setTimeout(() => {
        setUser({
          id: "user-123",
          name: "John Doe",
          email: "john.doe@example.com",
          role: "ROLE_PET_OWNER", // <--- CHANGE THIS TO TEST DIFFERENT ROLES
        });
        setIsLoading(false);
      }, 500); // Simulate network delay
    };

    fetchUser();
  }, []);

  return (
    <UserContext.Provider value={{ user, setUser, isLoading }}>
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
