import { Toaster } from "@/components/ui/sonner";
import { UserProvider } from "@/context/UserContext";

const RootLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <UserProvider>
      <>
        {children}
        <Toaster
          toastOptions={{
            className: "!text-sm",
            classNames: {
              success: "!bg-green-500 !text-white",
              error: "!bg-brand !text-white",
            },
          }}
        />
      </>
    </UserProvider>
  );
};

export default RootLayout;
