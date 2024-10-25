"use client";

import { useAuthGuard } from '@/lib/auth/use-auth';
import React from 'react';

export default function RoleGuard({ rolesAllowed, children }) {
  const { user } = useAuthGuard({ middleware: 'guest' });

  if (!rolesAllowed) return null;

  const isAllowed = rolesAllowed.includes(user?.role);

  if (isAllowed) return children;

  return null;
}
