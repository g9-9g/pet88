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
      <Sidebar />
      <section className="flex h-full flex-1 flex-col">
        <Header />
        <div className="main-content">{children}</div>
      </section>
    </main>
  );
}
