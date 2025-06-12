"use client";

import { useEffect } from "react";
import { useUser } from "@/context/UserContext";
import { useReports } from "@/hooks/use-reports";
import { StatisticChart } from "@/components/dashboard/StatisticChart";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  MdArrowForward,
  MdMedicalServices,
  MdEvent,
  MdContentCut,
  MdHotel,
  MdPets,
} from "react-icons/md";
import { useRouter } from "next/navigation";

const chartColors = [
  "#3dd9b3", // chart-1
  "#56b8ff", // chart-2
  "#ff7474", // chart-3
  "#eea8fd", // chart-4
  "#f9ab72", // chart-5
];

const OwnerDashboardPage = () => {
  const { user } = useUser();
  const { ownerReport, loading, error, fetchOwnerReports } = useReports();
  const router = useRouter();

  useEffect(() => {
    if (user?.id) {
      fetchOwnerReports(user.id);
    }
  }, [user?.id, fetchOwnerReports]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!ownerReport) return null;

  const medicalRequestData = Object.entries(
    ownerReport.medicalRequestCount.statusCounts
  ).map(([status, count], idx) => ({
    name: status,
    value: count,
    fill: chartColors[idx % chartColors.length],
  }));

  const appointmentData = Object.entries(
    ownerReport.appointmentCount.statusCounts
  ).map(([status, count], idx) => ({
    name: status,
    value: count,
    fill: chartColors[idx % chartColors.length],
  }));

  const groomingData = Object.entries(
    ownerReport.groomingRequestCount.statusCounts
  ).map(([status, count], idx) => ({
    name: status,
    value: count,
    fill: chartColors[idx % chartColors.length],
  }));

  const bookingData = Object.entries(ownerReport.booking.statusCounts).map(
    ([status, count], idx) => ({
      name: status,
      value: count,
      fill: chartColors[idx % chartColors.length],
    })
  );

  return (
    <div className="container mx-auto py-6 space-y-6">
      <h1 className="text-3xl font-bold">Owner Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>
              <div className="flex items-center gap-2">
                <MdPets className="text-xl" />
                Total Pets
                <button
                  className="ml-2 p-1 rounded-full hover:bg-black/10"
                  onClick={() => router.push("/dashboard/pets")}
                  title="Go to Pets"
                  type="button"
                >
                  <MdArrowForward />
                </button>
              </div>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold">{ownerReport.petCount}</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <StatisticChart
          title={
            <div className="flex items-center gap-2">
              <MdMedicalServices className="text-xl" />
              Medical Requests
              <button
                className="ml-2 p-1 rounded-full hover:bg-black/10"
                onClick={() => router.push("/dashboard/requests")}
                title="Go to Medical Requests"
                type="button"
              >
                <MdArrowForward />
              </button>
            </div>
          }
          data={medicalRequestData}
          total={ownerReport.medicalRequestCount.total}
          description="Status distribution of medical requests"
        />
        <StatisticChart
          title={
            <div className="flex items-center gap-2">
              <MdEvent className="text-xl" />
              Appointments
              <button
                className="ml-2 p-1 rounded-full hover:bg-black/10"
                onClick={() => router.push("/dashboard/appointments")}
                title="Go to Appointments"
                type="button"
              >
                <MdArrowForward />
              </button>
            </div>
          }
          data={appointmentData}
          total={ownerReport.appointmentCount.total}
          description="Status distribution of appointments"
        />
        <StatisticChart
          title={
            <div className="flex items-center gap-2">
              <MdContentCut className="text-xl" />
              Grooming Requests
              <button
                className="ml-2 p-1 rounded-full hover:bg-black/10"
                onClick={() => router.push("/dashboard/services")}
                title="Go to Grooming Requests"
                type="button"
              >
                <MdArrowForward />
              </button>
            </div>
          }
          data={groomingData}
          total={ownerReport.groomingRequestCount.total}
          description="Status distribution of grooming requests"
        />
        <StatisticChart
          title={
            <div className="flex items-center gap-2">
              <MdHotel className="text-xl" />
              Bookings
              <button
                className="ml-2 p-1 rounded-full hover:bg-black/10"
                onClick={() => router.push("/dashboard/hotel")}
                title="Go to Bookings"
                type="button"
              >
                <MdArrowForward />
              </button>
            </div>
          }
          data={bookingData}
          total={ownerReport.booking.total}
          description="Status distribution of room bookings"
        />
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
