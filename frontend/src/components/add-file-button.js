import { Button } from "@/components/ui/button";
import { PlusIcon } from 'lucide-react';

const AddFileButton = ({ onClick }) => (
  <Button
    onClick={onClick}
    className="fixed bottom-8 right-8 w-14 h-14 rounded-full bg-blue-600 text-white flex items-center justify-center shadow-lg hover:bg-blue-700"
    aria-label="Add Data"
  >
    <PlusIcon className="w-6 h-6" />
  </Button>
);

export default AddFileButton;