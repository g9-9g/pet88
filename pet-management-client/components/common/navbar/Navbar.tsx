import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { GiCat, GiHamburgerMenu } from "react-icons/gi";
import { GrFormClose } from "react-icons/gr";
import Icon from "../Icon";

const navigation = [
  { name: "Home", href: "#", current: true },
  { name: "About Us", href: "#", current: false },
  { name: "Services", href: "#", current: false },
  { name: "Pricing", href: "#", current: false },
  { name: "Testimonial", href: "#", current: false },
];

export default function Navbar() {
  return (
    <header className="bg-white">
      <>
        <div className="min-w-7xl mx-auto border-b border-gray-50 bg-white px-2 sm:px-6 lg:px-8">
          <div className="relative mx-0 flex h-16 items-center justify-between md:mx-20">
            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
              {/* Mobile menu button*/}
              <Button className="inline-flex items-center justify-center rounded-md p-2 text-brand hover:bg-brand hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                <span className="sr-only">Open main menu</span>
                <GiHamburgerMenu className="block h-6 w-6" aria-hidden="true" />
              </Button>
            </div>
            <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
              <div className="flex flex-shrink-0 items-center">
                <Icon />
              </div>
              <div className="hidden sm:ml-6 sm:block md:ml-60">
                <div className="flex space-x-4">
                  {navigation.map((item) => (
                    <Link
                      key={item.name}
                      href={item.href}
                      className={cn(
                        item.current
                          ? "bg-brand text-white shadow-lg"
                          : "text-gray-500 hover:bg-brand hover:text-white hover:shadow-lg",
                        "rounded-full px-3 py-2 text-sm font-medium"
                      )}
                      aria-current={item.current ? "page" : undefined}
                    >
                      {item.name}
                    </Link>
                  ))}
                </div>
              </div>
            </div>
            <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
              <Link href="/sign-in">
                <Button className="rounded-full border border-orange-100 px-3 py-2 text-base font-medium text-brand hover:bg-brand hover:text-white hover:shadow-lg">
                  Register
                </Button>
              </Link>
            </div>
          </div>
        </div>

        <div className="sm:hidden">
          <div className="space-y-1 px-2 pt-2 pb-3">
            {navigation.map((item) => (
              <Button
                key={item.name}
                className={cn(
                  item.current
                    ? "bg-brand text-white shadow-lg"
                    : "text-gray-500 hover:bg-brand hover:text-white hover:shadow-lg",
                  "block rounded-md px-3 py-2 text-base font-medium"
                )}
                aria-current={item.current ? "page" : undefined}
              >
                {item.name}
              </Button>
            ))}
          </div>
        </div>
      </>
    </header>
  );
}
