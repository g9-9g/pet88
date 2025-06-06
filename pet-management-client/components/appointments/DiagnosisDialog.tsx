import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { useState, useEffect } from "react";
import { UpdateAppointmentDto } from "@/lib/api/appointments";
import {
  Medicine,
  AddMedicineToPrescriptionDto,
} from "@/lib/api/prescriptions";
import { usePrescriptions } from "@/hooks/use-prescriptions";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { IoTrash } from "react-icons/io5";

interface DiagnosisDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSubmit: (data: UpdateAppointmentDto) => Promise<void>;
  isReExamine?: boolean;
  appointmentId?: number;
}

export const DiagnosisDialog = ({
  open,
  onOpenChange,
  onSubmit,
  isReExamine = false,
  appointmentId,
}: DiagnosisDialogProps) => {
  const { fetchAllMedicines, addMedicinesBatch } = usePrescriptions();
  const [medicines, setMedicines] = useState<Medicine[]>([]);
  const [selectedMedicines, setSelectedMedicines] = useState<
    AddMedicineToPrescriptionDto[]
  >([]);
  const [formData, setFormData] = useState<UpdateAppointmentDto>({
    id: appointmentId,
    diagnosis: "",
    treatment: "",
    notes: "",
    status: "COMPLETED",
    completed: true,
  });

  useEffect(() => {
    const loadMedicines = async () => {
      try {
        const fetchedMedicines = await fetchAllMedicines();
        setMedicines(fetchedMedicines);
      } catch (error) {
        console.error("Failed to load medicines:", error);
      }
    };
    loadMedicines();
  }, [fetchAllMedicines]);

  const handleAddMedicine = () => {
    setSelectedMedicines([
      ...selectedMedicines,
      { medicineId: 0, quantity: 1, usageInstructions: "" },
    ]);
  };

  const handleMedicineChange = (
    index: number,
    field: keyof AddMedicineToPrescriptionDto,
    value: string | number
  ) => {
    const updatedMedicines = [...selectedMedicines];
    updatedMedicines[index] = {
      ...updatedMedicines[index],
      [field]: value,
    };
    setSelectedMedicines(updatedMedicines);
  };

  const handleRemoveMedicine = (index: number) => {
    setSelectedMedicines(selectedMedicines.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit(formData);
    if (selectedMedicines.length > 0 && appointmentId) {
      await addMedicinesBatch(appointmentId, selectedMedicines);
    }
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="h-fit overflow-y-auto bg-light-300 min-w-3xl max-h-[85vh]">
        <DialogHeader className="mb-3">
          <DialogTitle>
            {isReExamine ? "Schedule Re-examination" : "Enter Diagnosis"}
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          {!isReExamine && (
            <>
              <div className="space-y-2">
                <Label htmlFor="diagnosis">Diagnosis</Label>
                <Textarea
                  id="diagnosis"
                  value={formData.diagnosis}
                  onChange={(e) =>
                    setFormData((prev) => ({
                      ...prev,
                      diagnosis: e.target.value,
                    }))
                  }
                  placeholder="Enter diagnosis"
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="treatment">Treatment</Label>
                <Textarea
                  id="treatment"
                  value={formData.treatment}
                  onChange={(e) =>
                    setFormData((prev) => ({
                      ...prev,
                      treatment: e.target.value,
                    }))
                  }
                  placeholder="Enter treatment"
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="notes">Notes</Label>
                <Textarea
                  id="notes"
                  value={formData.notes}
                  onChange={(e) =>
                    setFormData((prev) => ({ ...prev, notes: e.target.value }))
                  }
                  placeholder="Enter additional notes"
                />
              </div>

              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <Label>Medicines</Label>
                  <Button
                    type="button"
                    onClick={handleAddMedicine}
                    className="bg-brand hover:bg-brand-100 text-white"
                  >
                    Add Medicine
                  </Button>
                </div>

                {selectedMedicines.map((medicine, index) => (
                  <div key={index} className="flex gap-4 items-start">
                    <div className="flex-1 space-y-2">
                      <Select
                        value={medicine.medicineId.toString()}
                        onValueChange={(value) =>
                          handleMedicineChange(
                            index,
                            "medicineId",
                            parseInt(value)
                          )
                        }
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Select medicine" />
                        </SelectTrigger>
                        <SelectContent>
                          {medicines.map((med) => (
                            <SelectItem key={med.id} value={med.id.toString()}>
                              {med.name} - {med.unit}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="w-24">
                      <Input
                        type="number"
                        min="1"
                        value={medicine.quantity}
                        onChange={(e) =>
                          handleMedicineChange(
                            index,
                            "quantity",
                            parseInt(e.target.value)
                          )
                        }
                        placeholder="Qty"
                      />
                    </div>
                    <div className="flex-1 w-full">
                      <Input
                        value={medicine.usageInstructions}
                        onChange={(e) =>
                          handleMedicineChange(
                            index,
                            "usageInstructions",
                            e.target.value
                          )
                        }
                        placeholder="Usage instructions"
                      />
                    </div>
                    <Button
                      type="button"
                      variant="ghost"
                      onClick={() => handleRemoveMedicine(index)}
                    >
                      <IoTrash className="w-4 h-4 text-brand" />
                    </Button>
                  </div>
                ))}
              </div>
            </>
          )}

          {isReExamine && (
            <div className="space-y-2">
              <Label htmlFor="appointmentDateTime">
                Appointment Date and Time
              </Label>
              <Input
                id="appointmentDateTime"
                type="datetime-local"
                onChange={(e) =>
                  setFormData((prev) => ({
                    ...prev,
                    appointmentDateTime: e.target.value,
                  }))
                }
                required
              />
            </div>
          )}

          <div className="flex justify-end gap-4 pt-3">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              className="bg-brand hover:bg-brand-100 text-white"
            >
              {isReExamine ? "Schedule" : "Submit"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};
