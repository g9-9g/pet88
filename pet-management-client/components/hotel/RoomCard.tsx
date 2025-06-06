import { Room } from "@/lib/api/rooms";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import { useState } from "react";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { MoreHorizontal } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { RoomDetailModal } from "./RoomDetailModal";

interface RoomCardProps {
  room: Room;
  onUpdate?: (updatedRoom: Room) => void;
  onDelete?: (roomId: number) => void;
  onAvailabilityChange?: (roomId: number, available: boolean) => void;
}

const RoomCard = ({
  room,
  onUpdate,
  onDelete,
  onAvailabilityChange,
}: RoomCardProps) => {
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  const formatPrice = (price: number) => {
    if (price >= 1000000) {
      return `${(price / 1000000).toFixed(1)}M`;
    } else if (price >= 1000) {
      return `${(price / 1000).toFixed(1)}K`;
    }
    return price.toString();
  };

  const handleShowDetail = (e?: React.MouseEvent) => {
    if (e) {
      e.stopPropagation(); // Prevent card click event when clicking the button
    }
    setIsDetailModalOpen(true);
  };

  return (
    <>
      <Card
        onClick={() => handleShowDetail()}
        className="flex flex-col h-full bg-white rounded-2xl shadow-md border-0 p-4 relative select-none hover:cursor-pointer hover:bg-light-400 transition-all duration-300"
      >
        {/* Availability badge */}
        <div className="absolute -top-2 -right-2 z-10">
          <Badge
            className={cn(
              room.available ? "bg-lime-400" : "bg-red-500",
              "rounded-full px-3 py-1 font-bold tracking-wider shadow-sm text-slate-900/90"
            )}
          >
            {room.available ? "Available" : "Unavailable"}
          </Badge>
        </div>

        {/* Image */}
        <div className="flex justify-center items-center mb-0">
          <div className="relative size-36 rounded-xl overflow-hidden bg-light-300 select-none">
            <Image
              src="/assets/images/default-room.jpg"
              alt={room.description}
              fill
              className="object-cover"
              priority={false}
            />
          </div>
        </div>

        {/* Type, price */}
        <CardContent className="flex flex-col items-center gap-1">
          <div className="text-xl font-bold text-center mb-1 line-clamp-1">
            {room.type}
          </div>
          <div className="text-base text-gray-500 text-center line-clamp-2">
            {room.description}
          </div>
          <div className="text-base text-brand font-semibold text-center mt-1">
            {formatPrice(room.averageFee)} VND/night
          </div>
        </CardContent>

        <CardFooter className="flex flex-col items-center mb-1">
          <Button
            className="w-full rounded-full bg-brand hover:bg-brand-100 text-white font-semibold text-base py-2 cursor-pointer"
            onClick={handleShowDetail}
          >
            View Detail
          </Button>
        </CardFooter>
      </Card>

      <RoomDetailModal
        room={room}
        open={isDetailModalOpen}
        onOpenChange={setIsDetailModalOpen}
        onUpdate={onUpdate}
      />
    </>
  );
};

export default RoomCard;
