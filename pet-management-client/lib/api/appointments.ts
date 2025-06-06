import { privateApi } from "./client";

export interface Appointment {
  id: number;
  requestId: number | null;
  petId: number;
  petName: string;
  ownerId: number;
  ownerName: string;
  doctorId: number;
  doctorName: string;
  appointmentDateTime: string;
  symptoms: string | null;
  diagnosis: string | null;
  treatment: string | null;
  notes: string | null;
  status: "COMPLETED" | "SCHEDULED" | "CANCELLED" | "FOLLOW_UP";
  createdAt: string;
  updatedAt: string;
}

export interface CreateAppointmentDto {
  petId: number;
  appointmentDateTime: string;
  diagnosis?: string | null;
  treatment?: string | null;
  notes?: string | null;
}

export interface UpdateAppointmentDto {
  id?: number;
  diagnosis?: string;
  treatment?: string;
  notes?: string;
  status?: "SCHEDULED" | "COMPLETED" | "CANCELLED" | "FOLLOW_UP";
  completed?: boolean;
  appointmentDateTime?: string;
}

export interface GetAppointmentsParams {
  status?: "ALL" | "COMPLETED" | "SCHEDULED" | "CANCELLED" | "FOLLOW_UP";
}

// Get appointments for pet owner
export const getOwnerAppointments = async (
  params?: GetAppointmentsParams
): Promise<Appointment[]> => {
  const filteredParams = {
    ...params,
    status: params?.status === "ALL" ? undefined : params?.status,
  };
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments/owner",
    { params: filteredParams }
  );
  return response.data;
};

// Get appointments for doctor
export const getDoctorAppointments = async (
  params?: GetAppointmentsParams
): Promise<Appointment[]> => {
  const filteredParams = {
    ...params,
    status: params?.status === "ALL" ? undefined : params?.status,
  };
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments/doctor",
    { params: filteredParams }
  );
  return response.data;
};

// Get all appointments (public)
export const getAllAppointments = async (
  params?: GetAppointmentsParams
): Promise<Appointment[]> => {
  const filteredParams = {
    ...params,
    status: params?.status === "ALL" ? undefined : params?.status,
  };
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments",
    { params: filteredParams }
  );
  return response.data;
};

// Get completed appointments for a pet
export const getCompletedAppointments = async (
  ownerId: number
): Promise<Appointment[]> => {
  const response = await privateApi.get<Appointment[]>(
    `/api/pets/completed-appointments`,
    {
      params: { ownerId },
    }
  );
  return response.data;
};

// Create a new appointment
export const createAppointment = async (
  data: CreateAppointmentDto
): Promise<Appointment> => {
  const response = await privateApi.post<Appointment>(
    "/api/medical/appointments",
    data
  );
  return response.data;
};

// Update an appointment
export const updateAppointment = async (
  id: number,
  data: UpdateAppointmentDto
): Promise<Appointment> => {
  const response = await privateApi.put<Appointment>(
    `/api/medical/appointments/${id}`,
    data
  );
  return response.data;
};
