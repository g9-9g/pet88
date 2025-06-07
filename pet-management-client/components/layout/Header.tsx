"use client";

import { useUser } from "@/context/UserContext";
import { useRouter } from "next/navigation";
import Image from "next/image";
import Search from "./Search";
import { Button } from "../ui/button";
import { MdOutlineNotifications } from "react-icons/md";
import { useNotifications } from "@/hooks/use-notifications";
import { useEffect, useState } from "react";
import { Notification, NotificationStatus } from "@/lib/api/notifications";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "../ui/input";
import { Label } from "../ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { getUsersByRole, User } from "@/lib/api/users";
import { toast } from "sonner";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";

const Header = () => {
  const { user, logout } = useUser();
  const router = useRouter();
  const {
    notifications,
    fetchOwnedNotifications,
    markAsRead,
    addNotification,
    markAllAsRead,
  } = useNotifications();
  const [showNotifications, setShowNotifications] = useState(false);
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUserId, setSelectedUserId] = useState<string>("");
  const [message, setMessage] = useState("");
  const [statusFilter, setStatusFilter] = useState<NotificationStatus | "ALL">(
    "ALL"
  );

  useEffect(() => {
    if (user) {
      fetchOwnedNotifications(
        statusFilter === "ALL" ? undefined : statusFilter
      );
    }
  }, [user, fetchOwnedNotifications, statusFilter]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const [owners, vets] = await Promise.all([
          getUsersByRole("ROLE_PET_OWNER"),
          getUsersByRole("ROLE_VET"),
        ]);
        setUsers([...owners, ...vets]);
      } catch (error) {
        console.error("Failed to fetch users:", error);
        toast.error("Failed to load users");
      }
    };
    fetchUsers();
  }, []);

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

  const handleSendNotification = async () => {
    if (!selectedUserId || !message.trim()) {
      toast.error("Please select a user and enter a message");
      return;
    }

    try {
      await addNotification({
        userId: parseInt(selectedUserId),
        message: message.trim(),
      });
      setMessage("");
      setSelectedUserId("");
      toast.success("Notification sent successfully");
    } catch (error) {
      console.error("Failed to send notification:", error);
      toast.error("Failed to send notification");
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
                <Dialog>
                  <DialogTrigger asChild>
                    <Button
                      variant="link"
                      size="icon"
                      className="text-sm mx-2 text-slate-900"
                    >
                      Send new
                    </Button>
                  </DialogTrigger>
                  <DialogContent className="max-w-md bg-white">
                    <DialogHeader>
                      <DialogTitle>Send New Notification</DialogTitle>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                      <div className="grid gap-2">
                        <Label htmlFor="user">Select User</Label>
                        <Select
                          value={selectedUserId}
                          onValueChange={setSelectedUserId}
                        >
                          <SelectTrigger>
                            <SelectValue placeholder="Select a user" />
                          </SelectTrigger>
                          <SelectContent>
                            {users.map((user) => (
                              <SelectItem
                                key={user.id}
                                value={user.id.toString()}
                              >
                                {user.username} ({user.role})
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="grid gap-2">
                        <Label htmlFor="message">Message</Label>
                        <Input
                          id="message"
                          value={message}
                          onChange={(e) => setMessage(e.target.value)}
                          placeholder="Enter your message"
                        />
                      </div>
                      <Button
                        className="bg-brand text-white hover:bg-brand-100 rounded-2xl"
                        onClick={handleSendNotification}
                      >
                        Send
                      </Button>
                    </div>
                  </DialogContent>
                </Dialog>
              )}
            </div>
            <div className="px-4 pb-2">
              <RadioGroup
                value={statusFilter}
                onValueChange={(value: string) =>
                  setStatusFilter(value as NotificationStatus | "ALL")
                }
                className="flex gap-4"
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="ALL" id="all" />
                  <Label htmlFor="all" className="text-sm">
                    All
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="READ" id="read" />
                  <Label htmlFor="read" className="text-sm">
                    Read
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="UNREAD" id="unread" />
                  <Label htmlFor="unread" className="text-sm">
                    Unread
                  </Label>
                </div>
              </RadioGroup>
            </div>
            {unreadCount > 0 && (
              <button
                className="bg-brand text-white hover:bg-brand-100 rounded-2xl text-xs font-semibold px-2 py-1 mx-3"
                onClick={markAllAsRead}
              >
                Mark all as read
              </button>
            )}
            {notifications.length === 0 ? (
              <p className="text-gray-500 text-center py-4">No notifications</p>
            ) : (
              <div className="max-h-52 overflow-y-auto">
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
