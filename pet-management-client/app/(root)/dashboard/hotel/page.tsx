"use client";

import { useRooms } from "@/hooks/use-rooms";
import { useEffect } from "react";
import RoomCard from "@/components/hotel/RoomCard";
import { Button } from "@/components/ui/button";
import { FaPlus } from "react-icons/fa";
import { useRouter } from "next/navigation";
import { useUser } from "@/context/UserContext";

const HotelPage = () => {
  const { rooms, loading, error, fetchRooms } = useRooms();
  const router = useRouter();
  const { user } = useUser();

  useEffect(() => {
    fetchRooms();
  }, [fetchRooms]);

  return (
    <div className="container mx-auto py-6">
      <div className="flex flex-col gap-4 justify-between items-start mb-6">
        <h1 className="text-3xl font-bold">Hotel Rooms</h1>
        <div className="flex w-full justify-between items-center">
          {user?.role === "ROLE_ADMIN" && (
            <Button
              onClick={() => router.push("/dashboard/hotel/rooms/add")}
              className="shadow-sm py-3 rounded-full text-white bg-brand hover:bg-brand-100 cursor-pointer"
            >
              <FaPlus className="mr-2 h-4 w-4" />
              Add New Room
            </Button>
          )}
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {rooms.map((room) => (
          <RoomCard key={room.id} room={room} />
        ))}
      </div>
    </div>
  );
};

export default HotelPage;
