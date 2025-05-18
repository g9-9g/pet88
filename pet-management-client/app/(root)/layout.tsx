import { Toaster } from "@/components/ui/sonner";
import { UserProvider } from "@/context/UserContext";

const RootLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <UserProvider>
      <>
        {children}
        <Toaster />
      </>
    </UserProvider>
  );
};

export default RootLayout;
