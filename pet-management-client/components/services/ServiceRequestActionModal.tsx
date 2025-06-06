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

interface ServiceRequestActionModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: {
    scheduledDateTime?: string;
    completedDateTime?: string;
    staffNotes?: string;
  }) => void;
  type: "accept" | "schedule" | "start" | "complete" | "reject";
  defaultDateTime?: string;
}

const ServiceRequestActionModal = ({
  isOpen,
  onClose,
  onSubmit,
  type,
  defaultDateTime,
}: ServiceRequestActionModalProps) => {
  const [dateTime, setDateTime] = React.useState(defaultDateTime || "");
  const [staffNotes, setStaffNotes] = React.useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (type === "schedule") {
      onSubmit({ scheduledDateTime: dateTime, staffNotes });
    } else if (type === "complete") {
      onSubmit({ completedDateTime: new Date().toISOString(), staffNotes });
    } else {
      onSubmit({ staffNotes });
    }
    setDateTime(defaultDateTime || "");
    setStaffNotes("");
  };

  const getTitle = () => {
    switch (type) {
      case "accept":
        return "Approve Request";
      case "schedule":
        return "Schedule Service";
      case "start":
        return "Start Service";
      case "complete":
        return "Complete Service";
      case "reject":
        return "Reject Request";
    }
  };

  const getSubmitButtonText = () => {
    switch (type) {
      case "accept":
        return "Approve";
      case "schedule":
        return "Schedule";
      case "start":
        return "Start";
      case "complete":
        return "Complete";
      case "reject":
        return "Reject";
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white border-none rounded-2xl shadow-md">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold mb-4">
            {getTitle()}
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          {type === "schedule" && (
            <div className="space-y-2">
              <Label htmlFor="dateTime">Schedule Date & Time</Label>
              <Input
                id="dateTime"
                type="datetime-local"
                value={dateTime}
                onChange={(e) => setDateTime(e.target.value)}
                required
              />
            </div>
          )}
          <div className="space-y-2">
            <Label htmlFor="staffNotes">Staff Notes</Label>
            <Textarea
              id="staffNotes"
              value={staffNotes}
              onChange={(e) => setStaffNotes(e.target.value)}
              placeholder="Enter staff notes..."
              required
            />
          </div>
          <div className="flex justify-end gap-2">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button
              type="submit"
              className={
                type === "reject"
                  ? "bg-red-500 hover:bg-red-600 text-white"
                  : "bg-brand hover:bg-brand-100 text-white"
              }
            >
              {getSubmitButtonText()}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ServiceRequestActionModal;
