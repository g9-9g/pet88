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
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { RoomDetailModal } from "./RoomDetailModal";
import UpdateRoomModal from "./UpdateRoomModal";
import { useUser } from "@/context/UserContext";
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
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const { user } = useUser();

  const formatPrice = (price: number) => {
    if (price >= 1000000) {
      return `${(price / 1000000).toFixed(1)}M`;
    } else if (price >= 1000) {
      return `${(price / 1000).toFixed(1)}K`;
    }
    return price.toString();
  };

  const handleShowDetail = (editMode = false) => {
    if (editMode) {
      setIsUpdateModalOpen(true);
    } else {
      setIsDetailModalOpen(true);
    }
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation(); // Prevent card click event
    setIsDeleteDialogOpen(true);
  };

  const handleConfirmDelete = () => {
    if (onDelete) {
      onDelete(room.id);
    }
    setIsDeleteDialogOpen(false);
  };

  const handleAvailabilityChange = (e: React.MouseEvent) => {
    e.stopPropagation(); // Prevent card click event
    if (onAvailabilityChange) {
      onAvailabilityChange(room.id, !room.available);
    }
  };

  return (
    <>
      <Card
        onClick={() => handleShowDetail(false)}
        className="flex flex-col h-full bg-white rounded-2xl shadow-md border-0 p-4 relative select-none hover:cursor-pointer hover:bg-light-400 transition-all duration-300"
      >
        {/* Three dots menu for admin */}
        {user?.role === "ROLE_ADMIN" && (
          <div className="absolute top-4 right-4 z-10">
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="ghost"
                  size="icon"
                  className="rounded-full active:none cursor-pointer"
                  onClick={(e) => e.stopPropagation()} // Prevent card click event
                >
                  <MoreHorizontal className="size-5" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent
                align="center"
                className="w-48 bg-white font-semibold"
              >
                <DropdownMenuItem
                  onClick={(e) => {
                    e.stopPropagation(); // Prevent card click event
                    handleShowDetail(true);
                  }}
                  className="cursor-pointer hover:bg-light-400"
                >
                  Update
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={handleAvailabilityChange}
                  className={cn(
                    "cursor-pointer hover:bg-light-400",
                    room.available ? "text-red-500" : "text-green-500"
                  )}
                >
                  {room.available ? "Make Unavailable" : "Make Available"}
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem
                  onClick={handleDelete}
                  className="text-red-500 cursor-pointer hover:bg-light-400"
                >
                  Delete
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        )}

        {/* Availability badge */}
        <div className="absolute -top-2 -right-2 z-10">
          <Badge
            className={cn(
              room.available ? "bg-lime-400" : "bg-red-500",
              "rounded-full px-3 py-1 font-bold tracking-wider shadow-sm text-white"
            )}
          >
            {room.available ? "AVAILABLE" : "UNAVAILABLE"}
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
            onClick={(e) => {
              e.stopPropagation(); // Prevent card click event
              handleShowDetail(false);
            }}
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

      <UpdateRoomModal
        room={room}
        isOpen={isUpdateModalOpen}
        onClose={() => setIsUpdateModalOpen(false)}
        onSubmit={onUpdate}
      />

      <AlertDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
      >
        <AlertDialogContent className="bg-white border-none rounded-2xl shadow-md">
          <AlertDialogHeader>
            <AlertDialogTitle className="text-2xl font-bold">
              Delete Room
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this room? This action cannot be
              undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="border-none">
              No, keep it
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleConfirmDelete}
              className="bg-red-500 hover:bg-red-600 text-white"
            >
              Yes, delete room
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default RoomCard;
