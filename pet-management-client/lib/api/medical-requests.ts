import { privateApi } from "./client";

export interface MedicalRequest {
  id: number;
  petId: number;
  petName: string;
  ownerId: number;
  ownerName: string;
  updatedById: number | null;
  updatedByName: string | null;
  symptoms: string;
  notes: string;
  preferredDateTime: string;
  status: "PENDING" | "ACCEPTED" | "REJECTED";
  rejectionReason: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateMedicalRequestDto {
  petId: number;
  symptoms: string;
  notes: string;
  preferredDateTime: string;
}

export interface GetMedicalRequestsParams {
  ownerId?: number;
  medicalRequestStatus?: "ALL" | "PENDING" | "ACCEPTED" | "REJECTED";
}

export interface HandleRequestDto {
  status: "ACCEPTED" | "REJECTED";
  doctorId?: number;
  rejectionReason?: string;
}

export const createMedicalRequest = async (
  data: CreateMedicalRequestDto
): Promise<MedicalRequest> => {
  const response = await privateApi.post<MedicalRequest>(
    "/api/medical/requests",
    data
  );
  return response.data;
};

export const getMedicalRequests = async (
  params?: GetMedicalRequestsParams
): Promise<MedicalRequest[]> => {
  const filteredParams = {
    ...params,
    medicalRequestStatus:
      params?.medicalRequestStatus === "ALL"
        ? undefined
        : params?.medicalRequestStatus,
  };

  const response = await privateApi.get<MedicalRequest[]>(
    "/api/medical/requests",
    { params: filteredParams }
  );
  return response.data;
};

export const getMedicalRequest = async (
  id: number
): Promise<MedicalRequest> => {
  const response = await privateApi.get<MedicalRequest>(
    `/api/medical/requests/${id}`
  );
  return response.data;
};

export const updateMedicalRequest = async (
  id: number,
  data: Partial<CreateMedicalRequestDto>
): Promise<MedicalRequest> => {
  const response = await privateApi.put<MedicalRequest>(
    `/api/medical/requests/${id}`,
    data
  );
  return response.data;
};

export const deleteMedicalRequest = async (id: number): Promise<void> => {
  await privateApi.delete(`/api/medical/requests/${id}`);
};

export const handleMedicalRequest = async (
  requestId: number,
  data: HandleRequestDto
): Promise<MedicalRequest> => {
  const response = await privateApi.post<MedicalRequest>(
    `/api/medical/requests/${requestId}/handle`,
    data
  );
  return response.data;
};
