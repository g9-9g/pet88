"use client";

import { useServices } from "@/hooks/use-services";
import { useEffect, useState } from "react";
import ServiceCard from "@/components/services/ServiceCard";
import { Button } from "@/components/ui/button";
import { FaClipboardCheck, FaPlus } from "react-icons/fa";
import { useRouter } from "next/navigation";
import { useUser } from "@/context/UserContext";
import AddServiceModal from "@/components/services/AddServiceModal";
import { toast } from "sonner";
import { Service } from "@/lib/api/services";

const ServicesPage = () => {
  const {
    services,
    loading,
    error,
    fetchServices,
    addService,
    updateServiceById,
    removeService,
  } = useServices();
  const router = useRouter();
  const { user } = useUser();
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);

  useEffect(() => {
    fetchServices();
  }, [fetchServices]);

  const handleAddService = async (data: {
    name: string;
    description: string;
    price: number;
    durationMinutes: number;
  }) => {
    try {
      await addService({
        ...data,
        isActive: true,
        imageUrl: "/assets/images/default-service.png",
      });
      setIsAddModalOpen(false);
      toast.success("Service added successfully");
    } catch (error) {
      console.error("Failed to add service:", error);
      toast.error("Failed to add service");
    }
  };

  const handleUpdateService = async (updatedService: Service) => {
    try {
      await updateServiceById(updatedService.id, {
        name: updatedService.name,
        description: updatedService.description,
        price: updatedService.price,
        durationMinutes: updatedService.durationMinutes,
        isActive: updatedService.isActive,
        imageUrl: updatedService.imageUrl,
      });
      toast.success("Service updated successfully");
    } catch (error) {
      console.error("Failed to update service:", error);
      toast.error("Failed to update service");
    }
  };

  const handleDeleteService = async (serviceId: number) => {
    try {
      await removeService(serviceId);
      toast.success("Service deleted successfully");
    } catch (error) {
      console.error("Failed to delete service:", error);
      toast.error("Failed to delete service");
    }
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex flex-col gap-4 justify-between items-start mb-6">
        <h1 className="text-3xl font-bold">Services</h1>
        <div className="flex w-full justify-between items-center">
          {user?.role !== "ROLE_VET" && (
            <Button
              onClick={() => router.push("/dashboard/services/requests")}
              className="shadow-sm py-3 rounded-full text-white bg-brand hover:bg-brand-100 cursor-pointer"
            >
              <FaClipboardCheck className="mr-2 h-4 w-4" />
              See all services requests
            </Button>
          )}
          {user?.role === "ROLE_ADMIN" && (
            <Button
              onClick={() => setIsAddModalOpen(true)}
              className="shadow-sm py-3 rounded-full text-white bg-brand hover:bg-brand-100 cursor-pointer"
            >
              <FaPlus className="mr-2 h-4 w-4" />
              Add New Service
            </Button>
          )}
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {services.map((service) => (
          <ServiceCard
            key={service.id}
            service={service}
            onUpdate={handleUpdateService}
            onDelete={handleDeleteService}
          />
        ))}
      </div>

      <AddServiceModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onSubmit={handleAddService}
      />
    </div>
  );
};

export default ServicesPage;
