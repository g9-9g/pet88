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
  searchPets,
  PaginatedResponse,
} from "@/lib/api/pets";
import { toast } from "sonner";
import { useUser } from "@/context/UserContext";

interface PetsContextType {
  pets: Pet[];
  loading: boolean;
  error: string | null;
  totalItems: number;
  totalPages: number;
  currentPage: number;
  fetchPets: (page?: number) => Promise<void>;
  addPet: (petData: CreatePetDto) => Promise<Pet>;
  removePet: (petId: number) => Promise<void>;
}

const PetsContext = createContext<PetsContextType | undefined>(undefined);

interface PetsProviderProps {
  children: ReactNode;
}

export const PetsProvider = ({ children }: PetsProviderProps) => {
  const { user } = useUser();
  const [pets, setPets] = useState<Pet[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchPets = useCallback(
    async (page: number = 0) => {
      try {
        setLoading(true);
        setError(null);

        if (user?.role === "ROLE_PET_OWNER") {
          const data = await getOwnedPets();
          setPets(data || []);
          setTotalItems(data?.length || 0);
          setTotalPages(1);
          setCurrentPage(0);
        } else {
          const response = await searchPets({ page });
          setPets(response?.pets || []);
          setTotalItems(response?.totalItems || 0);
          setTotalPages(response?.totalPages || 0);
          setCurrentPage(response?.currentPage || 0);
        }
      } catch (err) {
        setError("Failed to fetch pets");
        toast.error("Failed to fetch pets");
        setPets([]);
        setTotalItems(0);
        setTotalPages(0);
        setCurrentPage(0);
      } finally {
        setLoading(false);
      }
    },
    [user?.role]
  );

  const addPet = useCallback(async (petData: CreatePetDto) => {
    try {
      setLoading(true);
      setError(null);
      const newPet = await createPet(petData);
      setPets((prev) => [...prev, newPet]);
      setTotalItems((prev) => prev + 1);
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
      setTotalItems((prev) => prev - 1);
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
        totalItems,
        totalPages,
        currentPage,
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
