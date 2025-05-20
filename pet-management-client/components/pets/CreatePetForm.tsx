"use client";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { CreatePetDto, createPet } from "@/lib/api/pets";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import * as z from "zod";
import { useEffect } from "react";

const formSchema = z.object({
  ownerId: z.number(),
  name: z.string().min(2, "Name must be at least 2 characters"),
  species: z.string().min(2, "Species must be at least 2 characters"),
  breed: z.string().min(2, "Breed must be at least 2 characters"),
  gender: z.enum(["Male", "Female"]),
  birthdate: z.string().min(1, "Birthdate is required"),
  color: z.string().min(2, "Color must be at least 2 characters"),
  avatarUrl: z.string().url("Must be a valid URL").optional().or(z.literal("")),
  healthNotes: z.string().optional(),
  nutritionNotes: z.string().optional(),
});

interface CreatePetFormProps {
  ownerId: number;
  onSuccess: () => void;
  onCancel: () => void;
}

export const CreatePetForm = ({
  ownerId,
  onSuccess,
  onCancel,
}: CreatePetFormProps) => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      ownerId,
      name: "",
      species: "",
      breed: "",
      gender: "Male",
      birthdate: "",
      color: "",
      avatarUrl: "",
      healthNotes: "",
      nutritionNotes: "",
    },
  });

  // Set ownerId when component mounts
  useEffect(() => {
    form.setValue("ownerId", ownerId);
  }, [form, ownerId]);

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    console.log("Form values:", values);
    try {
      await createPet(values);
      toast.success("Pet created successfully");
      form.reset();
      onSuccess();
    } catch (error) {
      console.error("Error creating pet:", error);
      toast.error("Failed to create pet");
    }
  };

  const onError = (errors: any) => {
    console.log("Form validation errors:", errors);
    toast.error("Please check the form for errors");
  };

  console.log(ownerId);

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit, onError)}
        className="space-y-4"
      >
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input placeholder="Enter pet name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <div className="flex justify-between">
          <FormField
            control={form.control}
            name="species"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Species</FormLabel>
                <FormControl>
                  <Input placeholder="Enter species" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="breed"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Breed</FormLabel>
                <FormControl>
                  <Input placeholder="Enter breed" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div className="flex justify-between">
          <FormField
            control={form.control}
            name="color"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Color</FormLabel>
                <FormControl>
                  <Input placeholder="Enter color" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="avatarUrl"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Avatar URL</FormLabel>
                <FormControl>
                  <Input placeholder="Enter avatar URL" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div className="flex justify-between space-x-3 items-center">
          <FormField
            control={form.control}
            name="birthdate"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Birthdate</FormLabel>
                <FormControl>
                  <Input type="date" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="gender"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Gender</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select gender" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent className="bg-light-300">
                    <SelectItem value="Male">Male</SelectItem>
                    <SelectItem value="Female">Female</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <FormField
          control={form.control}
          name="healthNotes"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Health Notes</FormLabel>
              <FormControl>
                <Textarea
                  placeholder="Enter health notes"
                  className="resize-none"
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="nutritionNotes"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Nutrition Notes</FormLabel>
              <FormControl>
                <Textarea
                  placeholder="Enter nutrition notes"
                  className="resize-none"
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onCancel}>
            Cancel
          </Button>
          <Button
            type="submit"
            className="bg-brand text-white hover:bg-brand-100 border border-black"
          >
            Create Pet
          </Button>
        </div>
      </form>
    </Form>
  );
};
