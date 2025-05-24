import { Toaster } from "@/components/ui/sonner";

const RootLayout = ({ children }: { children: React.ReactNode }) => {
  return (
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
  );
};

export default RootLayout;
