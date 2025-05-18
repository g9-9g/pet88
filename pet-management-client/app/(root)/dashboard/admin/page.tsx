"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Calendar,
  Clock,
  PawPrint,
  Users,
  Activity,
  DollarSign,
} from "lucide-react";

const overviewCards = [
  {
    title: "Total Appointments",
    value: "248",
    description: "↗️ 12% from last month",
    icon: Calendar,
  },
  {
    title: "Active Pets",
    value: "357",
    description: "↗️ 8% from last month",
    icon: PawPrint,
  },
  {
    title: "Available Doctors",
    value: "12",
    description: "Online now",
    icon: Users,
  },
  {
    title: "Average Wait Time",
    value: "24m",
    description: "↘️ 3% from last month",
    icon: Clock,
  },
];

export default function AdminDashboardPage() {
  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Admin Dashboard</h2>
        <p className="text-muted-foreground">
          Welcome back! Here's an overview of your pet care center.
        </p>
      </div>

      {/* Overview Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {overviewCards.map((card) => (
          <Card key={card.title}>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {card.title}
              </CardTitle>
              <card.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{card.value}</div>
              <p className="text-xs text-muted-foreground">
                {card.description}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Recent Activity and Upcoming Appointments */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <Activity className="h-5 w-5 text-brand" />
              <CardTitle>Recent Activity</CardTitle>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {/* Placeholder activities */}
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="flex items-center gap-4 rounded-lg border p-3"
                >
                  <div className="h-8 w-8 rounded-full bg-gray-100" />
                  <div>
                    <p className="text-sm font-medium">
                      New appointment scheduled
                    </p>
                    <p className="text-xs text-gray-500">2 hours ago</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <DollarSign className="h-5 w-5 text-brand" />
              <CardTitle>Revenue Overview</CardTitle>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {/* Placeholder revenue items */}
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="flex items-center justify-between rounded-lg border p-3"
                >
                  <div className="flex items-center gap-3">
                    <div className="h-8 w-8 rounded-full bg-gray-100" />
                    <div>
                      <p className="text-sm font-medium">Pet Checkup</p>
                      <p className="text-xs text-gray-500">30 mins ago</p>
                    </div>
                  </div>
                  <p className="font-medium text-brand">$120.00</p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
