import { privateApi } from "./client";

export interface Medicine {
  id: number;
  name: string;
  description: string;
  unit: string;
  unitPrice: number;
}

export interface Prescription {
  id: number;
  appointmentId: number;
  medicineId: number;
  medicineName: string;
  quantity: number;
  usageInstructions: string;
}

export interface CreateMedicineDto {
  name: string;
  description: string;
  unit: string;
  unitPrice: number;
}

export interface AddMedicineToPrescriptionDto {
  medicineId: number;
  quantity: number;
  usageInstructions: string;
}

// Create a new medicine
export const createMedicine = async (
  data: CreateMedicineDto
): Promise<Medicine> => {
  const response = await privateApi.post<Medicine>(
    "/api/medical/prescriptions/medicine",
    data
  );
  return response.data;
};

// Add a single medicine to prescription
export const addMedicineToPrescription = async (
  appointmentId: number,
  data: AddMedicineToPrescriptionDto
): Promise<Prescription> => {
  const response = await privateApi.post<Prescription>(
    `/api/medical/prescriptions/appointment/${appointmentId}`,
    data
  );
  return response.data;
};

// Add multiple medicines to prescription
export const addMedicineToPrescriptionBatch = async (
  appointmentId: number,
  data: AddMedicineToPrescriptionDto[]
): Promise<Prescription[]> => {
  const response = await privateApi.post<Prescription[]>(
    `/api/medical/prescriptions/appointment/${appointmentId}/batch`,
    data
  );
  return response.data;
};

// Get medicines from an appointment
export const getMedicineFromAppointment = async (
  appointmentId: number
): Promise<Prescription[]> => {
  const response = await privateApi.get<Prescription[]>(
    `/api/medical/prescriptions/appointment/${appointmentId}`
  );
  return response.data;
};

// Get all medicines
export const getAllMedicine = async (): Promise<Medicine[]> => {
  const response = await privateApi.get<Medicine[]>(
    "/api/medical/prescriptions/medicine"
  );
  return response.data;
};
