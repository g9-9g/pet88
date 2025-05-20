"use client";

import { useUser } from "@/context/UserContext";
import { CreatePetModal } from "@/components/pets/CreatePetModal";
import { Toaster } from "sonner";
import { usePets } from "@/hooks/use-pets";
import { useEffect } from "react";

const PetsPage = () => {
  const { user } = useUser();
  console.log(user);
  const { pets, loading, error, fetchPets } = usePets();

  useEffect(() => {
    if (user) {
      fetchPets();
    }
  }, [user, fetchPets]);

  const handleSuccess = () => {
    fetchPets();
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Pets</h1>
        {user && <CreatePetModal ownerId={user.id} onSuccess={handleSuccess} />}
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {pets.map((pet) => (
          <div key={pet.petId} className="p-4 border rounded-lg">
            <h3 className="text-lg font-semibold">{pet.name}</h3>
            <p>Species: {pet.species}</p>
            <p>Breed: {pet.breed}</p>
            <p>Gender: {pet.gender}</p>
          </div>
        ))}
      </div>

      <Toaster />
    </div>
  );
};

export default PetsPage;
