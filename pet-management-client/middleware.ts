import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// Define protected routes and their allowed roles
const protectedRoutes = {
  "/dashboard": ["ROLE_PET_OWNER", "ROLE_DOCTOR", "ROLE_STAFF"],
  "/appointments": ["ROLE_PET_OWNER", "ROLE_DOCTOR", "ROLE_STAFF"],
  "/pets": ["ROLE_PET_OWNER"],
  "/medical-records": ["ROLE_DOCTOR", "ROLE_STAFF"],
  "/staff": ["ROLE_STAFF"],
};

export function middleware(request: NextRequest) {
  // const token = request.cookies.get("token")?.value;
  // const userRole = request.cookies.get("userRole")?.value;

  // // If no token, redirect to sign-in
  // if (!token) {
  //   return NextResponse.redirect(new URL("/sign-in", request.url));
  // }

  // // Check if the current path is protected
  // const currentPath = request.nextUrl.pathname;
  // const isProtectedRoute = Object.keys(protectedRoutes).some((route) =>
  //   currentPath.startsWith(route)
  // );

  // if (isProtectedRoute) {
  //   // Find the matching protected route
  //   const matchingRoute = Object.entries(protectedRoutes).find(([route]) =>
  //     currentPath.startsWith(route)
  //   );

  //   if (matchingRoute) {
  //     const [, allowedRoles] = matchingRoute;

  //     // Check if user's role is allowed for this route
  //     if (!userRole || !allowedRoles.includes(userRole)) {
  //       // Redirect to unauthorized page or dashboard
  //       return NextResponse.redirect(new URL("/unauthorized", request.url));
  //     }
  //   }
  // }

  return NextResponse.next();
}

// Configure which routes to run middleware on
export const config = {
  matcher: [
    "/dashboard/:path*",
    "/appointments/:path*",
    "/pets/:path*",
    "/medical-records/:path*",
    "/staff/:path*",
  ],
};
