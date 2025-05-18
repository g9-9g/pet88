"use client";

import Header from "@/components/layout/Header";
import Sidebar from "@/components/layout/Sidebar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <main className="flex h-screen">
      <Sidebar
        fullName="John Doe"
        avatar="/assets/images/Seia_ball.png"
        email="john.doe@example.com"
      />
      <section className="flex h-full flex-1 flex-col">
        <Header ownerId="123" accountId="123" />
        <div className="main-content">{children}</div>
      </section>
    </main>
  );
}
