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
  completed: boolean;
  cancelled: boolean;
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
  diagnosis?: string;
  treatment?: string;
  notes?: string;
  status?: "COMPLETED" | "CANCELLED";
  completed?: boolean;
}

// Get appointments for pet owner
export const getOwnerAppointments = async (): Promise<Appointment[]> => {
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments/owner"
  );
  return response.data;
};

// Get appointments for doctor
export const getDoctorAppointments = async (): Promise<Appointment[]> => {
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments/doctor"
  );
  return response.data;
};

// Get all appointments (public)
export const getAllAppointments = async (): Promise<Appointment[]> => {
  const response = await privateApi.get<Appointment[]>(
    "/api/medical/appointments"
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
