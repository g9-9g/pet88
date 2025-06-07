import { privateApi } from "./client";
import { Pet } from "./pets";
import { Room } from "./rooms";

export type BookingStatus =
  | "PENDING"
  | "CONFIRMED"
  | "CHECKED_IN"
  | "COMPLETED"
  | "CANCELLED";

export interface Booking {
  id: number;
  petId: number;
  ownerId: number;
  roomId: number;
  checkInTime: string;
  checkOutTime: string;
  status: BookingStatus;
  specialCareNotes: string | null;
  estimatedFee: number;
  createdAt: string;
  updatedAt: string;
  confirmed: boolean;
  pet?: Pet;
  room?: Room;
}

export interface CreateBookingDto {
  petId: number;
  roomId: number;
  checkInTime: string;
  checkOutTime: string;
  specialCareNotes?: string;
}

export interface UpdateBookingDto {
  checkInTime?: string;
  checkOutTime?: string;
  status?: BookingStatus;
  specialCareNotes?: string;
  confirmed?: boolean;
}

export interface SearchBookingsParams {
  ownerId?: number;
  petId?: number;
  roomId?: number;
  status?: BookingStatus;
  startDate?: string;
  endDate?: string;
  page?: number;
  size?: number;
}

export interface PaginatedBookingsResponse {
  totalItems: number;
  totalPages: number;
  bookings: Booking[];
  currentPage: number;
}

// Get all bookings
export const getBookings = async (): Promise<Booking[]> => {
  const response = await privateApi.get<Booking[]>("/api/bookings");
  return response.data;
};

// Get my bookings (for logged-in user)
export const getMyBookings = async (): Promise<Booking[]> => {
  const response = await privateApi.get<Booking[]>("/api/bookings/my-bookings");
  return response.data;
};

// Create a new booking
export const createBooking = async (
  data: CreateBookingDto
): Promise<Booking> => {
  const response = await privateApi.post<Booking>("/api/bookings", data);
  return response.data;
};

// Update a booking
export const updateBooking = async (
  id: number,
  data: UpdateBookingDto
): Promise<Booking> => {
  const response = await privateApi.put<Booking>(`/api/bookings/${id}`, data);
  return response.data;
};

// Cancel a booking
export const cancelBooking = async (id: number): Promise<void> => {
  await privateApi.delete(`/api/bookings/${id}`);
};

// Search bookings
export const searchBookings = async (
  params: SearchBookingsParams
): Promise<PaginatedBookingsResponse> => {
  const response = await privateApi.get<PaginatedBookingsResponse>(
    "/api/bookings/search",
    { params }
  );
  return response.data;
};

// Update booking status
export const updateBookingStatus = async (
  id: number,
  status: BookingStatus
): Promise<Booking> => {
  const response = await privateApi.put<Booking>(
    `/api/bookings/${id}/status`,
    status
  );
  return response.data;
};
