"use client";

import React, {
  createContext,
  useContext,
  useState,
  ReactNode,
  useCallback,
} from "react";
import {
  Pet,
  CreatePetDto,
  createPet,
  getOwnedPets,
  deletePet,
} from "@/lib/api/pets";
import { toast } from "sonner";

interface PetsContextType {
  pets: Pet[];
  loading: boolean;
  error: string | null;
  fetchPets: () => Promise<void>;
  addPet: (petData: CreatePetDto) => Promise<Pet>;
  removePet: (petId: number) => Promise<void>;
}

const PetsContext = createContext<PetsContextType | undefined>(undefined);

interface PetsProviderProps {
  children: ReactNode;
}

export const PetsProvider = ({ children }: PetsProviderProps) => {
  const [pets, setPets] = useState<Pet[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchPets = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getOwnedPets();
      setPets(data);
    } catch (err) {
      setError("Failed to fetch pets");
      toast.error("Failed to fetch pets");
    } finally {
      setLoading(false);
    }
  }, []);

  const addPet = useCallback(async (petData: CreatePetDto) => {
    try {
      setLoading(true);
      setError(null);
      const newPet = await createPet(petData);
      setPets((prev) => [...prev, newPet]);
      toast.success("Pet created successfully");
      return newPet;
    } catch (err) {
      setError("Failed to create pet");
      toast.error("Failed to create pet");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const removePet = useCallback(async (petId: number) => {
    try {
      setLoading(true);
      setError(null);
      await deletePet(petId);
      setPets((prev) => prev.filter((pet) => pet.petId !== petId));
      toast.success("Pet deleted successfully");
    } catch (err) {
      setError("Failed to delete pet");
      toast.error("Failed to delete pet");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return (
    <PetsContext.Provider
      value={{
        pets,
        loading,
        error,
        fetchPets,
        addPet,
        removePet,
      }}
    >
      {children}
    </PetsContext.Provider>
  );
};

export const usePets = (): PetsContextType => {
  const context = useContext(PetsContext);
  if (context === undefined) {
    throw new Error("usePets must be used within a PetsProvider");
  }
  return context;
};
