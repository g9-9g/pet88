import { privateApi } from "./client";

export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string | null;
  locked: boolean;
}

export const getUsersByRole = async (role: string): Promise<User[]> => {
  const response = await privateApi.get<User[]>("/api/users/search", {
    params: { role },
  });
  return response.data;
};
