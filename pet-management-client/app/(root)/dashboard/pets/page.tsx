"use client";

import { useUser } from "@/context/UserContext";
import { CreatePetModal } from "@/components/pets/CreatePetModal";
import { usePets } from "@/context/PetsContext";
import { useEffect, useState } from "react";
import PetCard from "@/components/pets/PetCard";
import { DeletePetModal } from "@/components/pets/DeletePetModal";
import { Pet } from "@/lib/api/pets";

const PetsPage = () => {
  const { user } = useUser();
  const { pets, loading, error, fetchPets, removePet } = usePets();
  const [petToDelete, setPetToDelete] = useState<Pet | null>(null);

  useEffect(() => {
    if (user) {
      fetchPets();
    }
  }, [user, fetchPets]);

  const handleSuccess = () => {
    fetchPets();
  };

  const handleDeleteClick = (pet: Pet) => {
    setPetToDelete(pet);
  };

  const handleDeleteConfirm = async () => {
    if (petToDelete) {
      await removePet(petToDelete.petId);
      setPetToDelete(null);
    }
  };

  console.log(pets[0]);

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Pets</h1>
        {user && <CreatePetModal ownerId={user.id} onSuccess={handleSuccess} />}
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {pets.map((pet) => (
          <PetCard
            key={pet.petId}
            pet={pet}
            onUpdate={handleSuccess}
            onDeleteClick={handleDeleteClick}
          />
        ))}
      </div>

      {petToDelete && (
        <DeletePetModal
          pet={petToDelete}
          open={!!petToDelete}
          onOpenChange={(open) => !open && setPetToDelete(null)}
          onConfirm={handleDeleteConfirm}
        />
      )}
    </div>
  );
};

export default PetsPage;
