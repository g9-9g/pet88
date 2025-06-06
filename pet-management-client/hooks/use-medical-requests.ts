import { useState, useCallback } from "react";
import {
  MedicalRequest,
  CreateMedicalRequestDto,
  HandleRequestDto,
  createMedicalRequest,
  getMedicalRequests,
  deleteMedicalRequest,
  GetMedicalRequestsParams,
  handleMedicalRequest,
} from "@/lib/api/medical-requests";
import { toast } from "sonner";
import { useUser } from "@/context/UserContext";

export const useMedicalRequests = () => {
  const { user } = useUser();
  const [requests, setRequests] = useState<MedicalRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRequests = useCallback(
    async (
      status: GetMedicalRequestsParams["medicalRequestStatus"] = "ALL"
    ) => {
      try {
        setLoading(true);
        setError(null);
        const params: GetMedicalRequestsParams = {
          medicalRequestStatus: status,
        };

        // Only add ownerId param for pet owners
        if (user?.role === "ROLE_PET_OWNER") {
          params.ownerId = user.id;
        }

        const data = await getMedicalRequests(params);
        setRequests(data);
      } catch (err) {
        setError("Failed to fetch medical requests");
        toast.error("Failed to fetch medical requests");
      } finally {
        setLoading(false);
      }
    },
    [user]
  );

  const addRequest = useCallback(
    async (requestData: CreateMedicalRequestDto) => {
      try {
        setLoading(true);
        setError(null);
        const newRequest = await createMedicalRequest(requestData);
        setRequests((prev) => [...prev, newRequest]);
        toast.success("Medical request created successfully");
        return newRequest;
      } catch (err) {
        setError("Failed to create medical request");
        toast.error("Failed to create medical request");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const removeRequest = useCallback(async (id: number) => {
    try {
      setLoading(true);
      setError(null);
      await deleteMedicalRequest(id);
      setRequests((prev) => prev.filter((request) => request.id !== id));
      toast.success("Medical request deleted successfully");
    } catch (err) {
      setError("Failed to delete medical request");
      toast.error("Failed to delete medical request");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const handleRequest = useCallback(
    async (requestId: number, data: HandleRequestDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRequest = await handleMedicalRequest(requestId, data);
        setRequests((prev) =>
          prev.map((request) =>
            request.id === requestId ? updatedRequest : request
          )
        );
        toast.success(
          `Medical request ${data.status.toLowerCase()} successfully`
        );
      } catch (err) {
        setError(`Failed to ${data.status.toLowerCase()} medical request`);
        toast.error(`Failed to ${data.status.toLowerCase()} medical request`);
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return {
    requests,
    loading,
    error,
    fetchRequests,
    addRequest,
    removeRequest,
    handleRequest,
  };
};
