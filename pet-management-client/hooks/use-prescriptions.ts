import { useState, useCallback } from "react";
import {
  Medicine,
  Prescription,
  CreateMedicineDto,
  AddMedicineToPrescriptionDto,
  createMedicine,
  addMedicineToPrescription,
  addMedicineToPrescriptionBatch,
  getMedicineFromAppointment,
  getAllMedicine,
} from "@/lib/api/prescriptions";
import { toast } from "sonner";

export const usePrescriptions = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createNewMedicine = useCallback(async (data: CreateMedicineDto) => {
    try {
      setLoading(true);
      setError(null);
      const newMedicine = await createMedicine(data);
      toast.success("Medicine created successfully");
      return newMedicine;
    } catch (err) {
      console.error("Failed to create medicine:", err);
      setError("Failed to create medicine");
      toast.error("Failed to create medicine");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const addMedicine = useCallback(
    async (appointmentId: number, data: AddMedicineToPrescriptionDto) => {
      try {
        setLoading(true);
        setError(null);
        const prescription = await addMedicineToPrescription(
          appointmentId,
          data
        );
        toast.success("Medicine added to prescription successfully");
        return prescription;
      } catch (err) {
        console.error("Failed to add medicine to prescription:", err);
        setError("Failed to add medicine to prescription");
        toast.error("Failed to add medicine to prescription");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const addMedicinesBatch = useCallback(
    async (appointmentId: number, data: AddMedicineToPrescriptionDto[]) => {
      try {
        setLoading(true);
        setError(null);
        const prescriptions = await addMedicineToPrescriptionBatch(
          appointmentId,
          data
        );
        toast.success("Medicines added to prescription successfully");
        return prescriptions;
      } catch (err) {
        console.error("Failed to add medicines to prescription:", err);
        setError("Failed to add medicines to prescription");
        toast.error("Failed to add medicines to prescription");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const getAppointmentMedicines = useCallback(async (appointmentId: number) => {
    try {
      setLoading(true);
      setError(null);
      const prescriptions = await getMedicineFromAppointment(appointmentId);
      return prescriptions;
    } catch (err) {
      console.error("Failed to fetch appointment medicines:", err);
      setError("Failed to fetch appointment medicines");
      toast.error("Failed to fetch appointment medicines");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchAllMedicines = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const medicines = await getAllMedicine();
      return medicines;
    } catch (err) {
      console.error("Failed to fetch medicines:", err);
      setError("Failed to fetch medicines");
      toast.error("Failed to fetch medicines");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    loading,
    error,
    createNewMedicine,
    addMedicine,
    addMedicinesBatch,
    getAppointmentMedicines,
    fetchAllMedicines,
  };
};
