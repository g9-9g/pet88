import { privateApi } from "./client";

export type RequestStatus =
  | "PENDING" // Initial state when request is created
  | "APPROVED" // Request is approved by staff
  | "REJECTED" // Request is rejected by staff
  | "SCHEDULED" // Approved and scheduled for a specific time
  | "IN_PROGRESS" // Service is currently being performed
  | "COMPLETED" // Service has been completed
  | "CANCELLED"; // Request was cancelled by the pet owner

export interface GroomingRequest {
  id: number;
  serviceId: number;
  petId: number;
  ownerId: number;
  staffId: number | null;
  requestedDateTime: string;
  scheduledDateTime: string | null;
  status: RequestStatus;
  notes: string | null;
  staffNotes: string | null;
  completedDateTime: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateRequestDto {
  serviceId: number;
  petId: number;
  requestedDateTime: string;
  notes?: string;
}

export interface UpdateRequestDto {
  status?: RequestStatus;
  scheduledDateTime?: string;
  staffNotes?: string;
}

export interface SearchRequestsParams {
  ownerId?: number;
  petId?: number;
  serviceId?: number;
  staffId?: number;
  status?: RequestStatus;
  page?: number;
  size?: number;
}

export interface PaginatedRequestsResponse {
  totalItems: number;
  totalPages: number;
  requests: GroomingRequest[];
  currentPage: number;
}

// Get all requests (admin only)
export const getRequests = async (): Promise<GroomingRequest[]> => {
  const response = await privateApi.get<GroomingRequest[]>(
    "/api/grooming/requests"
  );
  return response.data;
};

// Get my requests (for logged-in user)
export const getMyRequests = async (): Promise<GroomingRequest[]> => {
  const response = await privateApi.get<GroomingRequest[]>(
    "/api/grooming/requests/my-requests"
  );
  return response.data;
};

// Get requests for a specific pet
export const getRequestsOfPet = async (
  petId: number
): Promise<GroomingRequest[]> => {
  const response = await privateApi.get<GroomingRequest[]>(
    `/api/grooming/requests/pet/${petId}`
  );
  return response.data;
};

// Create a new request
export const createRequest = async (
  data: CreateRequestDto
): Promise<GroomingRequest> => {
  const response = await privateApi.post<GroomingRequest>(
    "/api/grooming/requests",
    data
  );
  return response.data;
};

// Update a request
export const updateRequest = async (
  id: number,
  data: UpdateRequestDto
): Promise<GroomingRequest> => {
  const response = await privateApi.put<GroomingRequest>(
    `/api/grooming/requests/${id}`,
    data
  );
  return response.data;
};

// Search requests
export const searchRequests = async (
  params: SearchRequestsParams
): Promise<PaginatedRequestsResponse> => {
  const response = await privateApi.get<PaginatedRequestsResponse>(
    "/api/grooming/requests/search",
    { params }
  );
  return response.data;
};
