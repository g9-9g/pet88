import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { usePets } from "@/context/PetsContext";
import { useEffect, useState } from "react";
import { CreateMedicalRequestDto } from "@/lib/api/medical-requests";
import { useMedicalRequests } from "@/hooks/use-medical-requests";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface CreateMedicalRequestFormProps {
  onSuccess: () => void;
  onCancel: () => void;
}

export const CreateMedicalRequestForm = ({
  onSuccess,
  onCancel,
}: CreateMedicalRequestFormProps) => {
  const { pets } = usePets();
  const { addRequest } = useMedicalRequests();
  const [formData, setFormData] = useState<CreateMedicalRequestDto>({
    petId: 0,
    symptoms: "",
    notes: "",
    preferredDateTime: "",
  });

  useEffect(() => {
    if (pets.length > 0 && !formData.petId) {
      setFormData((prev) => ({ ...prev, petId: pets[0].petId }));
    }
  }, [pets, formData.petId]);

  console.log(pets);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await addRequest(formData);
      onSuccess();
    } catch (error) {
      console.error("Error creating medical request:", error);
    }
  };

  const handleChange = (
    field: keyof CreateMedicalRequestDto,
    value: string | number
  ) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="pet">Select Pet</Label>
        <Select
          value={formData.petId.toString()}
          onValueChange={(value) => handleChange("petId", parseInt(value))}
        >
          <SelectTrigger>
            <SelectValue placeholder="Select a pet" />
          </SelectTrigger>
          <SelectContent>
            {pets.map((pet) => (
              <SelectItem key={pet.petId} value={pet.petId.toString()}>
                {pet.name} ({pet.species})
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="space-y-2">
        <Label htmlFor="symptoms">Symptoms</Label>
        <Textarea
          id="symptoms"
          value={formData.symptoms}
          onChange={(e) => handleChange("symptoms", e.target.value)}
          placeholder="Enter symptoms"
          required
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="notes">Notes</Label>
        <Textarea
          id="notes"
          value={formData.notes}
          onChange={(e) => handleChange("notes", e.target.value)}
          placeholder="Enter additional notes"
          required
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="preferredDateTime">Preferred Date and Time</Label>
        <Input
          id="preferredDateTime"
          type="datetime-local"
          value={formData.preferredDateTime}
          onChange={(e) => handleChange("preferredDateTime", e.target.value)}
          required
        />
      </div>

      <div className="flex justify-end gap-4 pt-3">
        <Button type="button" variant="outline" onClick={onCancel}>
          Cancel
        </Button>
        <Button
          type="submit"
          className="bg-brand hover:bg-brand-100 text-white"
        >
          Create Request
        </Button>
      </div>
    </form>
  );
};

export default CreateMedicalRequestForm;
