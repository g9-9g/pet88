import { useState, useCallback } from "react";
import {
  Appointment,
  CreateAppointmentDto,
  UpdateAppointmentDto,
  GetAppointmentsParams,
  getOwnerAppointments,
  getDoctorAppointments,
  getAllAppointments,
  createAppointment,
  updateAppointment,
} from "@/lib/api/appointments";
import { toast } from "sonner";
import { useUser } from "@/context/UserContext";

export const useAppointments = () => {
  const { user } = useUser();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchAppointments = useCallback(
    async (status: GetAppointmentsParams["status"] = "ALL") => {
      if (!user) return;

      try {
        setLoading(true);
        setError(null);
        let data: Appointment[];

        const params: GetAppointmentsParams = { status };

        switch (user.role) {
          case "ROLE_PET_OWNER":
            data = await getOwnerAppointments(params);
            break;
          case "ROLE_VET":
            data = await getDoctorAppointments(params);
            break;
          case "ROLE_STAFF":
          case "ROLE_ADMIN":
            data = await getAllAppointments(params);
            break;
          default:
            data = [];
        }

        setAppointments(data);
      } catch (err) {
        console.error("Failed to fetch appointments:", err);
        setError("Failed to load appointments");
        toast.error("Failed to load appointments");
      } finally {
        setLoading(false);
      }
    },
    [user]
  );

  const addAppointment = useCallback(
    async (appointmentData: CreateAppointmentDto) => {
      try {
        setLoading(true);
        setError(null);
        const newAppointment = await createAppointment(appointmentData);
        setAppointments((prev) => [...prev, newAppointment]);
        toast.success("Appointment created successfully");
        return newAppointment;
      } catch (err) {
        setError("Failed to create appointment");
        toast.error("Failed to create appointment");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const updateAppointmentStatus = useCallback(
    async (id: number, data: UpdateAppointmentDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedAppointment = await updateAppointment(id, data);
        setAppointments((prev) =>
          prev.map((appointment) =>
            appointment.id === id ? updatedAppointment : appointment
          )
        );
        toast.success("Appointment updated successfully");
        return updatedAppointment;
      } catch (err) {
        setError("Failed to update appointment");
        toast.error("Failed to update appointment");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return {
    appointments,
    loading,
    error,
    fetchAppointments,
    addAppointment,
    updateAppointmentStatus,
  };
};
