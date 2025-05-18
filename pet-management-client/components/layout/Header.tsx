import React from "react";

import Image from "next/image";
import Search from "./Search";
import { Button } from "../ui/button";

const Header = ({
  ownerId,
  accountId,
}: {
  ownerId: string;
  accountId: string;
}) => {
  return (
    <header className="header">
      <Search />
      <div className="header-wrapper">
        <form
        // action={async () => {
        //   "use server";
        //   await signOutUser();
        // }}
        >
          <Button
            type="submit"
            className="flex-center h-[52px] min-w-[54px] items-center rounded-full bg-brand/10 p-0 text-brand shadow-none transition-all hover:bg-brand/20"
          >
            <Image
              src="/assets/icons/logout.svg"
              alt="logo"
              width={24}
              height={24}
              className="w-6"
            />
          </Button>
        </form>
      </div>
    </header>
  );
};

export default Header;
