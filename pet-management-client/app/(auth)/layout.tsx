import Image from "next/image";
import Link from "next/link";
import React from "react";
import { GiCat } from "react-icons/gi";

const Layout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="flex min-h-screen">
      <section className="hidden w-1/2 items-center justify-center bg-brand p-10 lg:flex xl:w-2/5">
        <div className="flex max-h-[800px] max-w-[430px] flex-col justify-center space-y-12">
          <Link
            href="/"
            className="flex gap-3 items-center hover:opacity-75 cursor-pointer"
          >
            <GiCat size={36} color="white" />
            <h1 className="text-3xl text-white">
              Pet<span className="font-semibold">Care88</span>
            </h1>
          </Link>
          <div className="space-y-5 text-white">
            <h1 className="h1">Treating Your Pet By Our Professionals</h1>
            <p className="body-1">
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Cumque
              non nobis nostrum libero est itaque dolores ratione nemo atque
              vitae.
            </p>
          </div>
          <Image
            src="/assets/images/Seia_ball.png"
            alt="Files"
            width={300}
            height={300}
            className="transition-all hover:-rotate-2 hover:scale-105"
          />
        </div>
      </section>
      <section className="flex flex-1 flex-col items-center bg-white text-dark-200 p-4 py-10 lg:justify-center lg:p-10 lg:py-0">
        <div className="mb-16 lg:hidden">
          <h1 className="text-3xl font-semibold text-white">PetCare88</h1>
        </div>
        {children}
      </section>
    </div>
  );
};

export default Layout;
