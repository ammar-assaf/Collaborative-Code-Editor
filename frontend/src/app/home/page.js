"use client";
import FileList from '@/components/lists/fileList';
import AddFileDialog from '@/components/dialogs/add-file-dialog';
import { useState, useEffect } from 'react';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import fileClient from '@/lib/fileClient';
import fileVersionClient from '@/lib/fileVersionClient';

export default function Home() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState('owner');

  useEffect(() => {
    const getAllUserFiles = async () => {
      try {
        const res = await fileClient.getUserFiles();
        console.log('API Response:', res.data);
        setFiles(res.data);
      } catch (error) {
        console.error('Error fetching user files:', error);
      } finally {
        setLoading(false);
      }
    };

    getAllUserFiles();
  }, []);

  const addFile = (newFile) => {
    setFiles(prevFiles => [...prevFiles, newFile]);
  };

  const deleteFile = async (fileToDelete) => {
    try {
      console.log(`dddddd ${fileToDelete}`)
      await fileClient.deleteFileById(fileToDelete);
      setFiles(prevFiles => prevFiles.filter(file => file.id !== fileToDelete));
    } catch (error) {
      console.error('Error deleting file:', error.message || error);
    }
  };

  const shareFile = async (id, fileToShare) => {
    try {
      await fileClient.addUserPermission(id, fileToShare);
    } catch (error) {
      console.error('Error sharing file:', error);
    }
  };

  const forkFile = async (id) => {
    try {
      await fileVersionClient.forkFile(id);
    } catch (error) {
      console.error('Error forking file:', error);
    }
  };

  return (
    <div className="h-screen p-6 bg-gray-50">
      <div className="flex items-center justify-between p-4">
        <h1 className="text-lg font-semibold">Recent Files</h1>
        <Select value={selected} onValueChange={setSelected} className="block w-full mt-1">
          <SelectTrigger className="w-1/4 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring focus:border-blue-500 sm:text-sm">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="owner">Owned by me</SelectItem>
            <SelectItem value="shared">Shared with me</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {loading ? (
        <p>Loading files...</p>
      ) : files.length === 0 ? (
        <p>No files available.</p>
      ) : (
        <FileList
          selected={selected}
          files={files}
          onDelete={deleteFile}
          onShare={shareFile}
          onFork={forkFile}
        />
      )}
      
      <div className="fixed bottom-4 right-4">
        <AddFileDialog onAddFile={addFile} />
      </div>
    </div>
  );
}
