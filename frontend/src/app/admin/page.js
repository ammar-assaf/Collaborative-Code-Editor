"use client";
import React, { useState, useEffect } from 'react';
import httpClient from '@/lib/httpClient';
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import AppPagination from '@/components/appPagination';
import useSWR from "swr";


function Admin() {
  const [page, setPage] = useState(0);
  const { data } = useSWR(`/api/admin/users?page=${page}`, () => {
    return httpClient
      .get("/api/admin/users", {
        params: { page },
      })
      .then((res) => res.data);
  });

  return (
    
    <div className="min-h-screen p-6 bg-gray-100">
      {console.log(data)}
      <h1 className="mb-6 text-3xl font-bold text-center">Admin Dashboard</h1>
      <h2 className="mb-4 text-2xl font-semibold">Users</h2>
      <Table>
        <TableCaption>A list of registered users</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead>#</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>First Name</TableHead>
            <TableHead>Last Name</TableHead>
            <TableHead>Role</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data?.data?.length > 0 ? (
            data.data.map((user) => (
              <TableRow key={user.id} className="transition duration-200 hover:bg-gray-200">
                <TableCell>{user.id}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.firstName}</TableCell>
                <TableCell>{user.lastName}</TableCell>
                <TableCell>{user.role}</TableCell>
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={5} className="text-center text-gray-500">
                No users found
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>

      {data && (
        <div className="flex flex-col items-center justify-between mt-4 md:flex-row">
          <AppPagination
            hasNext={data.page + 1 < data?.totalPages}
            hasPrevious={data?.page >= 1}
            onNextPage={() => setPage(page + 1)}
            onPreviousPage={() => setPage(page - 1)}
          />
        </div>
      )}
    </div>
  );
}

export default Admin;
