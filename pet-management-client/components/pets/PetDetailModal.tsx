import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Image from "next/image";
import { Pet, updatePet } from "@/lib/api/pets";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { useState, useEffect } from "react";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface PetDetailModalProps {
  pet: Pet;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onUpdate?: (updatedPet: Pet) => void;
  onDeleteClick: (pet: Pet) => void;
  initialEdit?: boolean;
  imageUrl?: string;
}

export const PetDetailModal = ({
  pet,
  open,
  onOpenChange,
  onUpdate,
  onDeleteClick,
  initialEdit = false,
  imageUrl,
}: PetDetailModalProps) => {
  const [isEditing, setIsEditing] = useState(initialEdit);
  const [editedPet, setEditedPet] = useState<Pet>(pet);
  const [isImageHovered, setIsImageHovered] = useState(false);

  // Reset editedPet and isEditing when pet changes or modal opens/closes
  useEffect(() => {
    setEditedPet(pet);
    setIsEditing(initialEdit);
  }, [pet, open, initialEdit]);

  const handleUpdate = async () => {
    try {
      const updatedPet = await updatePet(pet.petId, editedPet);
      toast.success("Pet updated successfully");
      setIsEditing(false);
      if (onUpdate) {
        onUpdate(updatedPet);
      }
    } catch (error) {
      console.error("Error updating pet:", error);
      toast.error("Failed to update pet");
    }
  };

  const handleCancel = () => {
    setEditedPet(pet); // Reset to original pet data
    setIsEditing(false);
  };

  const handleDelete = () => {
    onOpenChange(false);
    onDeleteClick(pet);
  };

  const handleFieldChange = (field: keyof Pet, value: string) => {
    setEditedPet((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  return (
    <Dialog
      open={open}
      onOpenChange={(newOpen) => {
        if (!newOpen) {
          setIsEditing(false); // Reset edit state when modal closes
        }
        onOpenChange(newOpen);
      }}
    >
      <DialogContent className="max-w-md bg-light-300 rounded-2xl pt-10 flex flex-col items-center overflow-visible fixed top-[57%] left-1/2 -translate-x-1/2 -translate-y-1/2">
        <DialogHeader>
          <DialogTitle></DialogTitle>
        </DialogHeader>
        {/* Floating pet image */}
        <div
          className="absolute left-1/2 -top-24 -translate-x-1/2 z-20"
          onMouseEnter={() => setIsImageHovered(true)}
          onMouseLeave={() => setIsImageHovered(false)}
        >
          <div className="size-52 rounded-2xl overflow-hidden bg-light-300 shadow-lg border-4 border-light-300 flex items-center justify-center relative cursor-pointer">
            <Image
              src={imageUrl || "/assets/images/default-pet.jpg"}
              alt={pet.name}
              width={200}
              height={200}
              className={cn(
                "object-cover transition-all duration-200",
                isImageHovered && "brightness-75"
              )}
              priority={false}
            />
            {isImageHovered && (
              <div className="absolute inset-0 flex items-center justify-center">
                <Pencil className="w-8 h-8 text-white" />
              </div>
            )}
          </div>
        </div>
        {/* Pet info */}
        <div className="w-full flex flex-col items-center mt-16 px-4">
          <div className="text-2xl font-bold text-center mb-2 uppercase tracking-wide">
            {isEditing ? (
              <input
                type="text"
                value={editedPet.name}
                onChange={(e) => handleFieldChange("name", e.target.value)}
                className="text-center bg-white border rounded-lg px-2 py-1"
              />
            ) : (
              pet.name
            )}
          </div>
          <div className="flex items-center justify-between w-full gap-8">
            {/* Species */}
            <div className="w-full mb-3 relative">
              <div className="text-xs font-semibold text-gray-600 mb-1">
                Species
              </div>
              <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
                {isEditing ? (
                  <input
                    type="text"
                    value={editedPet.species}
                    onChange={(e) =>
                      handleFieldChange("species", e.target.value)
                    }
                    className="w-full outline-none"
                  />
                ) : (
                  editedPet.species
                )}
                {isEditing && <Pencil className="w-4 h-4 text-gray-400" />}
              </div>
            </div>
            {/* Breed */}
            <div className="w-full mb-3 relative">
              <div className="text-xs font-semibold text-gray-500 mb-1">
                Breed
              </div>
              <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
                {isEditing ? (
                  <input
                    type="text"
                    value={editedPet.breed}
                    onChange={(e) => handleFieldChange("breed", e.target.value)}
                    className="w-full outline-none"
                  />
                ) : (
                  editedPet.breed
                )}
                {isEditing && <Pencil className="w-4 h-4 text-gray-400" />}
              </div>
            </div>
          </div>
          <div className="flex items-center justify-between w-full gap-8">
            {/* Gender */}
            <div className="w-full mb-3 relative">
              <div className="text-xs font-semibold text-gray-500 mb-1">
                Gender
              </div>
              <div className="text-sm w-full rounded-lg border bg-white text-gray-800 flex justify-between items-center">
                {isEditing ? (
                  <Select
                    value={editedPet.gender}
                    onValueChange={(value) =>
                      handleFieldChange("gender", value)
                    }
                  >
                    <SelectTrigger className="w-full outline-none border-none">
                      <SelectValue placeholder="Select Gender" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Male">Male</SelectItem>
                      <SelectItem value="Female">Female</SelectItem>
                    </SelectContent>
                  </Select>
                ) : (
                  <span className="px-4 py-2">{editedPet.gender}</span>
                )}
              </div>
            </div>
            {/* Color */}
            <div className="w-full mb-3 relative">
              <div className="text-xs font-semibold text-gray-500 mb-1">
                Color
              </div>
              <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
                {isEditing ? (
                  <input
                    type="text"
                    value={editedPet.color}
                    onChange={(e) => handleFieldChange("color", e.target.value)}
                    className="w-full outline-none"
                  />
                ) : (
                  editedPet.color
                )}
                {isEditing && <Pencil className="w-4 h-4 text-gray-400" />}
              </div>
            </div>
          </div>
          {/* Birthdate */}
          <div className="w-full mb-3 relative">
            <div className="text-xs font-semibold text-gray-500 mb-1">
              Birthdate
            </div>
            <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
              {isEditing ? (
                <input
                  type="date"
                  value={editedPet.birthdate}
                  onChange={(e) =>
                    handleFieldChange("birthdate", e.target.value)
                  }
                  className="w-full outline-none"
                />
              ) : (
                editedPet.birthdate
              )}
            </div>
          </div>
          {/* Health Notes */}
          <div className="w-full mb-3 relative">
            <div className="text-xs font-semibold text-gray-500 mb-1">
              Health Notes
            </div>
            <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
              {isEditing ? (
                <textarea
                  value={editedPet.healthNotes || ""}
                  onChange={(e) =>
                    handleFieldChange("healthNotes", e.target.value)
                  }
                  className="w-full outline-none resize-none"
                  rows={2}
                />
              ) : (
                editedPet.healthNotes || "No health notes provided"
              )}
              {isEditing && <Pencil className="w-4 h-4 text-gray-400" />}
            </div>
          </div>
          {/* Nutrition Notes */}
          <div className="w-full mb-1 relative">
            <div className="text-xs font-semibold text-gray-500 mb-1">
              Nutrition Notes
            </div>
            <div className="text-sm w-full rounded-lg border bg-white px-4 py-2 text-gray-800 flex justify-between items-center">
              {isEditing ? (
                <textarea
                  value={editedPet.nutritionNotes || ""}
                  onChange={(e) =>
                    handleFieldChange("nutritionNotes", e.target.value)
                  }
                  className="w-full outline-none resize-none"
                  rows={2}
                />
              ) : (
                editedPet.nutritionNotes || "No nutrition notes provided"
              )}
              {isEditing && <Pencil className="w-4 h-4 text-gray-400" />}
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex justify-between gap-4 w-full px-4">
          <Button
            variant="outline"
            onClick={isEditing ? handleCancel : () => setIsEditing(true)}
            className="flex items-center gap-2 cursor-pointer"
          >
            <Pencil className="w-4 h-4" />
            {isEditing ? "Cancel" : "Edit"}
          </Button>
          {isEditing ? (
            <Button
              onClick={handleUpdate}
              className="bg-brand text-white hover:bg-brand-100 cursor-pointer"
            >
              Save Changes
            </Button>
          ) : (
            <Button
              variant="outline"
              onClick={handleDelete}
              className="text-brand hover:text-white hover:bg-brand-100 border-red-600 cursor-pointer"
            >
              Delete
            </Button>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default PetDetailModal;
