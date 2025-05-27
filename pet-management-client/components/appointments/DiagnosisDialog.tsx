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
import { useState } from "react";
import { UpdateAppointmentDto } from "@/lib/api/appointments";

interface DiagnosisDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSubmit: (data: UpdateAppointmentDto) => Promise<void>;
  isReExamine?: boolean;
}

export const DiagnosisDialog = ({
  open,
  onOpenChange,
  onSubmit,
  isReExamine = false,
}: DiagnosisDialogProps) => {
  const [formData, setFormData] = useState<UpdateAppointmentDto>({
    diagnosis: "",
    treatment: "",
    notes: "",
    status: "COMPLETED",
    completed: true,
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit(formData);
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="h-fit overflow-y-auto bg-light-300">
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
