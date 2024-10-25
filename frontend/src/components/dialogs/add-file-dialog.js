import { useState } from 'react';
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription} from "@/components/ui/dialog";
import {  Select,  SelectContent,  SelectGroup,  SelectItem,  SelectLabel,  SelectTrigger,  SelectValue,} from "@/components/ui/select"
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import AddFileButton from '../add-file-button';
import fileClient from '@/lib/fileClient'



const AddFileDialog = ({ onAddFile }) => {
  const [filename, setFilename] = useState("");
  const [language, setLanguage] = useState("");
  const [isOpen, setIsOpen] = useState(false);


  const handleAddFile = () => {
    if (filename && language) {
      
        console.log("Sending data:", filename);
       
        fileClient.createFile(filename,language).then(res => {
          onAddFile(res.data);
        });

      setFilename("");
      setLanguage("");
      setIsOpen(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen} >
      <DialogTrigger asChild>
        <AddFileButton />
      </DialogTrigger>

      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add New File</DialogTitle>
          <DialogDescription>Fill in the details to add a new File to the list.</DialogDescription>
        </DialogHeader>
        <div className="space-y-4">
          <div>
            <Label className="block text-sm font-medium text-gray-700">File name</Label>
            <Input
              type="text"
              value={filename}
              onChange={(e) => setFilename(e.target.value)}
              className="block w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm"
              placeholder="enter file name"
            />
          </div>
          <div>
            <Select value={language} onValueChange={setLanguage} className="block w-full mt-1">
              <SelectTrigger className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm">
                  <SelectValue placeholder="Select a language" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="java">Java</SelectItem>
                <SelectItem value="javascript">Javascript</SelectItem>
                <SelectItem value="python">Python</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <Button onClick={handleAddFile} className="w-full mt-4 text-white bg-blue-600 hover:bg-blue-700">Add File</Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default AddFileDialog;