import Cookies from "js-cookie";

const TOKEN_KEY = "auth_token";
const USER_KEY = "user_data";

export interface UserData {
  userId: number;
  username: string;
  role: string;
}

export const authUtils = {
  setToken: (token: string) => {
    Cookies.set(TOKEN_KEY, token, { expires: 7 }); // Token expires in 7 days
  },

  getToken: () => {
    return Cookies.get(TOKEN_KEY);
  },

  removeToken: () => {
    Cookies.remove(TOKEN_KEY);
  },

  setUserData: (userData: UserData) => {
    Cookies.set(USER_KEY, JSON.stringify(userData), { expires: 7 });
  },

  getUserData: (): UserData | null => {
    const userData = Cookies.get(USER_KEY);
    return userData ? JSON.parse(userData) : null;
  },

  removeUserData: () => {
    Cookies.remove(USER_KEY);
  },

  clearAuth: () => {
    authUtils.removeToken();
    authUtils.removeUserData();
  },
};
