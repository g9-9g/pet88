import { useState, useCallback } from "react";
import {
  Room,
  CreateRoomDto,
  UpdateRoomDto,
  UpdateRoomStatusDto,
  SearchAvailableRoomsParams,
  getRooms,
  createRoom,
  updateRoom,
  deleteRoom,
  updateRoomStatus,
  getAvailableRooms,
} from "@/lib/api/rooms";
import { toast } from "sonner";

export const useRooms = () => {
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRooms = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getRooms();
      setRooms(data || []);
    } catch (err) {
      console.error("Failed to fetch rooms:", err);
      setError("Failed to load rooms");
      toast.error("Failed to load rooms");
      setRooms([]);
    } finally {
      setLoading(false);
    }
  }, []);

  const addRoom = useCallback(async (roomData: CreateRoomDto) => {
    try {
      setLoading(true);
      setError(null);
      const newRoom = await createRoom(roomData);
      setRooms((prev) => [...prev, newRoom]);
      toast.success("Room created successfully");
      return newRoom;
    } catch (err) {
      setError("Failed to create room");
      toast.error("Failed to create room");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const updateRoomById = useCallback(
    async (id: number, data: UpdateRoomDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRoom = await updateRoom(id, data);
        setRooms((prev) =>
          prev.map((room) => (room.id === id ? updatedRoom : room))
        );
        toast.success("Room updated successfully");
        return updatedRoom;
      } catch (err) {
        setError("Failed to update room");
        toast.error("Failed to update room");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const removeRoom = useCallback(async (id: number) => {
    try {
      setLoading(true);
      setError(null);
      await deleteRoom(id);
      setRooms((prev) => prev.filter((room) => room.id !== id));
      toast.success("Room deleted successfully");
    } catch (err) {
      setError("Failed to delete room");
      toast.error("Failed to delete room");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const updateRoomAvailability = useCallback(
    async (id: number, data: UpdateRoomStatusDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRoom = await updateRoomStatus(id, data);
        setRooms((prev) =>
          prev.map((room) => (room.id === id ? updatedRoom : room))
        );
        toast.success(
          `Room ${
            data.available ? "marked as available" : "marked as unavailable"
          }`
        );
        return updatedRoom;
      } catch (err) {
        setError("Failed to update room status");
        toast.error("Failed to update room status");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const searchAvailableRooms = useCallback(
    async (params: SearchAvailableRoomsParams) => {
      try {
        setLoading(true);
        setError(null);
        const availableRooms = await getAvailableRooms(params);
        setRooms(availableRooms || []);
        return availableRooms;
      } catch (err) {
        console.error("Failed to search available rooms:", err);
        setError("Failed to search available rooms");
        toast.error("Failed to search available rooms");
        setRooms([]);
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return {
    rooms,
    loading,
    error,
    fetchRooms,
    addRoom,
    updateRoomById,
    removeRoom,
    updateRoomAvailability,
    searchAvailableRooms,
  };
};
