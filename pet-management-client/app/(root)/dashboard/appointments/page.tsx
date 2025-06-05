"use client";

import React, { useEffect, useState } from "react";
import { useUser } from "@/context/UserContext";
import { useRouter, useSearchParams } from "next/navigation";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useAppointments } from "@/hooks/use-appointments";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { IoFilter } from "react-icons/io5";
import { Appointment } from "@/lib/api/appointments";
import { DiagnosisDialog } from "@/components/appointments/DiagnosisDialog";
import { CreateAppointmentDto } from "@/lib/api/appointments";
import { Separator } from "@/components/ui/separator";
import { AppointmentMedicinesModal } from "@/components/appointments/AppointmentMedicinesModal";

const AppointmentsPage = () => {
  const { user, isLoading } = useUser();
  const {
    appointments,
    loading,
    error,
    fetchAppointments,
    updateAppointmentStatus,
    addAppointment,
  } = useAppointments();
  const searchParams = useSearchParams();
  const router = useRouter();
  const status = searchParams.get("status") || "ALL";
  const [selectedAppointment, setSelectedAppointment] =
    useState<Appointment | null>(null);
  const [isDiagnosisOpen, setIsDiagnosisOpen] = useState(false);
  const [isReExamineOpen, setIsReExamineOpen] = useState(false);

  useEffect(() => {
    if (!isLoading && !user) {
      router.push("/sign-in");
    }
  }, [user, isLoading, router]);

  useEffect(() => {
    if (user) {
      fetchAppointments();
    }
  }, [user, fetchAppointments]);

  if (isLoading) {
    return <div>Loading user information...</div>;
  }

  if (!user) {
    return null;
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PENDING":
        return "bg-amber-400";
      case "COMPLETED":
        return "bg-lime-500";
      case "CANCELLED":
        return "bg-red-400";
      default:
        return "bg-gray-500";
    }
  };

  const handleStatusChange = (value: string) => {
    const params = new URLSearchParams(searchParams.toString());
    if (value === "ALL") {
      params.delete("status");
    } else {
      params.set("status", value);
    }
    router.push(`/dashboard/appointments?${params.toString()}`);
  };

  const handleDiagnosis = async (data: any) => {
    if (!selectedAppointment) return;
    try {
      await updateAppointmentStatus(selectedAppointment.id, {
        ...data,
        status: "COMPLETED",
        completed: true,
      });
      await fetchAppointments();
    } catch (error) {
      console.error("Failed to update appointment:", error);
    }
  };

  const handleReExamine = async (data: any) => {
    if (!selectedAppointment) return;
    try {
      const newAppointment: CreateAppointmentDto = {
        petId: selectedAppointment.petId,
        appointmentDateTime: data.appointmentDateTime,
      };
      await addAppointment(newAppointment);
      await fetchAppointments();
    } catch (error) {
      console.error("Failed to create re-examination appointment:", error);
    }
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Appointments</h1>
        <div className="flex items-center gap-4">
          <Select value={status} onValueChange={handleStatusChange}>
            <SelectTrigger className="w-full px-4 py-2 font-semibold border-none rounded-full shadow-sm">
              <IoFilter className="w-6 h-6 mr-2" />
              <SelectValue placeholder="Filter by status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">All</SelectItem>
              <SelectItem value="PENDING">Pending</SelectItem>
              <SelectItem value="COMPLETED">Completed</SelectItem>
              <SelectItem value="CANCELLED">Cancelled</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="flex flex-col gap-8">
        {appointments.map((appointment: Appointment) => {
          const date = new Date(appointment.appointmentDateTime);
          const month = date.toLocaleString("default", { month: "long" });
          const year = date.getFullYear();
          const day = date.getDate();
          const time = date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          });
          return (
            <Card
              key={appointment.id}
              className="flex flex-row p-0 bg-white rounded-2xl relative overflow-hidden shadow-lg border-none min-h-[200px]"
            >
              {/* Status badge at top right */}
              <div className="absolute right-8 top-5 text-slate-100 flex flex-col items-end">
                <Badge
                  className={cn(
                    getStatusColor(
                      appointment.completed ? "COMPLETED" : "PENDING"
                    ),
                    "rounded-full px-3 py-1 font-bold tracking-wider shadow-sm"
                  )}
                >
                  {appointment.completed ? "COMPLETED" : "PENDING"}
                </Badge>
                {/* Doctor actions */}
                {user?.role === "ROLE_VET" && !appointment.completed && (
                  <div className="mt-4">
                    <Button
                      onClick={() => {
                        setSelectedAppointment(appointment);
                        setIsDiagnosisOpen(true);
                      }}
                      className="border border-green-500 hover:bg-green-500 hover:text-white text-green-500"
                    >
                      Diagnose
                    </Button>
                  </div>
                )}
                {user?.role === "ROLE_VET" && appointment.completed && (
                  <div className="mt-4">
                    <Button
                      onClick={() => {
                        setSelectedAppointment(appointment);
                        setIsReExamineOpen(true);
                      }}
                      className="border border-blue-500 hover:bg-blue-500 hover:text-white text-blue-500"
                    >
                      Re-examine
                    </Button>
                  </div>
                )}
                {appointment.completed && (
                  <div className="mt-4">
                    <AppointmentMedicinesModal appointmentId={appointment.id} />
                  </div>
                )}
              </div>
              {/* Left section: Date */}
              <div
                className={cn(
                  "bg-blue-100 flex flex-col items-center justify-between py-6 px-6 w-36 min-w-[8rem] text-center"
                )}
              >
                <div>
                  <div className="text-gray-800 text-lg font-mono flex flex-col items-center justify-between">
                    <span>{year}</span>
                    <span>{month}</span>
                  </div>
                  <div className="text-4xl font-bold text-gray-800 mt-1">
                    {day}
                  </div>
                </div>
                <div className="text-gray-700 text-sm font-mono mt-4">
                  {time}
                </div>
              </div>
              {/* Right section: Content */}

              <div className="flex-1 p-4 flex flex-col justify-between bg-white">
                <div className="text-xl font-mono mb-2 ml-0">
                  Appointment for{" "}
                  <span className="text-2xl font-semibold">
                    {appointment.petName}
                  </span>
                </div>
                <div className="flex flex-row gap-2 items-center text-gray-500 text-base mb-4 font-mono">
                  Owner:{" "}
                  <span className="font-semibold text-gray-700">
                    {appointment.ownerName}
                  </span>
                  {/* Display Doctor if available and completed */}
                  {appointment.completed && appointment.doctorName && (
                    <div className="text-lime-500 font-semibold text-base font-mono">
                      {" "}
                      | Diagnosed by Dr.{" "}
                      <span className="font-semibold">
                        {appointment.doctorName}
                      </span>
                    </div>
                  )}
                </div>
                <div className="grid grid-cols-2 bg-white divide-x divide-gray-300">
                  {/* Column 1 */}
                  <div className="flex flex-col gap-2 pr-2">
                    {appointment.symptoms && (
                      <div className="flex flex-col gap-1 text-gray-600 text-base mb-4">
                        <span className="font-semibold">Symptoms:</span>{" "}
                        {appointment.symptoms}
                      </div>
                    )}
                    {appointment.notes && (
                      <div className="flex flex-col gap-1">
                        <span className="font-semibold text-gray-500">
                          Notes:
                        </span>
                        <span className="text-gray-700 whitespace-pre-line">
                          {appointment.notes}
                        </span>
                      </div>
                    )}
                  </div>

                  {/* Column 2 */}
                  <div className="flex flex-col gap-2 pl-4">
                    {appointment.diagnosis && (
                      <div className="flex flex-col gap-1 text-slate-600 text-base w-fit rounded mb-4">
                        <span className="font-bold">Diagnosis:</span>{" "}
                        {appointment.diagnosis}
                      </div>
                    )}
                    {appointment.treatment && (
                      <div className="flex flex-col gap-1 text-slate-600 text-base w-fit rounded">
                        <span className="font-bold">Treatment:</span>{" "}
                        {appointment.treatment}
                      </div>
                    )}
                  </div>
                </div>
              </div>
              <div
                className={cn(
                  "min-h-full w-3 rounded-full absolute left-0",
                  getStatusColor(
                    appointment.completed ? "COMPLETED" : "PENDING"
                  )
                )}
              ></div>
            </Card>
          );
        })}
      </div>

      <DiagnosisDialog
        open={isDiagnosisOpen}
        onOpenChange={setIsDiagnosisOpen}
        onSubmit={handleDiagnosis}
      />

      <DiagnosisDialog
        open={isReExamineOpen}
        onOpenChange={setIsReExamineOpen}
        onSubmit={handleReExamine}
        isReExamine
      />
    </div>
  );
};

export default AppointmentsPage;
