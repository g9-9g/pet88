import { useState, useCallback } from "react";
import {
  Booking,
  CreateBookingDto,
  UpdateBookingDto,
  SearchBookingsParams,
  getBookings,
  getMyBookings,
  createBooking,
  updateBooking,
  cancelBooking,
  searchBookings,
} from "@/lib/api/bookings";
import { toast } from "sonner";

export const useBookings = () => {
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchBookings = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getBookings();
      setBookings(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch bookings:", err);
      setError("Failed to load bookings");
      toast.error("Failed to load bookings");
      setBookings([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchMyBookings = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getMyBookings();
      setBookings(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch my bookings:", err);
      setError("Failed to load my bookings");
      toast.error("Failed to load my bookings");
      setBookings([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const addBooking = useCallback(async (bookingData: CreateBookingDto) => {
    try {
      setLoading(true);
      setError(null);
      const newBooking = await createBooking(bookingData);
      setBookings((prev) => [...prev, newBooking]);
      setTotalItems((prev) => prev + 1);
      toast.success("Booking created successfully");
      return newBooking;
    } catch (err) {
      setError("Failed to create booking");
      toast.error("Failed to create booking");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const updateBookingById = useCallback(
    async (id: number, data: UpdateBookingDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedBooking = await updateBooking(id, data);
        setBookings((prev) =>
          prev.map((booking) => (booking.id === id ? updatedBooking : booking))
        );
        toast.success("Booking updated successfully");
        return updatedBooking;
      } catch (err) {
        setError("Failed to update booking");
        toast.error("Failed to update booking");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const removeBooking = useCallback(async (id: number) => {
    try {
      setLoading(true);
      setError(null);
      await cancelBooking(id);
      setBookings((prev) => prev.filter((booking) => booking.id !== id));
      setTotalItems((prev) => prev - 1);
      toast.success("Booking cancelled successfully");
    } catch (err) {
      setError("Failed to cancel booking");
      toast.error("Failed to cancel booking");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const searchBookingsList = useCallback(
    async (params: SearchBookingsParams) => {
      try {
        setLoading(true);
        setError(null);
        const response = await searchBookings(params);
        setBookings(response.bookings || []);
        setTotalItems(response.totalItems);
        setTotalPages(response.totalPages);
        setCurrentPage(response.currentPage);
      } catch (err) {
        console.error("Failed to search bookings:", err);
        setError("Failed to search bookings");
        toast.error("Failed to search bookings");
        setBookings([]);
        setTotalItems(0);
        setTotalPages(0);
        setCurrentPage(0);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return {
    bookings,
    loading,
    error,
    totalItems,
    totalPages,
    currentPage,
    fetchBookings,
    fetchMyBookings,
    addBooking,
    updateBookingById,
    removeBooking,
    searchBookingsList,
  };
};
