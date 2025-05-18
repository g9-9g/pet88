"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";

type FormType = "sign-in" | "sign-up";

// Define base form data type
type BaseFormData = {
  username: string;
  password: string;
};

// Define sign-up specific form data
type SignUpFormData = BaseFormData & {
  email: string;
  role: string;
};

// Define form data type based on form type
type FormData = {
  "sign-in": BaseFormData;
  "sign-up": SignUpFormData;
}[FormType];

// Define schemas
const baseSchema = {
  username: z.string().min(2, {
    message: "Username must be at least 2 characters.",
  }),
  password: z.string().min(6, {
    message: "Password must be at least 6 characters.",
  }),
};

const signInSchema = z.object(baseSchema);

const signUpSchema = z.object({
  ...baseSchema,
  email: z.string().email({
    message: "Please enter a valid email address.",
  }),
  role: z.string({
    required_error: "Please select a role.",
  }),
});

const role = "owner";

const AuthForm = ({ type }: { type: FormType }) => {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const form = useForm<FormData>({
    resolver: zodResolver(type === "sign-in" ? signInSchema : signUpSchema),
    defaultValues:
      type === "sign-in"
        ? {
            username: "",
            password: "",
          }
        : {
            username: "",
            password: "",
            email: "",
            role: "",
          },
  });

  const handleSignIn = async (values: BaseFormData) => {
    setIsLoading(true);
    setErrorMessage("");

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_ENDPOINT}/auth/signin`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(values),
        }
      );

      if (!response.ok) {
        throw new Error("Sign in failed");
      }

      // Handle successful sign in
      console.log("Sign in successful");
    } catch (error) {
      setErrorMessage("Failed to sign in. Please try again");
    } finally {
      setIsLoading(false);
      router.push(`/dashboard/${role}`);
    }
  };

  const handleSignUp = async (values: SignUpFormData) => {
    setIsLoading(true);
    setErrorMessage("");

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_ENDPOINT}/auth/signup`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(values),
        }
      );

      if (!response.ok) {
        throw new Error("Sign up failed");
      }

      // Handle successful sign up
      console.log("Sign up successful");
    } catch (error) {
      setErrorMessage("Failed to create account. Please try again");
    } finally {
      setIsLoading(false);
      router.push(`/dashboard/${role}`);
    }
  };

  const onSubmit = async (values: FormData) => {
    if (type === "sign-in") {
      await handleSignIn(values as BaseFormData);
    } else {
      await handleSignUp(values as SignUpFormData);
    }
  };

  return (
    <>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="flex max-h-[800px] w-full max-w-[580px] flex-col justify-center space-y-2 transition-all lg:h-full lg:space-y-4"
        >
          <h1 className="h1 text-center text-light-100 md:text-left">
            {type === "sign-in" ? "Sign In" : "Sign up"}
          </h1>

          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <div className="flex h-[78px] flex-col justify-center rounded-xl border border-light-300 px-4">
                  <FormLabel className="text-light-100 pt-2 body-2 w-full">
                    Username
                  </FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter your username"
                      className="border-none shadow-none p-0 outline-none ring-offset-transparent focus:ring-transparent focus:ring-offset-0 focus-visible:outline-none focus-visible:ring-0 focus-visible:ring-transparent focus-visible:ring-offset-0 placeholder:text-light-200 body-2"
                      {...field}
                    />
                  </FormControl>
                </div>
                <FormMessage className="text-red body-2 ml-4" />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <div className="flex h-[78px] flex-col justify-center rounded-xl border border-light-300 px-4">
                  <FormLabel className="text-light-100 pt-2 body-2 w-full">
                    Password
                  </FormLabel>
                  <FormControl>
                    <Input
                      type="password"
                      placeholder="Enter your password"
                      className="border-none shadow-none p-0 outline-none ring-offset-transparent focus:ring-transparent focus:ring-offset-0 focus-visible:outline-none focus-visible:ring-0 focus-visible:ring-transparent focus-visible:ring-offset-0 placeholder:text-light-200 body-2"
                      {...field}
                    />
                  </FormControl>
                </div>
                <FormMessage className="text-red body-2 ml-4" />
              </FormItem>
            )}
          />

          {type === "sign-up" && (
            <>
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex h-[78px] flex-col justify-center rounded-xl border border-light-300 px-4">
                      <FormLabel className="text-light-100 pt-2 body-2 w-full">
                        Email
                      </FormLabel>
                      <FormControl>
                        <Input
                          placeholder="Enter your email"
                          className="border-none shadow-none p-0 outline-none ring-offset-transparent focus:ring-transparent focus:ring-offset-0 focus-visible:outline-none focus-visible:ring-0 focus-visible:ring-transparent focus-visible:ring-offset-0 placeholder:text-light-200 body-2"
                          {...field}
                        />
                      </FormControl>
                    </div>
                    <FormMessage className="text-red body-2 ml-4" />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="role"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex h-[78px] flex-col justify-center rounded-xl border border-light-300 px-4">
                      <FormLabel className="text-light-100 pt-2 body-2 w-full">
                        Role
                      </FormLabel>
                      <FormControl>
                        <Select
                          onValueChange={field.onChange}
                          defaultValue={field.value}
                        >
                          <SelectTrigger className="border-none shadow-none p-0 outline-none ring-offset-transparent focus:ring-transparent focus:ring-offset-0 focus-visible:outline-none focus-visible:ring-0 focus-visible:ring-transparent focus-visible:ring-offset-0">
                            <SelectValue placeholder="Select your role" />
                          </SelectTrigger>
                          <SelectContent className="bg-white">
                            <SelectItem value="ROLE_PET_OWNER">
                              Pet Owner
                            </SelectItem>
                            <SelectItem value="ROLE_DOCTOR">Doctor</SelectItem>
                            <SelectItem value="ROLE_STAFF">Staff</SelectItem>
                          </SelectContent>
                        </Select>
                      </FormControl>
                    </div>
                    <FormMessage className="text-red body-2 ml-4" />
                  </FormItem>
                )}
              />
            </>
          )}

          <Button
            type="submit"
            className="bg-brand hover:bg-brand-100 transition-all rounded-full button h-[50px] cursor-pointer text-white"
            disabled={isLoading}
          >
            {type === "sign-in" ? "Sign In" : "Sign Up"}
            {isLoading && (
              <Image
                src="/assets/icons/loader.svg"
                alt="loader"
                width={24}
                height={24}
                className="ml-2 animate-spin"
              />
            )}
          </Button>

          {errorMessage && <p className="error-message">*{errorMessage}</p>}

          <div className="body-2 flex justify-center">
            <p className="text-light-100">
              {type === "sign-in"
                ? "Don't have an account?"
                : "Already have an account?"}
            </p>
            <Link
              href={type === "sign-in" ? "/sign-up" : "/sign-in"}
              className="ml-1 font-medium text-brand hover:underline"
            >
              {" "}
              {type === "sign-in" ? "Sign Up" : "Sign In"}
            </Link>
          </div>
        </form>
      </Form>
    </>
  );
};

export default AuthForm;
