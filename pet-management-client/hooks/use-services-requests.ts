import { useState, useCallback } from "react";
import {
  GroomingRequest,
  CreateRequestDto,
  UpdateRequestDto,
  SearchRequestsParams,
  getRequests,
  getMyRequests,
  getRequestsOfPet,
  createRequest,
  updateRequest,
  searchRequests,
} from "@/lib/api/services-requests";
import { toast } from "sonner";

export const useServicesRequests = () => {
  const [requests, setRequests] = useState<GroomingRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchRequests = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getRequests();
      setRequests(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch requests:", err);
      setError("Failed to load requests");
      toast.error("Failed to load requests");
      setRequests([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchMyRequests = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getMyRequests();
      setRequests(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch my requests:", err);
      setError("Failed to load my requests");
      toast.error("Failed to load my requests");
      setRequests([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchRequestsOfPet = useCallback(async (petId: number) => {
    try {
      setLoading(true);
      setError(null);
      const data = await getRequestsOfPet(petId);
      setRequests(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch pet requests:", err);
      setError("Failed to load pet requests");
      toast.error("Failed to load pet requests");
      setRequests([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const addRequest = useCallback(async (requestData: CreateRequestDto) => {
    try {
      setLoading(true);
      setError(null);
      const newRequest = await createRequest(requestData);
      setRequests((prev) => [...prev, newRequest]);
      setTotalItems((prev) => prev + 1);
      toast.success("Request created successfully");
      return newRequest;
    } catch (err) {
      setError("Failed to create request");
      toast.error("Failed to create request");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const updateRequestById = useCallback(
    async (id: number, data: UpdateRequestDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedRequest = await updateRequest(id, data);
        setRequests((prev) =>
          prev.map((request) => (request.id === id ? updatedRequest : request))
        );
        toast.success("Request updated successfully");
        return updatedRequest;
      } catch (err) {
        setError("Failed to update request");
        toast.error("Failed to update request");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const searchRequestsList = useCallback(
    async (params: SearchRequestsParams) => {
      try {
        setLoading(true);
        setError(null);
        const response = await searchRequests(params);
        setRequests(response.requests || []);
        setTotalItems(response.totalItems);
        setTotalPages(response.totalPages);
        setCurrentPage(response.currentPage);
      } catch (err) {
        console.error("Failed to search requests:", err);
        setError("Failed to search requests");
        toast.error("Failed to search requests");
        setRequests([]);
        setTotalItems(0);
        setTotalPages(0);
        setCurrentPage(0);
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
    totalItems,
    totalPages,
    currentPage,
    fetchRequests,
    fetchMyRequests,
    fetchRequestsOfPet,
    addRequest,
    updateRequestById,
    searchRequestsList,
  };
};
