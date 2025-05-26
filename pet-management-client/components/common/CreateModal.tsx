import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { useState, ReactNode } from "react";

interface CreateModalProps {
  title: string;
  triggerText: string;
  triggerIcon?: ReactNode;
  onSuccess: () => void;
  children: (props: {
    onSuccess: () => void;
    onCancel: () => void;
  }) => ReactNode;
}

export const CreateModal = ({
  title,
  triggerText,
  triggerIcon = <Plus className="mr-2 h-4 w-4" />,
  onSuccess,
  children,
}: CreateModalProps) => {
  const [open, setOpen] = useState(false);

  const handleSuccess = () => {
    setOpen(false);
    onSuccess();
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="shadow-sm py-3 rounded-full text-black hover:text-white hover:bg-brand-100 cursor-pointer">
          {triggerIcon}
          {triggerText}
        </Button>
      </DialogTrigger>
      <DialogContent className="h-fit overflow-y-auto bg-light-300">
        <DialogHeader className="mb-3">
          <DialogTitle>{title}</DialogTitle>
        </DialogHeader>
        {children({ onSuccess: handleSuccess, onCancel: () => setOpen(false) })}
      </DialogContent>
    </Dialog>
  );
};

export default CreateModal;
