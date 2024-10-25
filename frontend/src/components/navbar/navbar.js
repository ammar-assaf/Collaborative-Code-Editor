"use client";
import React from "react";
import ModeToggle from "../mode-toggle";
import { useAuthGuard } from "@/lib/auth/use-auth";
import { UserNav } from "./user-nav";
import AdminNav from "./admin-nav";

export default function Navbar() {
  const { user } = useAuthGuard({ middleware: "guest" });
  
  return (
    <nav className="flex items-center justify-between p-4 bg-white shadow-md dark:bg-gray-800">
      
      <div className="flex items-center gap-x-4">
        <ModeToggle />
        
         <AdminNav />
        {user && user.role && <UserNav />}
      </div>
      
      <div className="text-lg font-bold text-gray-800 dark:text-white">Collaborative Code Editor</div>

    </nav>
  );
}
