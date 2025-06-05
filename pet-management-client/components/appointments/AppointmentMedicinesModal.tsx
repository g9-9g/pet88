"use client";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Pill } from "lucide-react";
import { useState, useEffect } from "react";
import { usePrescriptions } from "@/hooks/use-prescriptions";
import { Prescription } from "@/lib/api/prescriptions";

interface AppointmentMedicinesModalProps {
  appointmentId: number;
}

export const AppointmentMedicinesModal = ({
  appointmentId,
}: AppointmentMedicinesModalProps) => {
  const [open, setOpen] = useState(false);
  const [medicines, setMedicines] = useState<Prescription[]>([]);
  const { getAppointmentMedicines, loading, error } = usePrescriptions();

  useEffect(() => {
    if (open) {
      const fetchMedicines = async () => {
        try {
          const data = await getAppointmentMedicines(appointmentId);
          setMedicines(data);
        } catch (error) {
          console.error("Failed to fetch medicines:", error);
        }
      };
      fetchMedicines();
    }
  }, [open, appointmentId, getAppointmentMedicines]);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="border border-blue-500 hover:bg-blue-500 hover:text-white text-blue-500">
          <Pill className="mr-2 h-4 w-4" />
          View Medicines
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-2xl bg-white">
        <DialogHeader>
          <DialogTitle>Prescribed Medicines</DialogTitle>
        </DialogHeader>
        {loading ? (
          <div className="flex justify-center items-center h-32">
            Loading medicines...
          </div>
        ) : error ? (
          <div className="text-red-500 text-center">{error}</div>
        ) : medicines.length === 0 ? (
          <div className="text-gray-500 text-center py-8">
            No medicines prescribed for this appointment
          </div>
        ) : (
          <div className="space-y-4">
            {medicines.map((medicine) => (
              <div
                key={medicine.id}
                className="p-4 border border-gray-200 rounded-lg bg-gray-50"
              >
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="font-semibold text-lg text-gray-900">
                      {medicine.medicineName}
                    </h3>
                    <p className="text-gray-800 mt-1">
                      Quantity: {medicine.quantity}
                    </p>
                  </div>
                </div>
                <div className="mt-2">
                  <p className="text-gray-700">
                    <span className="font-medium">Instructions: </span>
                    {medicine.usageInstructions}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};
