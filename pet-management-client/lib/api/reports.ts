import { privateApi } from "./client";

interface StatusCounts {
  [key: string]: number;
}

interface CountWithStatus {
  total: number;
  statusCounts: StatusCounts;
}

export interface OwnerReport {
  petCount: number;
  medicalRequestCount: CountWithStatus;
  appointmentCount: CountWithStatus;
  groomingRequestCount: CountWithStatus;
  booking: CountWithStatus;
}

export interface VetReport {
  totalPets: number;
  appointmentCount: CountWithStatus;
}

export interface StaffReport {
  petCount: number;
  medicalRequestCount: CountWithStatus;
  appointmentCount: CountWithStatus;
  groomingRequestCount: CountWithStatus;
  booking: CountWithStatus;
  serviceCount: number;
  roomCount: {
    total: number;
    statusCounts: {
      SUITE: number;
      LUXURY: number;
      STANDARD: number;
      DELUXE: number;
    };
  };
}

// Get owner reports
export const getOwnerReports = async (
  ownerId: number
): Promise<OwnerReport> => {
  const response = await privateApi.get<OwnerReport>("/api/reports/owner", {
    params: { id: ownerId },
  });
  return response.data;
};

// Get vet reports
export const getVetReports = async (doctorId: number): Promise<VetReport> => {
  const response = await privateApi.get<VetReport>("/api/reports/doctor", {
    params: { id: doctorId },
  });
  return response.data;
};

// Get staff reports
export const getStaffReports = async (): Promise<StaffReport> => {
  const response = await privateApi.get<StaffReport>("/api/reports/staff");
  return response.data;
};
