import Navbar from "@/components/layout/Navbar";
import { Toaster } from "@/components/ui/sonner";

export default function MarketingLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <main className="flex h-screen">
      <section className="flex h-full flex-1 flex-col">
        <Navbar />
        <div className="main-content">{children}</div>
      </section>
      <Toaster />
    </main>
  );
}
