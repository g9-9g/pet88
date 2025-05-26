"use client";

import React, { useEffect, useState } from "react";
import { useUser } from "@/context/UserContext";
import { useMedicalRequests } from "@/hooks/use-medical-requests";
import CreateModal from "@/components/common/CreateModal";
import CreateMedicalRequestForm from "@/components/medical-requests/CreateMedicalRequestForm";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { MedicalRequest } from "@/lib/api/medical-requests";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { IoFilter } from "react-icons/io5";
import { useSearchParams, useRouter } from "next/navigation";
import { cn, formatDateTime } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { FaCheck, FaTimes } from "react-icons/fa";
import { getUsersByRole, User } from "@/lib/api/users";
import { toast } from "sonner";

const RequestsPage = () => {
  const { user, isLoading } = useUser();
  const {
    requests,
    loading,
    error,
    fetchRequests,
    handleAcceptRequest,
    handleRejectRequest,
  } = useMedicalRequests();
  const searchParams = useSearchParams();
  const router = useRouter();
  const status = searchParams.get("status") || "ALL";
  const [activeForm, setActiveForm] = useState<{
    id: number;
    type: "accept" | "reject";
  } | null>(null);
  const [selectedVetId, setSelectedVetId] = useState<string>("");
  const [rejectionReason, setRejectionReason] = useState("");
  const [vets, setVets] = useState<User[]>([]);

  useEffect(() => {
    const fetchVets = async () => {
      try {
        const vetsList = await getUsersByRole("ROLE_VET");
        setVets(vetsList);
      } catch (error) {
        console.error("Failed to fetch vets:", error);
        toast.error("Failed to fetch vets list");
      }
    };

    if (user?.role === "ROLE_STAFF") {
      fetchVets();
    }
  }, [user?.role]);

  useEffect(() => {
    if (user) {
      fetchRequests(status as "ALL" | "PENDING" | "ACCEPTED" | "REJECTED");
    }
  }, [user, fetchRequests, status]);

  if (isLoading) {
    return <div>Loading user information...</div>;
  }

  if (!user) {
    router.push("/sign-in");
    return null;
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PENDING":
        return "bg-amber-400";
      case "ACCEPTED":
        return "bg-lime-500";
      case "REJECTED":
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
    router.push(`/dashboard/requests?${params.toString()}`);
  };

  const handleAccept = async (requestId: number) => {
    if (!selectedVetId) {
      toast.error("Please select a vet");
      return;
    }

    try {
      await handleAcceptRequest(requestId, parseInt(selectedVetId));
      setActiveForm(null);
      setSelectedVetId("");
    } catch (error) {
      console.error("Failed to accept request:", error);
    }
  };

  const handleReject = async (requestId: number) => {
    if (!rejectionReason.trim()) {
      toast.error("Please enter a rejection reason");
      return;
    }

    try {
      await handleRejectRequest(requestId, rejectionReason);
      setActiveForm(null);
      setRejectionReason("");
    } catch (error) {
      console.error("Failed to reject request:", error);
    }
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Medical Requests</h1>
        <div className="flex items-center gap-4">
          <Select value={status} onValueChange={handleStatusChange}>
            <SelectTrigger className="w-full px-4 py-2 font-semibold border-none rounded-full shadow-sm">
              <IoFilter className="w-6 h-6 mr-2" />
              <SelectValue placeholder="Filter by status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">All</SelectItem>
              <SelectItem value="PENDING">Pending</SelectItem>
              <SelectItem value="ACCEPTED">Accepted</SelectItem>
              <SelectItem value="REJECTED">Rejected</SelectItem>
            </SelectContent>
          </Select>
          {user.role === "ROLE_PET_OWNER" && (
            <CreateModal
              title="Create Medical Request"
              triggerText="New Request"
              onSuccess={fetchRequests}
            >
              {({ onSuccess, onCancel }) => (
                <CreateMedicalRequestForm
                  onSuccess={onSuccess}
                  onCancel={onCancel}
                />
              )}
            </CreateModal>
          )}
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div className="text-red-500">{error}</div>}

      <div className="flex flex-col gap-8">
        {requests.map((request: MedicalRequest) => {
          const date = new Date(request.preferredDateTime);
          const month = date.toLocaleString("default", { month: "long" });
          const year = date.getFullYear();
          const day = date.getDate();
          const time = date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          });
          return (
            <Card
              key={request.id}
              className="flex flex-row p-0 bg-white rounded-2xl relative overflow-hidden shadow-lg border-none min-h-[200px]"
            >
              {/* Status badge at top right */}
              <div className="absolute right-8 top-5 text-slate-100 flex flex-col items-end">
                <Badge
                  className={cn(
                    getStatusColor(request.status),
                    "rounded-full px-3 py-1 font-bold tracking-wider shadow-sm"
                  )}
                >
                  {request.status}
                </Badge>
                {/* Staff actions */}
                {user?.role === "ROLE_STAFF" &&
                  request.status === "PENDING" && (
                    <div className="mt-4">
                      {activeForm?.id === request.id ? (
                        <div className="flex flex-col gap-2 px-4 bg-gray-50 rounded-lg text-gray-700">
                          {activeForm.type === "accept" ? (
                            <>
                              <Select
                                value={selectedVetId}
                                onValueChange={setSelectedVetId}
                              >
                                <SelectTrigger className="w-full font-semibold">
                                  <SelectValue placeholder="Select a vet" />
                                </SelectTrigger>
                                <SelectContent>
                                  {vets.map((vet) => (
                                    <SelectItem
                                      key={vet.id}
                                      value={vet.id.toString()}
                                    >
                                      {vet.username}
                                    </SelectItem>
                                  ))}
                                </SelectContent>
                              </Select>
                              <Button
                                onClick={() => handleAccept(request.id)}
                                className="text-white bg-lime-500 hover:bg-lime-600"
                              >
                                Assign
                              </Button>
                            </>
                          ) : (
                            <>
                              <Input
                                placeholder="Enter rejection reason"
                                value={rejectionReason}
                                onChange={(e) =>
                                  setRejectionReason(e.target.value)
                                }
                              />
                              <Button
                                onClick={() => handleReject(request.id)}
                                className="text-white bg-red-400 hover:bg-brand-100"
                              >
                                Confirm Reject
                              </Button>
                            </>
                          )}
                          <Button
                            variant="outline"
                            onClick={() => setActiveForm(null)}
                          >
                            Cancel
                          </Button>
                        </div>
                      ) : (
                        <div className="flex gap-2">
                          <Button
                            onClick={() =>
                              setActiveForm({ id: request.id, type: "accept" })
                            }
                            className="border border-lime-500 hover:bg-lime-500 hover:text-white text-lime-500"
                          >
                            <FaCheck className="" />
                          </Button>
                          <Button
                            onClick={() =>
                              setActiveForm({ id: request.id, type: "reject" })
                            }
                            className="border border-red-400 hover:bg-red-400 text-red-400 hover:text-white"
                          >
                            <FaTimes className="" />
                          </Button>
                        </div>
                      )}
                    </div>
                  )}
              </div>
              {/* Left section: Date */}
              <div
                className={cn(
                  "bg-slate-200 flex flex-col items-center justify-between py-6 px-6 w-36 min-w-[8rem] text-center"
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
                  Medical request for{" "}
                  <span className="text-2xl font-semibold">
                    {request.petName}
                  </span>
                </div>
                <div className="flex flex-row gap-2 items-center text-gray-500 text-base mb-4 font-mono">
                  Requested by
                  <span className="font-semibold text-gray-700">
                    {request.ownerName}
                  </span>{" "}
                  at
                  <span className="font-semibold text-gray-700">
                    {formatDateTime(request.createdAt).dateTime}
                  </span>
                </div>
                <div className="flex flex-col gap-1 text-gray-600 text-base mb-4">
                  <span className="font-semibold">Symptoms:</span>{" "}
                  {request.symptoms}
                </div>
                <div className="flex flex-col gap-1">
                  <span className="font-semibold text-gray-500">Notes:</span>
                  <span className="text-gray-700 whitespace-pre-line">
                    {request.notes}
                  </span>
                </div>
                {request.status === "ACCEPTED" && (
                  <div className="text-lime-500 mt-2">
                    <span className="font-semibold">
                      Request accepted by staff{" "}
                    </span>{" "}
                    <span className="font-mono">{request.updatedByName}</span>
                  </div>
                )}
                {request.rejectionReason && (
                  <div className="text-red-500 mt-2">
                    <span className="font-semibold">Rejected Reason:</span>{" "}
                    {request.rejectionReason}
                  </div>
                )}
              </div>
              <div
                className={cn(
                  "min-h-full w-3 rounded-full absolute left-0",
                  getStatusColor(request.status)
                )}
              ></div>
            </Card>
          );
        })}
      </div>
    </div>
  );
};

export default RequestsPage;
