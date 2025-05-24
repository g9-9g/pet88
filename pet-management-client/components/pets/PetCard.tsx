import { Card, CardContent, CardFooter } from "@/components/ui/card";
import Image from "next/image";
import { Pet } from "@/lib/api/pets";
import { Button } from "@/components/ui/button";
import { MoreHorizontal } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useState } from "react";
import PetDetailModal from "./PetDetailModal";

interface PetCardProps {
  pet: Pet;
  onUpdate?: () => void;
  onDeleteClick: (pet: Pet) => void;
}

export const PetCard = ({ pet, onUpdate, onDeleteClick }: PetCardProps) => {
  const [showDetail, setShowDetail] = useState(false);
  const [initialEdit, setInitialEdit] = useState(false);
  const imageUrl = "/assets/images/default-pet.jpg";

  const handleShowDetail = (editMode = false) => {
    setInitialEdit(editMode);
    setShowDetail(true);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation(); // Prevent card click event
    onDeleteClick(pet);
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
              src={imageUrl}
              alt={pet.name}
              fill
              className="object-contain"
              priority={false}
            />
          </div>
        </div>
        {/* Name, species, breed */}
        <CardContent className="flex flex-col items-center gap-1">
          <div className="text-3xl font-bold text-center mb-1 line-clamp-1">
            {pet.name}
          </div>
          <div className="text-base text-gray-500 text-center line-clamp-1">
            {pet.species} ・ {pet.breed}
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
      <PetDetailModal
        pet={pet}
        open={showDetail}
        onOpenChange={setShowDetail}
        onUpdate={onUpdate}
        onDeleteClick={onDeleteClick}
        initialEdit={initialEdit}
      />
    </>
  );
};

export default PetCard;
