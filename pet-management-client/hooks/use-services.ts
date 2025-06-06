import { useState, useCallback } from "react";
import {
  Service,
  CreateServiceDto,
  UpdateServiceDto,
  SearchServicesParams,
  getServices,
  createService,
  updateService,
  deleteService,
  searchServices,
} from "@/lib/api/services";
import { toast } from "sonner";

export const useServices = () => {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchServices = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getServices();
      setServices(data || []);
      setTotalItems(data?.length || 0);
      setTotalPages(1);
      setCurrentPage(0);
    } catch (err) {
      console.error("Failed to fetch services:", err);
      setError("Failed to load services");
      toast.error("Failed to load services");
      setServices([]);
      setTotalItems(0);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  }, []);

  const searchServicesList = useCallback(
    async (params: SearchServicesParams) => {
      try {
        setLoading(true);
        setError(null);
        const response = await searchServices(params);
        setServices(response.services || []);
        setTotalItems(response.totalItems);
        setTotalPages(response.totalPages);
        setCurrentPage(response.currentPage);
      } catch (err) {
        console.error("Failed to search services:", err);
        setError("Failed to search services");
        toast.error("Failed to search services");
        setServices([]);
        setTotalItems(0);
        setTotalPages(0);
        setCurrentPage(0);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const addService = useCallback(async (serviceData: CreateServiceDto) => {
    try {
      setLoading(true);
      setError(null);
      const newService = await createService(serviceData);
      setServices((prev) => [...prev, newService]);
      setTotalItems((prev) => prev + 1);
      toast.success("Service created successfully");
      return newService;
    } catch (err) {
      setError("Failed to create service");
      toast.error("Failed to create service");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const updateServiceById = useCallback(
    async (id: number, data: UpdateServiceDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedService = await updateService(id, data);
        setServices((prev) =>
          prev.map((service) => (service.id === id ? updatedService : service))
        );
        toast.success("Service updated successfully");
        return updatedService;
      } catch (err) {
        setError("Failed to update service");
        toast.error("Failed to update service");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const removeService = useCallback(async (id: number) => {
    try {
      setLoading(true);
      setError(null);
      await deleteService(id);
      setServices((prev) => prev.filter((service) => service.id !== id));
      setTotalItems((prev) => prev - 1);
      toast.success("Service deleted successfully");
    } catch (err) {
      setError("Failed to delete service");
      toast.error("Failed to delete service");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    services,
    loading,
    error,
    totalItems,
    totalPages,
    currentPage,
    fetchServices,
    searchServicesList,
    addService,
    updateServiceById,
    removeService,
  };
};
