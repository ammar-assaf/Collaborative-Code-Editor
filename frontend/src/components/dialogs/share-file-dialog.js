"use client";
import { useState } from 'react';
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription} from "@/components/ui/dialog";
import {  Select,  SelectContent,  SelectGroup,  SelectItem,  SelectLabel,  SelectTrigger,  SelectValue,} from "@/components/ui/select"
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from "@/components/ui/button";



const ShareFileDialog = ({ open, onOpenChange, file, onShare }) => {
   const [email, setEmail] = useState("");
   const [permission, setPermission] = useState("");
  const handleShareFile = () => {
     if (email && permission) {
      
      onShare(file.id,{user:{email},permission:permission});
      

        setEmail("");
        setPermission("");
        onOpenChange(false);

     }

  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogTrigger  onClick={(e) => e.stopPropagation()}>
      </DialogTrigger>

      <DialogContent>
        <DialogHeader>
          <DialogTitle>Share File</DialogTitle>
          <DialogDescription className = "text-black">Fill in the details to share a File.</DialogDescription>
        </DialogHeader>
        <div className="space-y-4">
          <div>
            <Label className="block text-sm font-medium text-gray-700">User Email</Label>
            <Input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="block w-full px-3 py-2 mt-1 text-gray-700 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm"
              placeholder="enter user email"
            />
          </div>
          <div>
            <Select value={permission} onValueChange={setPermission} className="block w-full mt-1">
              <SelectTrigger className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm">
                  <SelectValue placeholder="Select a permission" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="EDIT">Edit</SelectItem>
                <SelectItem value="VIEW">view</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <Button onClick={handleShareFile} className="w-full mt-4 text-white bg-blue-600 hover:bg-blue-700">Share File</Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ShareFileDialog;