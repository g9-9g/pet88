import React from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { RoomType } from "@/lib/api/rooms";

interface AddRoomModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: {
    description: string;
    type: RoomType;
    nightlyFee: number;
    cleanFee: number;
    serviceFee: number;
  }) => void;
}

const AddRoomModal = ({ isOpen, onClose, onSubmit }: AddRoomModalProps) => {
  const [description, setDescription] = React.useState("");
  const [type, setType] = React.useState<RoomType>("STANDARD");
  const [nightlyFee, setNightlyFee] = React.useState("");
  const [cleanFee, setCleanFee] = React.useState("");
  const [serviceFee, setServiceFee] = React.useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      description,
      type,
      nightlyFee: Number(nightlyFee),
      cleanFee: Number(cleanFee),
      serviceFee: Number(serviceFee),
    });
    // Reset form
    setDescription("");
    setType("STANDARD");
    setNightlyFee("");
    setCleanFee("");
    setServiceFee("");
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white border-none rounded-2xl shadow-md min-w-xl">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold mb-4">
            Add New Room
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="type">Room Type</Label>
            <Select
              value={type}
              onValueChange={(value) => setType(value as RoomType)}
            >
              <SelectTrigger>
                <SelectValue placeholder="Select room type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="STANDARD">Standard</SelectItem>
                <SelectItem value="DELUXE">Deluxe</SelectItem>
                <SelectItem value="LUXURY">Luxury</SelectItem>
                <SelectItem value="SUITE">Suite</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Enter room description..."
              required
            />
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div className="space-y-2">
              <Label htmlFor="nightlyFee">Nightly Fee (VND)</Label>
              <Input
                id="nightlyFee"
                type="number"
                value={nightlyFee}
                onChange={(e) => setNightlyFee(e.target.value)}
                placeholder="Enter nightly fee..."
                required
                min="0"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="cleanFee">Cleaning Fee (VND)</Label>
              <Input
                id="cleanFee"
                type="number"
                value={cleanFee}
                onChange={(e) => setCleanFee(e.target.value)}
                placeholder="Enter cleaning fee..."
                required
                min="0"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="serviceFee">Service Fee (VND)</Label>
              <Input
                id="serviceFee"
                type="number"
                value={serviceFee}
                onChange={(e) => setServiceFee(e.target.value)}
                placeholder="Enter service fee..."
                required
                min="0"
              />
            </div>
          </div>

          <div className="flex justify-end gap-2">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button
              type="submit"
              className="bg-brand hover:bg-brand-100 text-white"
            >
              Add Room
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddRoomModal;
