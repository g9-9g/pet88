import { Service } from "@/lib/api/services";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import { useState } from "react";
import { ServiceDetailModal } from "./ServiceDetailModal";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { MoreHorizontal } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import UpdateServiceModal from "./UpdateServiceModal";
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

interface ServiceCardProps {
  service: Service;
  onUpdate?: (updatedService: Service) => void;
  onDelete?: (serviceId: number) => void;
}

const ServiceCard = ({ service, onUpdate, onDelete }: ServiceCardProps) => {
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

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
      onDelete(service.id);
    }
    setIsDeleteDialogOpen(false);
  };

  return (
    <>
      <Card
        onClick={() => handleShowDetail(false)}
        className="flex flex-col h-full bg-white rounded-2xl shadow-md border-0 p-4 relative select-none hover:cursor-pointer hover:bg-light-400 transition-all duration-300"
      >
        {/* Three dots menu */}
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
              className="w-32 bg-white font-semibold"
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
                onClick={handleDelete}
                className="text-red-500 cursor-pointer hover:bg-light-400"
              >
                Delete
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>

        {/* Image */}
        <div className="flex justify-center items-center mb-0">
          <div className="relative size-36 rounded-xl overflow-hidden bg-light-300 select-none">
            <Image
              src={service.imageUrl || "/assets/images/default-service.png"}
              alt={service.name}
              fill
              className="object-contain"
              priority={false}
            />
          </div>
        </div>

        {/* Name, price, duration */}
        <CardContent className="flex flex-col items-center gap-1">
          <div className="text-xl font-bold text-center mb-1 line-clamp-1">
            {service.name}
          </div>
          <div className="text-base text-gray-500 text-center line-clamp-1">
            {formatPrice(service.price)} VND ・ {service.durationMinutes} mins
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

      <ServiceDetailModal
        service={service}
        open={isDetailModalOpen}
        onOpenChange={setIsDetailModalOpen}
        onUpdate={onUpdate}
      />

      <UpdateServiceModal
        service={service}
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
              Delete Service
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this service? This action cannot
              be undone.
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
              Yes, delete service
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default ServiceCard;
