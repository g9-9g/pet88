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
import { Room, RoomType } from "@/lib/api/rooms";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface UpdateRoomModalProps {
  room: Room;
  isOpen: boolean;
  onClose: () => void;
  onSubmit?: (updatedRoom: Room) => void;
}

const UpdateRoomModal = ({
  room,
  isOpen,
  onClose,
  onSubmit,
}: UpdateRoomModalProps) => {
  const [description, setDescription] = React.useState(room.description);
  const [type, setType] = React.useState<RoomType>(room.type);
  const [nightlyFee, setNightlyFee] = React.useState(
    room.nightlyFee.toString()
  );
  const [cleanFee, setCleanFee] = React.useState(room.cleanFee.toString());
  const [serviceFee, setServiceFee] = React.useState(
    room.serviceFee.toString()
  );

  React.useEffect(() => {
    // Update form when room changes
    setDescription(room.description);
    setType(room.type);
    setNightlyFee(room.nightlyFee.toString());
    setCleanFee(room.cleanFee.toString());
    setServiceFee(room.serviceFee.toString());
  }, [room]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSubmit) {
      onSubmit({
        ...room,
        description,
        type,
        nightlyFee: Number(nightlyFee),
        cleanFee: Number(cleanFee),
        serviceFee: Number(serviceFee),
      });
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white border-none rounded-2xl shadow-md min-w-xl">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold mb-4">
            Update Room
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
              Update Room
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateRoomModal;
