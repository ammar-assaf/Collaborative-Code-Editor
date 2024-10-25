"use client";
import Link from 'next/link';
import React from 'react';
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";
import { Button } from "@/components/ui/button"

export default function Login() {
   const getProviderLoginUrl = (provider) =>{
        return process.env.NEXT_PUBLIC_BASE_URL + `/oauth2/authorization/${provider}`;
      };

  return (
    <div className="flex flex-col items-center justify-center h-screen gap-y-2">
      <Link href={getProviderLoginUrl('google')}>
          <Button variant="outline" type="button" className="relative flex items-center mb-2 text-lg text-gray-800 transition-shadow duration-150 ease-in-out bg-white border border-gray-300 rounded-lg outline-none cursor-pointer w-80 h-13 dark:focus:ring-gray-600 dark:bg-gray-800 dark:border-gray-700 dark:text-white dark:hover:bg-gray-700">
            <FcGoogle className="absolute transform -translate-x-1/2 -translate-y-1/2 left-6 top-1/2" />
            <span className = "relative text-left">Continue with Google</span>
          </Button>
        </Link>
        
        <Link href={getProviderLoginUrl('github')}>
          <Button variant="outline" type="button" className="relative flex items-center mb-2 text-lg text-gray-800 transition-shadow duration-150 ease-in-out bg-white border border-gray-300 rounded-lg outline-none cursor-pointer w-80 h-13 dark:focus:ring-gray-600 dark:bg-gray-800 dark:border-gray-700 dark:text-white dark:hover:bg-gray-700">
            <FaGithub className="absolute transform -translate-x-1/2 -translate-y-1/2 left-6 top-1/2" />
            <span className = "relative text-left">Continue with Github</span>
          </Button>
        </Link>
     
    </div>
  );
}
