"use client";
import { useState } from 'react';
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";




const SaveDialog = ({ open, onOpenChange, onSave }) => {
   const [message, setMessage] = useState("");

  const handleSaveFile = () => {
     if (message) {
              onSave(message);
      

        setMessage("");
        onOpenChange(false);

     }

  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogTrigger  onClick={(e) => e.stopPropagation()}>
      </DialogTrigger>

      <DialogContent>
        <DialogHeader>
          <DialogTitle>save file</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <div>
            <Label className="block text-sm font-medium text-gray-700">Message</Label>
            <Input
              type="text"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              className="block w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm"
              placeholder="write a message"
            />
          </div>
          
          <Button onClick={handleSaveFile} className="w-full mt-4 text-white bg-blue-600 hover:bg-blue-700">save</Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default SaveDialog;