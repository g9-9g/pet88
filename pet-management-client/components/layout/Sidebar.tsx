"use client";

import { useUser } from "@/context/UserContext";
import { cn } from "@/lib/utils";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";
import Icon from "../common/Icon";
import NavIcon from "../icons/NavIcons";

const navItems = [
  {
    url: "/dashboard",
    name: "Dashboard",
    icon: "dashboard",
    roles: ["ROLE_PET_OWNER", "ROLE_VET", "ROLE_STAFF", "ROLE_ADMIN"],
  },
  {
    url: "/dashboard/pets",
    name: "Pets",
    icon: "pets",
    roles: ["ROLE_PET_OWNER", "ROLE_VET", "ROLE_STAFF", "ROLE_ADMIN"],
  },
  {
    url: "/dashboard/requests",
    name: "Requests",
    icon: "request",
    roles: ["ROLE_PET_OWNER", "ROLE_STAFF", "ROLE_ADMIN"],
  },
  {
    url: "/dashboard/appointments",
    name: "Appointments",
    icon: "calendar",
    roles: ["ROLE_PET_OWNER", "ROLE_VET", "ROLE_STAFF", "ROLE_ADMIN"],
  },
  {
    url: "/dashboard/users",
    name: "Users",
    icon: "users",
    roles: ["ROLE_ADMIN"],
  },
  {
    url: "/dashboard/services",
    name: "Services",
    icon: "services",
    roles: ["ROLE_PET_OWNER", "ROLE_VET", "ROLE_STAFF", "ROLE_ADMIN"],
  },
  {
    url: "/dashboard/hotel",
    name: "Hotel",
    icon: "hotel",
    roles: ["ROLE_PET_OWNER", "ROLE_VET", "ROLE_STAFF", "ROLE_ADMIN"],
  },
];

const Sidebar = () => {
  const pathname = usePathname();
  const { user } = useUser();

  if (!user) return null;

  const isActive = (itemUrl: string) => {
    if (itemUrl === "/dashboard") {
      return (
        pathname === "/dashboard" ||
        /^\/dashboard\/(owner|vet|staff|admin)$/.test(pathname)
      );
    }
    return pathname.startsWith(itemUrl);
  };

  const filteredNavItems = navItems.filter((item) =>
    item.roles.includes(user.role)
  );

  return (
    <aside className="sidebar">
      <Link href="/" className="block">
        <div className="hidden lg:block px-5">
          <Icon />
        </div>
        <div className="lg:hidden">
          <Icon showText={false} />
        </div>
      </Link>

      <nav className="sidebar-nav">
        <ul className="flex flex-1 flex-col gap-2">
          {filteredNavItems.map(({ url, name, icon }) => (
            <Link key={name} href={url} className="lg:w-full">
              <li
                className={cn(
                  "sidebar-nav-item hover:bg-light-300 transition duration-300",
                  isActive(url) && "shad-active hover:bg-brand"
                )}
              >
                <NavIcon
                  icon={icon}
                  className={cn(
                    "h-5 w-5",
                    isActive(url) ? "text-white" : "text-gray-500"
                  )}
                />
                <p className="hidden lg:block">{name}</p>
              </li>
            </Link>
          ))}
        </ul>
      </nav>
      <div className="flex-1 flex items-center justify-center">
        <div className="relative size-28">
          <Image
            src="/assets/images/Seia_ball.png"
            alt="logo"
            fill
            className="object-contain transition-all hover:rotate-2 hover:scale-105"
          />
        </div>
      </div>
      <div className="sidebar-user-info">
        <Image
          src="/assets/images/default-avatar.png"
          alt="avatar"
          width={44}
          height={44}
          className="sidebar-user-avatar"
        />
        <div className="hidden lg:block">
          <p className="subtitle-2 capitalize">{user.name}</p>
          <p className="caption capitalize">
            {user.role.replace("ROLE_", "").replace("_", " ")}
          </p>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
