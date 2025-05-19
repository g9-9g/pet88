export type UserRole =
  | "ROLE_PET_OWNER"
  | "ROLE_VET"
  | "ROLE_STAFF"
  | "ROLE_ADMIN";

export const roleUtils = {
  getRoleRoute: (role: UserRole): string => {
    const roleMap: Record<UserRole, string> = {
      ROLE_PET_OWNER: "owner",
      ROLE_VET: "vet",
      ROLE_STAFF: "staff",
      ROLE_ADMIN: "admin",
    };
    return roleMap[role];
  },

  getRoleLabel: (role: UserRole): string => {
    const roleMap: Record<UserRole, string> = {
      ROLE_PET_OWNER: "Pet Owner",
      ROLE_VET: "Veterinarian",
      ROLE_STAFF: "Staff",
      ROLE_ADMIN: "Administrator",
    };
    return roleMap[role];
  },
};
