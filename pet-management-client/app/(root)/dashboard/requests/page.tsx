"use client";

import React from "react";
import { useUser } from "@/context/UserContext"; // Import useUser

const RequestsPage = () => {
  const { user, isLoading } = useUser(); // Use the hook

  if (isLoading) {
    return <div>Loading user information...</div>;
  }

  if (!user) {
    // This case should ideally be handled by a redirect to login if no user is found after loading
    return <div>Please log in to see requests.</div>;
  }

  // Example: Fetch appointments based on user role (conceptual)
  // useEffect(() => {
  //   if (user) {
  //     // const endpoint = `/api/appointments?role=${user.role}&userId=${user.id}`;
  //     // fetch(endpoint).then(...)
  //     console.log(`Fetching appointments for ${user.role} (${user.name})`);
  //   }
  // }, [user]);

  return (
    <div>
      <h1>Appointments Page</h1>
      <p>
        Welcome, {user.name} ({user.role})
      </p>

      {/* Conditional rendering based on role */}
      {user.role === "ROLE_PET_OWNER" && (
        <div>
          <h2>Your Pet's Medical Requests</h2>
          <button>Book New Request</button>
          {/* List owner's appointments here */}
        </div>
      )}

      {user.role === "ROLE_STAFF" && (
        <div>
          <h2>Manage All Requests</h2>
          {/* Interface for staff to manage all appointments */}
        </div>
      )}

      {user.role === "ROLE_ADMIN" && (
        <div>
          <h2>Admin Overview of Requests</h2>
          {/* Admin view, perhaps statistics or high-level management */}
        </div>
      )}

      {/* You would typically fetch and display a list of appointments here,
          filtered and with actions appropriate for the user.role */}
    </div>
  );
};

export default RequestsPage;
