"use client";

import React, { useEffect, useState } from "react";
import { useUser } from "@/context/UserContext";
import { useBookings } from "@/hooks/use-bookings";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { IoArrowBack, IoFilter } from "react-icons/io5";
import { useRouter, useSearchParams } from "next/navigation";
import { cn, formatDateTime } from "@/lib/utils";
import { Booking, BookingStatus } from "@/lib/api/bookings";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { FaCheck, FaTimes } from "react-icons/fa";
import { toast } from "sonner";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

const HotelRequestsPage = () => {
  const { user, isLoading } = useUser();
  const {
    bookings,
    loading,
    error,
    fetchMyBookings,
    fetchBookings,
    updateBookingStatusById,
  } = useBookings();
  const router = useRouter();
  const searchParams = useSearchParams();
  const status = searchParams.get("status") || "ALL";
  const [cancelDialog, setCancelDialog] = useState<{
    isOpen: boolean;
    bookingId: number;
  } | null>(null);

  useEffect(() => {
    if (!isLoading && !user) {
      router.push("/sign-in");
    }
  }, [user, isLoading, router]);

  useEffect(() => {
    if (user) {
      if (user.role === "ROLE_PET_OWNER") {
        fetchMyBookings();
      } else if (user.role === "ROLE_STAFF" || user.role === "ROLE_ADMIN") {
        fetchBookings(status === "ALL" ? undefined : (status as BookingStatus));
      }
    }
  }, [user, fetchMyBookings, fetchBookings, status]);

  if (isLoading) {
    return <div>Loading user information...</div>;
  }

  if (!user) {
    return null;
  }

  const getStatusColor = (status: BookingStatus) => {
    switch (status) {
      case "PENDING":
        return "bg-amber-400";
      case "CONFIRMED":
        return "bg-blue-400";
      case "CHECKED_IN":
        return "bg-purple-400";
      case "COMPLETED":
        return "bg-lime-500";
      case "CANCELLED":
        return "bg-red-400";
      default:
        return "bg-gray-500";
    }
  };

  const handleStatusChange = (value: string) => {
    const params = new URLSearchParams(searchParams.toString());
    if (value === "ALL") {
      params.delete("status");
    } else {
      params.set("status", value);
    }
    router.push(`/dashboard/hotel/requests?${params.toString()}`);
  };

  const handleCancel = async () => {
    if (!cancelDialog) return;

    try {
      await updateBookingStatusById(cancelDialog.bookingId, "CANCELLED");
      toast.success("Booking cancelled successfully");
      setCancelDialog(null);
    } catch (error) {
      console.error("Failed to cancel booking:", error);
      toast.error("Failed to cancel booking");
    }
  };

  const getActionButtons = (booking: Booking) => {
    if (user?.role === "ROLE_PET_OWNER") {
      if (["PENDING", "CONFIRMED"].includes(booking.status)) {
        return (
          <div className="mt-4">
            <Button
              onClick={() =>
                setCancelDialog({
                  isOpen: true,
                  bookingId: booking.id,
                })
              }
              className="border border-red-400 hover:bg-red-400 text-red-400 hover:text-white"
            >
              Cancel Booking
            </Button>
          </div>
        );
      }
      return null;
    }

    if (user?.role !== "ROLE_STAFF" && user?.role !== "ROLE_ADMIN") {
      return null;
    }

    switch (booking.status) {
      case "PENDING":
        return (
          <div className="mt-4 flex gap-2">
            <Button
              onClick={() => updateBookingStatusById(booking.id, "CONFIRMED")}
              className="border border-lime-500 hover:bg-lime-500 hover:text-white text-lime-500"
            >
              <FaCheck className="" />
            </Button>
            <Button
              onClick={() => updateBookingStatusById(booking.id, "CANCELLED")}
              className="border border-red-400 hover:bg-red-400 text-red-400 hover:text-white"
            >
              <FaTimes className="" />
            </Button>
          </div>
        );
      case "CONFIRMED":
        return (
          <div className="mt-4 flex gap-2">
            <Button
              onClick={() => updateBookingStatusById(booking.id, "CHECKED_IN")}
              className="border border-purple-500 hover:bg-purple-500 hover:text-white text-purple-500"
            >
              Check In
            </Button>
          </div>
        );
      case "CHECKED_IN":
        return (
          <div className="mt-4 flex gap-2">
            <Button
              onClick={() => updateBookingStatusById(booking.id, "COMPLETED")}
              className="border border-lime-500 hover:bg-lime-500 hover:text-white text-lime-500"
            >
              Complete
            </Button>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-4">
          <Button
            variant="ghost"
            className="hover:bg-brand-100 hover:text-white cursor-pointer rounded-full"
            onClick={() => router.push("/dashboard/hotel")}
          >
            <IoArrowBack className="w-6 h-6" />
          </Button>
          <h1 className="text-3xl font-bold">Room Bookings</h1>
        </div>

        <div className="w-min px-2">
          <Select value={status} onValueChange={handleStatusChange}>
            <SelectTrigger className="w-full px-4 py-2 font-semibold border-none rounded-full shadow-sm">
              <IoFilter className="w-6 h-6 mr-2" />
              <SelectValue placeholder="Filter by status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">All</SelectItem>
              <SelectItem value="PENDING">Pending</SelectItem>
              <SelectItem value="CONFIRMED">Confirmed</SelectItem>
              <SelectItem value="CHECKED_IN">Checked In</SelectItem>
              <SelectItem value="COMPLETED">Completed</SelectItem>
              <SelectItem value="CANCELLED">Cancelled</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="flex flex-col gap-8">
        {bookings.map((booking: Booking) => {
          const date = new Date(booking.checkInTime);
          const month = date.toLocaleString("default", { month: "long" });
          const year = date.getFullYear();
          const day = date.getDate();
          const time = date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          });

          return (
            <Card
              key={booking.id}
              className="flex flex-row p-0 bg-white rounded-2xl relative overflow-hidden shadow-lg border-none min-h-[200px]"
            >
              {/* Status badge at top right */}
              <div className="absolute right-8 top-5 text-slate-100 flex flex-col items-end">
                <Badge
                  className={cn(
                    getStatusColor(booking.status),
                    "rounded-full px-3 py-1 font-bold tracking-wider shadow-sm"
                  )}
                >
                  {booking.status}
                </Badge>
                {getActionButtons(booking)}
              </div>

              {/* Left section: Date */}
              <div className="bg-slate-200 flex flex-col items-center justify-between py-6 px-6 w-36 min-w-[8rem] text-center">
                <div>
                  <div className="text-gray-800 text-lg font-mono flex flex-col items-center justify-between">
                    <span>{year}</span>
                    <span>{month}</span>
                  </div>
                  <div className="text-4xl font-bold text-gray-800 mt-1">
                    {day}
                  </div>
                </div>
                <div className="text-gray-700 text-sm font-mono mt-4">
                  {time}
                </div>
              </div>

              {/* Right section: Content */}
              <div className="flex-1 p-4 flex flex-col justify-between bg-white">
                <div className="text-xl font-mono mb-2 ml-0">
                  {booking.roomType} Room for{" "}
                  <span className="text-2xl font-semibold">
                    {booking.petName}
                  </span>
                </div>
                <div className="flex flex-col gap-2 text-gray-500 text-base mb-4 font-mono">
                  <div className="flex flex-row gap-2 items-center">
                    Check-in:{" "}
                    <span className="font-semibold text-gray-700">
                      {formatDateTime(booking.checkInTime).dateTime}
                    </span>
                  </div>
                  <div className="flex flex-row gap-2 items-center">
                    Check-out:{" "}
                    <span className="font-semibold text-gray-700">
                      {formatDateTime(booking.checkOutTime).dateTime}
                    </span>
                  </div>
                  <div className="flex flex-row gap-2 items-center">
                    Booked at{" "}
                    <span className="font-semibold text-gray-700">
                      {formatDateTime(booking.createdAt).dateTime}
                    </span>
                  </div>
                  <div className="flex flex-row gap-2 items-center">
                    Estimated Fee:{" "}
                    <span className="font-semibold text-gray-700">
                      {booking.estimatedFee.toLocaleString("vi-VN")} VND
                    </span>
                  </div>
                </div>
                {booking.specialCareNotes && (
                  <div className="flex flex-col gap-1">
                    <span className="font-semibold text-gray-500">
                      Special Care Notes:
                    </span>
                    <span className="text-gray-700 whitespace-pre-line">
                      {booking.specialCareNotes}
                    </span>
                  </div>
                )}
              </div>

              <div
                className={cn(
                  "min-h-full w-3 rounded-full absolute left-0",
                  getStatusColor(booking.status)
                )}
              ></div>
            </Card>
          );
        })}
      </div>

      <AlertDialog
        open={cancelDialog?.isOpen || false}
        onOpenChange={(open) => !open && setCancelDialog(null)}
      >
        <AlertDialogContent className="bg-white border-none rounded-2xl shadow-md">
          <AlertDialogHeader>
            <AlertDialogTitle className="text-2xl font-bold">
              Cancel Booking
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to cancel this room booking? This action
              cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="border-none">
              No, keep it
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleCancel}
              className="bg-brand hover:bg-brand-100 text-white"
            >
              Yes, cancel booking
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
};

export default HotelRequestsPage;
