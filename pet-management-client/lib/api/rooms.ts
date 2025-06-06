import { privateApi } from "./client";

export type RoomType = "STANDARD" | "DELUXE" | "LUXURY" | "SUITE";

export interface Room {
  id: number;
  description: string;
  type: RoomType;
  nightlyFee: number;
  cleanFee: number;
  serviceFee: number;
  averageFee: number;
  createdAt: string | null;
  updatedAt: string | null;
  available: boolean;
}

export interface CreateRoomDto {
  description: string;
  type: RoomType;
  nightlyFee: number;
  cleanFee: number;
  serviceFee: number;
}

export interface UpdateRoomDto {
  description?: string;
  type?: RoomType;
  nightlyFee?: number;
  cleanFee?: number;
  serviceFee?: number;
  available?: boolean;
}

export interface UpdateRoomStatusDto {
  available: boolean;
}

export interface SearchAvailableRoomsParams {
  start: string;
  end: string;
  type?: RoomType;
  maxPrice?: number;
  minPrice?: number;
}

// Get all rooms
export const getRooms = async (): Promise<Room[]> => {
  const response = await privateApi.get<Room[]>("/api/bookings/rooms");
  return response.data;
};

// Create a new room
export const createRoom = async (data: CreateRoomDto): Promise<Room> => {
  const response = await privateApi.post<Room>("/api/bookings/rooms", data);
  return response.data;
};

// Update a room
export const updateRoom = async (
  id: number,
  data: UpdateRoomDto
): Promise<Room> => {
  const response = await privateApi.put<Room>(
    `/api/bookings/rooms/${id}`,
    data
  );
  return response.data;
};

// Delete a room
export const deleteRoom = async (id: number): Promise<void> => {
  await privateApi.delete(`/api/bookings/rooms/${id}`);
};

// Update room status
export const updateRoomStatus = async (
  id: number,
  data: UpdateRoomStatusDto
): Promise<Room> => {
  const response = await privateApi.put<Room>(
    `/api/bookings/rooms/${id}/status`,
    data
  );
  return response.data;
};

// Get available rooms
export const getAvailableRooms = async (
  params: SearchAvailableRoomsParams
): Promise<Room[]> => {
  const response = await privateApi.get<Room[]>(
    "/api/bookings/available-rooms",
    {
      params,
    }
  );
  return response.data;
};
