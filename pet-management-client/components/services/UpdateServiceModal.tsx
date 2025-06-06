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
import { Service } from "@/lib/api/services";

interface UpdateServiceModalProps {
  service: Service;
  isOpen: boolean;
  onClose: () => void;
  onSubmit?: (updatedService: Service) => void;
}

const UpdateServiceModal = ({
  service,
  isOpen,
  onClose,
  onSubmit,
}: UpdateServiceModalProps) => {
  const [name, setName] = React.useState(service.name);
  const [description, setDescription] = React.useState(service.description);
  const [price, setPrice] = React.useState(service.price.toString());
  const [durationMinutes, setDurationMinutes] = React.useState(
    service.durationMinutes.toString()
  );

  React.useEffect(() => {
    // Update form when service changes
    setName(service.name);
    setDescription(service.description);
    setPrice(service.price.toString());
    setDurationMinutes(service.durationMinutes.toString());
  }, [service]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSubmit) {
      onSubmit({
        ...service,
        name,
        description,
        price: Number(price),
        durationMinutes: Number(durationMinutes),
      });
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white border-none rounded-2xl shadow-md">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold mb-4">
            Update Service
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Service Name</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter service name..."
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Enter service description..."
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="price">Price (VND)</Label>
              <Input
                id="price"
                type="number"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                placeholder="Enter price..."
                required
                min="0"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="duration">Duration (minutes)</Label>
              <Input
                id="duration"
                type="number"
                value={durationMinutes}
                onChange={(e) => setDurationMinutes(e.target.value)}
                placeholder="Enter duration..."
                required
                min="1"
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
              Update Service
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateServiceModal;
