"use client";

import { navItems } from "@/constants";
import { cn } from "@/lib/utils";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";
import Icon from "../common/Icon";
import NavIcon from "../icons/NavIcons";

interface Props {
  fullName: string;
  avatar: string;
  email: string;
}

const Sidebar = ({ fullName, avatar, email }: Props) => {
  const pathname = usePathname();

  const isActive = (itemUrl: string) => {
    // Exact match for the main dashboard link
    if (itemUrl === "/dashboard") {
      return pathname === itemUrl;
    }
    // For other items, check if the current path starts with the item's URL
    // and also ensure it's not just the main dashboard path unless the item IS the dashboard.
    // This prevents, for example, "Pets" being active if the path is just "/dashboard"
    if (pathname === "/dashboard" && itemUrl !== "/dashboard") return false;

    return pathname.startsWith(itemUrl);
  };

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
          {navItems.map(({ url, name, icon }) => (
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
          src={avatar}
          alt="avatar"
          width={44}
          height={44}
          className="sidebar-user-avatar"
        />
        <div className="hidden lg:block">
          <p className="subtitle-2 capitalize">{fullName}</p>
          <p className="caption">{email}</p>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
