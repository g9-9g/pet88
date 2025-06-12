import { useState, useCallback } from "react";
import {
  OwnerReport,
  VetReport,
  StaffReport,
  getOwnerReports,
  getVetReports,
  getStaffReports,
} from "@/lib/api/reports";
import { toast } from "sonner";

export const useReports = () => {
  const [ownerReport, setOwnerReport] = useState<OwnerReport | null>(null);
  const [vetReport, setVetReport] = useState<VetReport | null>(null);
  const [staffReport, setStaffReport] = useState<StaffReport | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchOwnerReports = useCallback(async (ownerId: number) => {
    try {
      setLoading(true);
      setError(null);
      const data = await getOwnerReports(ownerId);
      setOwnerReport(data);
      return data;
    } catch (err) {
      console.error("Failed to fetch owner reports:", err);
      setError("Failed to load owner reports");
      toast.error("Failed to load owner reports");
      setOwnerReport(null);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchVetReports = useCallback(async (doctorId: number) => {
    try {
      setLoading(true);
      setError(null);
      const data = await getVetReports(doctorId);
      setVetReport(data);
      return data;
    } catch (err) {
      console.error("Failed to fetch vet reports:", err);
      setError("Failed to load vet reports");
      toast.error("Failed to load vet reports");
      setVetReport(null);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchStaffReports = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getStaffReports();
      setStaffReport(data);
      return data;
    } catch (err) {
      console.error("Failed to fetch staff reports:", err);
      setError("Failed to load staff reports");
      toast.error("Failed to load staff reports");
      setStaffReport(null);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    ownerReport,
    vetReport,
    staffReport,
    loading,
    error,
    fetchOwnerReports,
    fetchVetReports,
    fetchStaffReports,
  };
};
