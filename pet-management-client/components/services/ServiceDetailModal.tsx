import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Image from "next/image";
import { Service } from "@/lib/api/services";
import { Button } from "@/components/ui/button";
import { useState } from "react";
import { useServicesRequests } from "@/hooks/use-services-requests";
import { useUser } from "@/context/UserContext";
import { usePets } from "@/context/PetsContext";
import { toast } from "sonner";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface ServiceDetailModalProps {
  service: Service;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onUpdate?: (updatedService: Service) => void;
}

export const ServiceDetailModal = ({
  service,
  open,
  onOpenChange,
  onUpdate,
}: ServiceDetailModalProps) => {
  const { user } = useUser();
  const { pets } = usePets();
  const { addRequest } = useServicesRequests();
  const [selectedPetId, setSelectedPetId] = useState<number | null>(null);
  const [requestedDateTime, setRequestedDateTime] = useState<string>("");
  const [notes, setNotes] = useState<string>("");

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  const handleMakeRequest = async () => {
    if (!user) {
      toast.error("Please login to make a request");
      return;
    }

    if (!selectedPetId) {
      toast.error("Please select a pet");
      return;
    }

    if (!requestedDateTime) {
      toast.error("Please select a date and time");
      return;
    }

    try {
      await addRequest({
        serviceId: service.id,
        petId: selectedPetId,
        requestedDateTime,
        notes: notes || undefined,
      });
      toast.success("Request created successfully");
      onOpenChange(false);
    } catch (error) {
      console.error("Error creating request:", error);
      toast.error("Failed to create request");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md bg-light-300 rounded-2xl pt-12 flex flex-col items-center overflow-visible fixed left-1/2 -translate-x-1/2 -translate-y-1/2">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold text-center mb-2">
            {service.name}
          </DialogTitle>
        </DialogHeader>

        {/* Service Details */}
        <div className="w-full flex flex-col items-center px-4">
          <div className="w-full mb-4">
            <div className="flex flex-col items-start gap-2">
              <div className="text-base font-semibold text-gray-600 min-w-[100px]">
                Description:
              </div>
              <div className="text-sm text-gray-800 flex-1 italic">
                {service.description}
              </div>
            </div>
          </div>

          <div className="flex items-center justify-between w-full gap-8 mb-4">
            <div className="flex items-center">
              <div className="text-base font-semibold text-gray-600 min-w-[60px]">
                Price:
              </div>
              <div className="text-base text-brand font-medium">
                {formatPrice(service.price)}
              </div>
            </div>
            <div className="flex items-center">
              <div className="text-base font-semibold text-gray-600 min-w-[80px]">
                Duration:
              </div>
              <div className="text-base text-gray-800">
                {service.durationMinutes} minutes
              </div>
            </div>
          </div>

          {/* Request Form */}
          {user?.role === "ROLE_PET_OWNER" && (
            <div className="w-full space-y-4">
              <div className="w-full">
                <div className="text-sm font-semibold text-gray-600 mb-1">
                  Select Pet
                </div>
                <Select
                  value={selectedPetId?.toString()}
                  onValueChange={(value) => setSelectedPetId(Number(value))}
                >
                  <SelectTrigger className="w-full">
                    <SelectValue placeholder="Select a pet" />
                  </SelectTrigger>
                  <SelectContent>
                    {pets.map((pet) => (
                      <SelectItem key={pet.petId} value={pet.petId.toString()}>
                        {pet.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              <div className="w-full">
                <div className="text-sm font-semibold text-gray-600 mb-1">
                  Preferred Date & Time
                </div>
                <input
                  type="datetime-local"
                  value={requestedDateTime}
                  onChange={(e) => setRequestedDateTime(e.target.value)}
                  className="w-full rounded-lg border bg-white px-4 py-2 text-gray-800"
                />
              </div>

              <div className="w-full">
                <div className="text-sm font-semibold text-gray-600 mb-1">
                  Notes (Optional)
                </div>
                <textarea
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  className="w-full rounded-lg border bg-white px-4 py-2 text-gray-800 resize-none"
                  rows={3}
                  placeholder="Any special instructions or requirements..."
                />
              </div>

              <Button
                onClick={handleMakeRequest}
                className="w-full bg-brand text-white hover:bg-brand-100"
              >
                Make Request
              </Button>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
};
