import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import { Pet } from "@/lib/api/pets";

interface DeletePetModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onConfirm: () => void;
  pet: Pet;
}

export const DeletePetModal = ({
  open,
  onOpenChange,
  onConfirm,
  pet,
}: DeletePetModalProps) => {
  const imageUrl = "/assets/images/default-pet.jpg";

  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure to delete this pet?</AlertDialogTitle>
          <div className="flex items-center gap-4 my-4">
            <div className="relative size-16 rounded-xl overflow-hidden bg-light-300">
              <Image
                src={imageUrl}
                alt={pet.name}
                fill
                className="object-cover"
                priority={false}
              />
            </div>
            <div>
              <p className="font-semibold text-lg">{pet.name}</p>
              <p className="text-sm text-gray-500">
                {pet.species} ・ {pet.breed}
              </p>
            </div>
          </div>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete your pet
            and remove this pet data from our servers.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            className="bg-brand text-white hover:bg-brand-100 cursor-pointer"
            onClick={onConfirm}
          >
            Continue
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
