import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Image from "next/image";
import { Room } from "@/lib/api/rooms";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { useUser } from "@/context/UserContext";
import { useState } from "react";
import { useBookings } from "@/hooks/use-bookings";
import { toast } from "sonner";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { usePets } from "@/context/PetsContext";

interface RoomDetailModalProps {
  room: Room;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onUpdate?: (updatedRoom: Room) => void;
}

export const RoomDetailModal = ({
  room,
  open,
  onOpenChange,
  onUpdate,
}: RoomDetailModalProps) => {
  const { user } = useUser();
  const { pets } = usePets();
  const { addBooking } = useBookings();
  const [isBookingModalOpen, setIsBookingModalOpen] = useState(false);
  const [selectedPetId, setSelectedPetId] = useState<number | null>(null);
  const [checkInTime, setCheckInTime] = useState("");
  const [checkOutTime, setCheckOutTime] = useState("");
  const [specialCareNotes, setSpecialCareNotes] = useState("");

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  const handleBookRoom = async () => {
    if (!selectedPetId) {
      toast.error("Please select a pet");
      return;
    }

    if (!checkInTime || !checkOutTime) {
      toast.error("Please select check-in and check-out times");
      return;
    }

    try {
      await addBooking({
        petId: selectedPetId,
        roomId: room.id,
        checkInTime,
        checkOutTime,
        specialCareNotes: specialCareNotes || undefined,
      });
      toast.success("Room booked successfully");
      setIsBookingModalOpen(false);
      onOpenChange(false);
    } catch (error) {
      console.error("Failed to book room:", error);
      toast.error("Failed to book room");
    }
  };

  return (
    <>
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="max-w-md bg-light-300 rounded-2xl pt-12 flex flex-col items-center overflow-visible fixed left-1/2 -translate-x-1/2 -translate-y-1/2">
          <DialogHeader>
            <DialogTitle className="text-2xl font-bold text-center mb-2">
              {room.type} Room
            </DialogTitle>
          </DialogHeader>

          {/* Room Image */}
          <div className="w-full mb-6">
            <div className="relative w-full h-48 rounded-xl overflow-hidden">
              <Image
                src="/assets/images/default-room.jpg"
                alt={room.description}
                fill
                className="object-cover"
              />
            </div>
          </div>

          {/* Room Details */}
          <div className="w-full flex flex-col items-center px-4">
            <div className="w-full mb-4">
              <div className="flex flex-col items-start gap-2">
                <div className="text-base font-semibold text-gray-600 min-w-[100px]">
                  Description:
                </div>
                <div className="text-sm text-gray-800 flex-1 italic">
                  {room.description}
                </div>
              </div>
            </div>

            <div className="w-full space-y-4 mb-6">
              <div className="flex items-center justify-between">
                <div className="text-base font-semibold text-gray-600">
                  Nightly Fee:
                </div>
                <div className="text-base text-brand font-medium">
                  {formatPrice(room.nightlyFee)}
                </div>
              </div>

              <div className="flex items-center justify-between">
                <div className="text-base font-semibold text-gray-600">
                  Cleaning Fee:
                </div>
                <div className="text-base text-gray-800">
                  {formatPrice(room.cleanFee)}
                </div>
              </div>

              <div className="flex items-center justify-between">
                <div className="text-base font-semibold text-gray-600">
                  Service Fee:
                </div>
                <div className="text-base text-gray-800">
                  {formatPrice(room.serviceFee)}
                </div>
              </div>

              <div className="flex items-center justify-between">
                <div className="text-base font-semibold text-gray-600">
                  Average Fee:
                </div>
                <div className="text-base text-brand font-medium">
                  {formatPrice(room.averageFee)}
                </div>
              </div>

              <div className="flex items-center justify-between">
                <div className="text-base font-semibold text-gray-600">
                  Status:
                </div>
                <Badge
                  className={cn(
                    room.available ? "bg-lime-400" : "bg-red-500",
                    "rounded-full px-3 py-1 font-bold tracking-wider text-white"
                  )}
                >
                  {room.available ? "Available" : "Unavailable"}
                </Badge>
              </div>
            </div>

            {user?.role === "ROLE_PET_OWNER" && room.available ? (
              <Button
                onClick={() => setIsBookingModalOpen(true)}
                className="w-full bg-brand text-white hover:bg-brand-100"
              >
                Book this room
              </Button>
            ) : (
              <Button
                onClick={() => onOpenChange(false)}
                className="w-full bg-brand text-white hover:bg-brand-100"
              >
                Close
              </Button>
            )}
          </div>
        </DialogContent>
      </Dialog>

      {/* Booking Modal */}
      <Dialog open={isBookingModalOpen} onOpenChange={setIsBookingModalOpen}>
        <DialogContent className="max-w-md bg-light-300 rounded-2xl pt-12">
          <DialogHeader>
            <DialogTitle className="text-2xl font-bold text-center mb-2">
              Book Room
            </DialogTitle>
          </DialogHeader>

          <div className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="pet">Select Pet</Label>
              <Select
                value={selectedPetId?.toString()}
                onValueChange={(value) => setSelectedPetId(Number(value))}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Select a pet" />
                </SelectTrigger>
                <SelectContent>
                  {pets.map((pet) => (
                    <SelectItem key={pet.petId} value={pet.petId.toString()}>
                      {pet.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="checkIn">Check-in Date & Time</Label>
              <Input
                id="checkIn"
                type="datetime-local"
                value={checkInTime}
                onChange={(e) => setCheckInTime(e.target.value)}
                min={new Date().toISOString().slice(0, 16)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="checkOut">Check-out Date & Time</Label>
              <Input
                id="checkOut"
                type="datetime-local"
                value={checkOutTime}
                onChange={(e) => setCheckOutTime(e.target.value)}
                min={checkInTime || new Date().toISOString().slice(0, 16)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="notes">Special Care Notes (Optional)</Label>
              <Textarea
                id="notes"
                value={specialCareNotes}
                onChange={(e) => setSpecialCareNotes(e.target.value)}
                placeholder="Enter any special care instructions for your pet..."
                className="resize-none"
              />
            </div>

            <div className="flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => setIsBookingModalOpen(false)}
              >
                Cancel
              </Button>
              <Button
                onClick={handleBookRoom}
                className="bg-brand text-white hover:bg-brand-100"
              >
                Confirm Booking
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
};
