"use client";
import { MoreVertical } from 'lucide-react';
import { useState, useEffect } from 'react';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { useRouter } from 'next/navigation';
import { Label } from "@/components/ui/label";
import ShareFileDialog from '@/components/dialogs/share-file-dialog';
import { useAuthGuard } from "@/lib/auth/use-auth";

const FileList = ({ selected, files, onDelete, onShare, onFork }) => {
  const [selectedIndex, setSelectedIndex] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const { user } = useAuthGuard({ middleware: "auth" });
  const [isOwner, setIsOwner] = useState(false);
  const router = useRouter();

  useEffect(() => {
    setIsOwner(selected === 'owner');
    console.log("ff ", files);
  }, [selected]);

  const handleFileClick = (file, index) => {
    setSelectedIndex(index);
    router.push(`/editor/${file.id}`);
  };

  const handleHistoryClick = (fileId) => {
    router.push(`/history/${fileId}`);
  };

  const handleShareClick = (file) => {
    setSelectedFile(file);
    setDialogOpen(true);
  };

  const filteredFiles = files.filter(file => {
    if (selected === 'shared') {
      return file.sharedWith.some((shared) => shared.user.email === user.email && shared.permission !== 'OWNER');
    } else if (selected === 'owner') {
      return file.owner.email === user.email;
    }
    return true;
  });

  return (
    <div className="p-4 rounded-lg shadow-md bg-gray-50">
      <div className="flex items-center justify-between p-4 border-b">
        <Label className="flex-1 font-bold truncate">Title</Label>
        <Label className="flex-1 font-bold text-center truncate">Owner</Label>
        <Label className="flex-1 font-bold text-center truncate">Language</Label>
        <div className="flex-shrink-0" />
      </div>

      <div className="h-64 mt-2 space-y-2 overflow-y-auto"> 
        {filteredFiles.map((file, index) => (
          <div
            key={file.id}
            className={`flex justify-between items-center p-4 bg-white rounded-md shadow-sm transition-transform transform hover:scale-105 ${
              selectedIndex === index ? 'bg-blue-100' : ''
            } cursor-pointer`}
            onClick={() => handleFileClick(file, index)}
            role="button"
            tabIndex={0}
            onKeyDown={(e) => e.key === 'Enter' && handleFileClick(file, index)}
          >
            <Label className="flex-1 text-sm font-medium truncate">{file.filename}</Label>
            <Label className="flex-1 text-sm text-center text-gray-600 truncate">{file.owner.username}</Label>
            <Label className="flex-1 text-sm text-center text-gray-600 truncate">{file.language}</Label>

            <DropdownMenu>
              <DropdownMenuTrigger
                className="text-gray-500 hover:text-gray-700"
                onClick={(e) => e.stopPropagation()}
                aria-label="More options"
              >
                <MoreVertical />
              </DropdownMenuTrigger>
              <DropdownMenuContent className="w-32">
                <DropdownMenuItem onClick={(e) => { e.stopPropagation(); handleHistoryClick(file.id); }}>History</DropdownMenuItem>

                {isOwner ? (
                  <>
                    <DropdownMenuItem onClick={(e) => { e.stopPropagation(); handleShareClick(file); }}>Share</DropdownMenuItem>
                    <DropdownMenuItem onClick={(e) => { e.stopPropagation(); onDelete(file.id); }} className="text-red-600">Delete</DropdownMenuItem>
                  </>
                ) : (
                  <DropdownMenuItem onClick={(e) => { e.stopPropagation(); onFork(file.id); }} className="text-gray-600">Fork</DropdownMenuItem>
                )}
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        ))}
      </div>

      {selectedFile && (
        <ShareFileDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          file={selectedFile}
          onShare={onShare}
        />
      )}
    </div>
  );
};

export default FileList;
