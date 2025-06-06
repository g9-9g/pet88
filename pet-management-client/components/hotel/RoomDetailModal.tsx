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
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  return (
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
                  "rounded-full px-3 py-1 font-bold tracking-wider text-slate-900/90"
                )}
              >
                {room.available ? "Available" : "Unavailable"}
              </Badge>
            </div>
          </div>

          <Button
            onClick={() => onOpenChange(false)}
            className="w-full bg-brand text-white hover:bg-brand-100"
          >
            Close
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};
