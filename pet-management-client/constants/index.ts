export const navItems = [
  {
    name: "Dashboard",
    icon: "/assets/icons/dashboard.svg",
    url: "/dashboard",
  },
  {
    name: "Appointments",
    icon: "/assets/icons/calendar.svg",
    url: "/dashboard/appointments",
  },
  {
    name: "Pets",
    icon: "/assets/icons/pet.svg",
    url: "/dashboard/pets",
  },
  {
    name: "Services",
    icon: "/assets/icons/services.svg",
    url: "/dashboard/services",
  },
  {
    name: "Medical Records",
    icon: "/assets/icons/medical.svg",
    url: "/dashboard/records",
  },
  {
    name: "Staff",
    icon: "/assets/icons/staff.svg",
    url: "/dashboard/staff",
  },
  {
    name: "Settings",
    icon: "/assets/icons/settings.svg",
    url: "/dashboard/settings",
  },
];

export const actionsDropdownItems = [
  {
    label: "Rename",
    icon: "/assets/icons/edit.svg",
    value: "rename",
  },
  {
    label: "Details",
    icon: "/assets/icons/info.svg",
    value: "details",
  },
  {
    label: "Share",
    icon: "/assets/icons/share.svg",
    value: "share",
  },
  {
    label: "Download",
    icon: "/assets/icons/download.svg",
    value: "download",
  },
  {
    label: "Delete",
    icon: "/assets/icons/delete.svg",
    value: "delete",
  },
];

export const sortTypes = [
  {
    label: "Date created (newest)",
    value: "$createdAt-desc",
  },
  {
    label: "Date Created (oldest)",
    value: "$createdAt-asc",
  },
  {
    label: "Name (A-Z)",
    value: "name-asc",
  },
  {
    label: "Name (Z-A)",
    value: "name-desc",
  },
  {
    label: "Size (Highest)",
    value: "size-desc",
  },
  {
    label: "Size (Lowest)",
    value: "size-asc",
  },
];

export const avatarPlaceholderUrl =
  "https://pbs.twimg.com/media/Gmua5bGbkAAWpSo?format=jpg";

export const MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
