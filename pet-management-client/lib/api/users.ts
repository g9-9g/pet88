import { privateApi } from "./client";
import { UserRole } from "@/lib/utils/role";

export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string | null;
  locked: boolean;
  role: UserRole;
}

export const getUsersByRole = async (role: string): Promise<User[]> => {
  const response = await privateApi.get<User[]>("/api/users/search", {
    params: { role },
  });
  return response.data;
};
