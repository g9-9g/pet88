"use client";

import { useRooms } from "@/hooks/use-rooms";
import { useEffect, useState } from "react";
import RoomCard from "@/components/hotel/RoomCard";
import { Button } from "@/components/ui/button";
import { FaClipboardCheck, FaPlus } from "react-icons/fa";
import { useRouter, useSearchParams } from "next/navigation";
import { useUser } from "@/context/UserContext";
import AddRoomModal from "@/components/hotel/AddRoomModal";
import { toast } from "sonner";
import { Room, RoomType } from "@/lib/api/rooms";
import { IoFilter } from "react-icons/io5";
import FilterRoomsModal from "@/components/hotel/FilterRoomsModal";

const HotelPage = () => {
  const {
    rooms,
    loading,
    error,
    fetchRooms,
    addRoom,
    updateRoomById,
    removeRoom,
    updateRoomAvailability,
    searchAvailableRooms,
  } = useRooms();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { user } = useUser();
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isFilterModalOpen, setIsFilterModalOpen] = useState(false);

  // Get initial filters from URL params
  const getInitialFilters = () => {
    const start = searchParams.get("start") || "";
    const end = searchParams.get("end") || "";
    const type = searchParams.get("type") as RoomType | null;
    const minPrice = searchParams.get("minPrice");
    const maxPrice = searchParams.get("maxPrice");

    return {
      start,
      end,
      type: type || undefined,
      minPrice: minPrice ? Number(minPrice) : undefined,
      maxPrice: maxPrice ? Number(maxPrice) : undefined,
    };
  };

  useEffect(() => {
    const filters = getInitialFilters();
    if (filters.start && filters.end) {
      searchAvailableRooms(filters);
    } else {
      fetchRooms();
    }
  }, [fetchRooms, searchAvailableRooms, searchParams]);

  const handleAddRoom = async (data: {
    description: string;
    type: "STANDARD" | "DELUXE" | "LUXURY" | "SUITE";
    nightlyFee: number;
    cleanFee: number;
    serviceFee: number;
  }) => {
    try {
      await addRoom(data);
      setIsAddModalOpen(false);
      toast.success("Room added successfully");
    } catch (error) {
      console.error("Failed to add room:", error);
      toast.error("Failed to add room");
    }
  };

  const handleUpdateRoom = async (updatedRoom: Room) => {
    try {
      await updateRoomById(updatedRoom.id, {
        description: updatedRoom.description,
        type: updatedRoom.type,
        nightlyFee: updatedRoom.nightlyFee,
        cleanFee: updatedRoom.cleanFee,
        serviceFee: updatedRoom.serviceFee,
      });
      toast.success("Room updated successfully");
    } catch (error) {
      console.error("Failed to update room:", error);
      toast.error("Failed to update room");
    }
  };

  const handleDeleteRoom = async (roomId: number) => {
    try {
      await removeRoom(roomId);
      toast.success("Room deleted successfully");
    } catch (error) {
      console.error("Failed to delete room:", error);
      toast.error("Failed to delete room");
    }
  };

  const handleAvailabilityChange = async (
    roomId: number,
    available: boolean
  ) => {
    try {
      await updateRoomAvailability(roomId, { available });
      toast.success(
        `Room ${available ? "marked as available" : "marked as unavailable"}`
      );
    } catch (error) {
      console.error("Failed to update room availability:", error);
      toast.error("Failed to update room availability");
    }
  };

  const handleFilter = async (filters: {
    start?: string;
    end?: string;
    type?: RoomType;
    maxPrice?: number;
    minPrice?: number;
  }) => {
    try {
      // Update URL params
      const params = new URLSearchParams();
      if (filters.start) params.set("start", filters.start);
      if (filters.end) params.set("end", filters.end);
      if (filters.type) params.set("type", filters.type);
      if (filters.minPrice) params.set("minPrice", filters.minPrice.toString());
      if (filters.maxPrice) params.set("maxPrice", filters.maxPrice.toString());

      router.push(`/dashboard/hotel?${params.toString()}`);
      setIsFilterModalOpen(false);
    } catch (error) {
      console.error("Failed to apply filters:", error);
      toast.error("Failed to apply filters");
    }
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex flex-col gap-4 justify-between items-start mb-10">
        <h1 className="text-3xl font-bold">Hotel Rooms</h1>
        <div className="flex w-full justify-between items-center">
          {user?.role !== "ROLE_VET" && (
            <div className="flex flex-col gap-4 items-start">
              <Button
                onClick={() => setIsFilterModalOpen(true)}
                className="shadow-sm py-3 rounded-full text-black bg-white hover:bg-white/90 cursor-pointer"
              >
                <IoFilter className="mr-2 h-4 w-4" />
                Filter options
              </Button>
              <Button
                onClick={() => router.push("/dashboard/hotel/requests")}
                className="shadow-sm py-3 rounded-full text-white bg-brand hover:bg-brand-100 cursor-pointer"
              >
                <FaClipboardCheck className="mr-2 h-4 w-4" />
                See all room requests
              </Button>
            </div>
          )}
          {user?.role === "ROLE_ADMIN" && (
            <Button
              onClick={() => setIsAddModalOpen(true)}
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
          <RoomCard
            key={room.id}
            room={room}
            onUpdate={handleUpdateRoom}
            onDelete={handleDeleteRoom}
            onAvailabilityChange={handleAvailabilityChange}
          />
        ))}
      </div>

      <AddRoomModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onSubmit={handleAddRoom}
      />

      <FilterRoomsModal
        isOpen={isFilterModalOpen}
        onClose={() => setIsFilterModalOpen(false)}
        onSubmit={handleFilter}
        initialFilters={getInitialFilters()}
      />
    </div>
  );
};

export default HotelPage;
