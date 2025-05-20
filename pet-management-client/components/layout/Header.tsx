"use client";

import { useUser } from "@/context/UserContext";
import { useRouter } from "next/navigation";
import Image from "next/image";
import Search from "./Search";
import { Button } from "../ui/button";

const Header = () => {
  const { user, logout } = useUser();
  const router = useRouter();

  if (!user) return null;

  const handleLogout = () => {
    logout();
    router.push("/sign-in");
  };

  return (
    <header className="header">
      <Search />
      <div className="header-wrapper">
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
