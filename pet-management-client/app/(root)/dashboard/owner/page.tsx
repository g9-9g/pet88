"use client";

import React from "react";
import Link from "next/link";
import { useUser } from "@/context/UserContext";
import { useRouter } from "next/navigation";

const OwnerDashboardPage = () => {
  const { user, isLoading } = useUser();
  const router = useRouter();

  if (isLoading) {
    return <div>Loading dashboard...</div>;
  }

  // If no user or user is not an owner, redirect them
  // (e.g., to login or a generic dashboard)
  // This check might also be handled by middleware for the route group
  if (!user || user.role !== "ROLE_PET_OWNER") {
    // In a real app, you might redirect to login or a generic error/dashboard page
    // For now, let's assume middleware would handle unauthorized access to this page,
    // or redirect to a more generic dashboard if the role is wrong.
    // router.push("/dashboard");
    return (
      <div>
        Access denied or incorrect role for this dashboard. (Owner Dashboard)
      </div>
    );
  }

  return (
    <div>
      <h1>Welcome to your Owner Dashboard, {user.name}!</h1>
      <p>Here you can manage your pets and appointments.</p>

      <h2>Quick Links:</h2>
      <ul>
        <li>
          <Link href="/dashboard/pets">My Pets</Link>
        </li>
        <li>
          <Link href="/dashboard/appointments">My Appointments</Link>
        </li>
        {/* Add other owner-specific links here */}
      </ul>

      {/* You can add owner-specific summary components here */}
      {/* e.g., <UpcomingAppointmentsSummary />, <MyPetsOverview /> */}
    </div>
  );
};

export default OwnerDashboardPage;
