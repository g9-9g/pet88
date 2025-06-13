import { Button } from "@/components/ui/button";
import Image from "next/image";
import Link from "next/link";
import { BsFillPlayCircleFill } from "react-icons/bs";

export default function Home() {
  return (
    <div className="flex md:flex-row flex-col justify-around items-center min-w-7xl">
      {/* textual area */}
      <div className="flex flex-col items-start justify-start md:ml-20 mx-10 mt-10 md:mt-0">
        <p className="text-brand font-semibold text-lg">Veterinary</p>
        <h2 className="font-bold md:text-5xl text-4xl text-gray-800">
          Treating Your Pet By Our Professionals.
        </h2>
        <p className="md:text-base text-sm font-semibold text-gray-400 mt-5">
          Our pet management system helps you keep track of your pets' health,
          appointments, and care schedules. With easy scheduling, detailed pet
          profiles, and professional veterinary services, we ensure your pets
          receive the best care possible.
        </p>
        <div className="mt-10 flex justify-center items-center gap-5">
          <Link href="/dashboard/requests">
            <button className="text-white bg-brand font-semibold rounded-full px-5 md:py-4 py-2 hover:bg-brand-100 hover:shadow-lg cursor-pointer">
              Create Schedule
            </button>
          </Link>
          <button className="bg-gray-50 rounded-full shadow-lg p-2">
            <BsFillPlayCircleFill className="text-brand md:text-5xl text-4xl hover:text-brand-100 hover:shadow-lg" />
          </button>
        </div>
      </div>
      {/* image area */}
      <div className="mr-20 md:block hidden">
        <Image
          src="/assets/images/Seia_ball.png"
          alt="hero"
          width={1400}
          height={1200}
          className="object-cover"
        />
      </div>
    </div>
  );
}
