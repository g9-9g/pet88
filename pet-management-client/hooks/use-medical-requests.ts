import { useState, useCallback } from "react";
import {
  MedicalRequest,
  CreateMedicalRequestDto,
  createMedicalRequest,
  getMedicalRequests,
  deleteMedicalRequest,
  GetMedicalRequestsParams,
  acceptMedicalRequest,
  rejectMedicalRequest,
} from "@/lib/api/medical-requests";
import { toast } from "sonner";
import { useUser } from "@/context/UserContext";

export const useMedicalRequests = () => {
  const { user } = useUser();
  const [requests, setRequests] = useState<MedicalRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRequests = useCallback(
    async (status: GetMedicalRequestsParams["status"] = "ALL") => {
      try {
        setLoading(true);
        setError(null);
        const params: GetMedicalRequestsParams = {
          status,
        };

        // Only add ownerName param for pet owners
        if (user?.role === "ROLE_PET_OWNER") {
          params.ownerName = user.name;
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

  const handleAcceptRequest = useCallback(
    async (requestId: number, doctorId: number) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRequest = await acceptMedicalRequest(requestId, doctorId);
        setRequests((prev) =>
          prev.map((request) =>
            request.id === requestId ? updatedRequest : request
          )
        );
        toast.success("Medical request accepted successfully");
      } catch (err) {
        setError("Failed to accept medical request");
        toast.error("Failed to accept medical request");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const handleRejectRequest = useCallback(
    async (requestId: number, rejectionReason: string) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRequest = await rejectMedicalRequest(
          requestId,
          rejectionReason
        );
        setRequests((prev) =>
          prev.map((request) =>
            request.id === requestId ? updatedRequest : request
          )
        );
        toast.success("Medical request rejected successfully");
      } catch (err) {
        setError("Failed to reject medical request");
        toast.error("Failed to reject medical request");
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
    handleAcceptRequest,
    handleRejectRequest,
  };
};
