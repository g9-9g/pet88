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
import { RoomType } from "@/lib/api/rooms";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface FilterRoomsModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: {
    start?: string;
    end?: string;
    type?: RoomType;
    maxPrice?: number;
    minPrice?: number;
  }) => void;
  initialFilters?: {
    start?: string;
    end?: string;
    type?: RoomType;
    maxPrice?: number;
    minPrice?: number;
  };
}

const FilterRoomsModal = ({
  isOpen,
  onClose,
  onSubmit,
  initialFilters,
}: FilterRoomsModalProps) => {
  const [startDate, setStartDate] = React.useState(
    initialFilters?.start?.split("T")[0] || ""
  );
  const [startTime, setStartTime] = React.useState(
    initialFilters?.start?.split("T")[1] || "00:00"
  );
  const [endDate, setEndDate] = React.useState(
    initialFilters?.end?.split("T")[0] || ""
  );
  const [endTime, setEndTime] = React.useState(
    initialFilters?.end?.split("T")[1] || "00:00"
  );
  const [type, setType] = React.useState<RoomType | undefined>(
    initialFilters?.type
  );
  const [minPrice, setMinPrice] = React.useState(
    initialFilters?.minPrice?.toString() || ""
  );
  const [maxPrice, setMaxPrice] = React.useState(
    initialFilters?.maxPrice?.toString() || ""
  );

  React.useEffect(() => {
    // Update form when filters change
    setStartDate(initialFilters?.start?.split("T")[0] || "");
    setStartTime(initialFilters?.start?.split("T")[1] || "00:00");
    setEndDate(initialFilters?.end?.split("T")[0] || "");
    setEndTime(initialFilters?.end?.split("T")[1] || "00:00");
    setType(initialFilters?.type);
    setMinPrice(initialFilters?.minPrice?.toString() || "");
    setMaxPrice(initialFilters?.maxPrice?.toString() || "");
  }, [initialFilters]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const filters: {
      start?: string;
      end?: string;
      type?: RoomType;
      maxPrice?: number;
      minPrice?: number;
    } = {};

    if (startDate) {
      filters.start = `${startDate}T${startTime}`;
    }
    if (endDate) {
      filters.end = `${endDate}T${endTime}`;
    }
    if (type) {
      filters.type = type;
    }
    if (minPrice) {
      filters.minPrice = Number(minPrice);
    }
    if (maxPrice) {
      filters.maxPrice = Number(maxPrice);
    }

    onSubmit(filters);
  };

  const handleClear = () => {
    setStartDate("");
    setStartTime("00:00");
    setEndDate("");
    setEndTime("00:00");
    setType(undefined);
    setMinPrice("");
    setMaxPrice("");
    onSubmit({});
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white border-none rounded-2xl shadow-md min-w-xl">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold mb-4">
            Filter Rooms
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="startDate">Check-in Date</Label>
              <Input
                id="startDate"
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="startTime">Check-in Time</Label>
              <Input
                id="startTime"
                type="time"
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="endDate">Check-out Date</Label>
              <Input
                id="endDate"
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="endTime">Check-out Time</Label>
              <Input
                id="endTime"
                type="time"
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="type">Room Type</Label>
            <Select
              value={type}
              onValueChange={(value) => setType(value as RoomType)}
            >
              <SelectTrigger>
                <SelectValue placeholder="All room types" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="STANDARD">Standard</SelectItem>
                <SelectItem value="DELUXE">Deluxe</SelectItem>
                <SelectItem value="LUXURY">Luxury</SelectItem>
                <SelectItem value="SUITE">Suite</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="minPrice">Min Price (VND)</Label>
              <Input
                id="minPrice"
                type="number"
                value={minPrice}
                onChange={(e) => setMinPrice(e.target.value)}
                placeholder="Enter minimum price..."
                min="0"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="maxPrice">Max Price (VND)</Label>
              <Input
                id="maxPrice"
                type="number"
                value={maxPrice}
                onChange={(e) => setMaxPrice(e.target.value)}
                placeholder="Enter maximum price..."
                min="0"
              />
            </div>
          </div>

          <div className="flex justify-end gap-2">
            <Button type="button" variant="outline" onClick={handleClear}>
              Clear Filters
            </Button>
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button
              type="submit"
              className="bg-brand hover:bg-brand-100 text-white"
            >
              Apply Filters
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default FilterRoomsModal;
