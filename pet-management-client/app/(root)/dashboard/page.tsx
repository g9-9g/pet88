"use client";

import React, { useEffect } from "react";
import { useUser } from "@/context/UserContext";
import { useRouter } from "next/navigation";

const GenericDashboardPage = () => {
  const { user, isLoading } = useUser();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading && user) {
      switch (user.role) {
        case "ROLE_PET_OWNER":
          router.replace("/dashboard/owner");
          break;
        case "ROLE_VET":
          router.replace("/dashboard/vet");
          break;
        case "ROLE_STAFF":
          router.replace("/dashboard/staff");
          break;
        case "ROLE_ADMIN":
          router.replace("/dashboard/admin");
          break;
        default:
          // Handle GUEST or unknown roles, maybe redirect to login or a generic info page
          router.replace("/login"); // Or some other appropriate default
          break;
      }
    }
    // If no user after loading, you might also redirect to login from here or rely on middleware
    if (!isLoading && !user) {
      router.replace("/login"); // Or your sign-in route
    }
  }, [user, isLoading, router]);

  // Display a loading message or a minimal UI while redirecting
  return <div>Loading your dashboard...</div>;
};

export default GenericDashboardPage;
