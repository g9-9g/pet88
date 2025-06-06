import { privateApi } from "./client";

export interface Service {
  id: number;
  name: string;
  description: string;
  price: number;
  durationMinutes: number;
  isActive: boolean;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateServiceDto {
  name: string;
  description: string;
  price: number;
  durationMinutes: number;
  isActive: boolean;
  imageUrl: string;
}

export interface UpdateServiceDto {
  name: string;
  description: string;
  price: number;
  durationMinutes: number;
  isActive: boolean;
  imageUrl: string;
}

export interface SearchServicesParams {
  name?: string;
  minPrice?: number;
  maxPrice?: number;
  isActive?: boolean;
  sortBy?: "name" | "price" | "duration";
  sortDir?: "asc" | "desc";
  page?: number;
  size?: number;
}

export interface PaginatedServicesResponse {
  totalItems: number;
  totalPages: number;
  services: Service[];
  currentPage: number;
}

// Get all services
export const getServices = async (): Promise<Service[]> => {
  const response = await privateApi.get<Service[]>("/api/grooming/services");
  return response.data;
};

// Create a new service
export const createService = async (
  data: CreateServiceDto
): Promise<Service> => {
  const response = await privateApi.post<Service>(
    "/api/grooming/services",
    data
  );
  return response.data;
};

// Update a service
export const updateService = async (
  id: number,
  data: UpdateServiceDto
): Promise<Service> => {
  const response = await privateApi.put<Service>(
    `/api/grooming/services/${id}`,
    data
  );
  return response.data;
};

// Delete a service
export const deleteService = async (id: number): Promise<void> => {
  await privateApi.delete(`/api/grooming/services/${id}`);
};

// Search services
export const searchServices = async (
  params: SearchServicesParams
): Promise<PaginatedServicesResponse> => {
  const response = await privateApi.get<PaginatedServicesResponse>(
    "/api/grooming/services/search",
    { params }
  );
  return response.data;
};
