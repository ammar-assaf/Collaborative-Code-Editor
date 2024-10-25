import React from 'react';
import { useRouter } from 'next/navigation';
import RoleGuard from '../role-guard';
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuTrigger } from '../ui/dropdown-menu';
import { Button } from '../ui/button';

export default function AdminNav() {
  const router = useRouter();

  const handleHomeClick = () => {
    console.log('Home clicked');
    router.push('/home');
  };

  return (
    <RoleGuard rolesAllowed={['ADMIN']}>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost" className="relative mx-2">
            Admin
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent className="w-56" align="end" forceMount>
          <DropdownMenuGroup>
            <DropdownMenuItem onClick={handleHomeClick}>
              Home
            </DropdownMenuItem>
          </DropdownMenuGroup>
        </DropdownMenuContent>
      </DropdownMenu>
    </RoleGuard>
  );
}
