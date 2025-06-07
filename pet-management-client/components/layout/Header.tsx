"use client";

import { useUser } from "@/context/UserContext";
import { useRouter } from "next/navigation";
import Image from "next/image";
import Search from "./Search";
import { Button } from "../ui/button";
import { MdOutlineNotifications } from "react-icons/md";
import { useNotifications } from "@/hooks/use-notifications";
import { useEffect } from "react";
import { Notification } from "@/lib/api/notifications";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const Header = () => {
  const { user, logout } = useUser();
  const router = useRouter();
  const { notifications, fetchOwnedNotifications, markAsRead } =
    useNotifications();

  useEffect(() => {
    if (user) {
      fetchOwnedNotifications("UNREAD");
    }
  }, [user, fetchOwnedNotifications]);

  if (!user) return null;

  const handleLogout = () => {
    logout();
    router.push("/sign-in");
  };

  const unreadCount = notifications.filter((n) => n.status === "UNREAD").length;

  const handleNotificationClick = (notification: Notification) => {
    if (notification.status === "UNREAD") {
      markAsRead(notification.id);
    }
  };

  return (
    <header className="header">
      <Search />
      <div className="header-wrapper">
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button className="relative flex-center h-[52px] min-w-[54px] items-center rounded-full p-0 text-brand cursor-pointer shadow-md transition-all hover:bg-brand/20">
              <MdOutlineNotifications className="size-8" />
              {unreadCount > 0 && (
                <span className="absolute top-0 right-0 bg-red-500 text-white rounded-full text-xs w-4 h-4 flex items-center justify-center">
                  {unreadCount}
                </span>
              )}
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-80 p-0 rounded-2xl border-none shadow-lg"
            align="end"
          >
            <div className="p-4 w-full flex justify-between">
              <h3 className="text-lg text-brand-100 font-semibold flex gap-2 items-center">
                <MdOutlineNotifications className="size-6" />
                Notifications
              </h3>
              {(user?.role === "ROLE_ADMIN" || user?.role === "ROLE_STAFF") && (
                <Button
                  variant="link"
                  size="icon"
                  className="text-sm mx-2 text-slate-900"
                >
                  Send new
                </Button>
              )}
            </div>
            {notifications.length === 0 ? (
              <p className="text-gray-500 text-center py-4">No notifications</p>
            ) : (
              <div className="max-h-96 overflow-y-auto">
                {notifications.map((notification) => (
                  <div
                    key={notification.id}
                    className={`p-3 border-b last:border-b-0 border-gray-400/30 cursor-pointer hover:bg-gray-50 ${
                      notification.status === "UNREAD"
                        ? "bg-white font-semibold"
                        : "bg-gray-50"
                    }`}
                    onClick={() => handleNotificationClick(notification)}
                  >
                    <p className="text-sm text-gray-800">
                      {notification.message}
                    </p>
                  </div>
                ))}
              </div>
            )}
          </DropdownMenuContent>
        </DropdownMenu>

        <Button
          onClick={handleLogout}
          className="flex-center h-[52px] min-w-[54px] items-center rounded-full bg-brand/10 p-0 text-brand shadow-none transition-all hover:bg-brand/20"
        >
          <Image
            src="/assets/icons/logout.svg"
            alt="logout"
            width={24}
            height={24}
            className="w-6"
          />
        </Button>
      </div>
    </header>
  );
};

export default Header;
