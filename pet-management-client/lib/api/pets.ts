import { privateApi, publicApi } from "./client";

const API_URL = process.env.NEXT_PUBLIC_API_ENDPOINT;

export interface Pet {
  petId: number;
  ownerId: number;
  name: string;
  species: string;
  breed: string;
  gender: "Male" | "Female";
  birthdate: string;
  color: string;
  avatarUrl: string;
  healthNotes?: string;
  vaccinationHistory?: string;
  nutritionNotes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreatePetDto {
  ownerId: number;
  name: string;
  species: string;
  breed: string;
  gender: "Male" | "Female";
  birthdate: string;
  color: string;
  avatarUrl?: string;
  healthNotes?: string;
  nutritionNotes?: string;
}

export const createPet = async (data: CreatePetDto): Promise<Pet> => {
  const response = await privateApi.post<Pet>("/api/pets", data);
  return response.data;
};

export const getOwnedPets = async (): Promise<Pet[]> => {
  const response = await privateApi.get<Pet[]>("/api/pets/owned");
  return response.data;
};

export const searchPets = async (params: {
  name?: string;
  species?: string;
  page?: number;
  size?: number;
}): Promise<Pet[]> => {
  const response = await publicApi.get<Pet[]>("/api/pets/search", { params });
  return response.data;
};

export const updatePet = async (
  petId: number,
  data: CreatePetDto
): Promise<Pet> => {
  const response = await privateApi.put<Pet>(`/api/pets/${petId}`, data);
  return response.data;
};

export const deletePet = async (petId: number): Promise<void> => {
  await privateApi.delete(`/api/pets/${petId}`);
};
