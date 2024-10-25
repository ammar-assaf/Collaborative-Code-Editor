"use client";
import CodeEditor from '@/components/code-editor';
import { useState, useEffect } from 'react';
import fileClient from '@/lib/fileClient';

export default function Editor({ params }) {
  const { id } = params;
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getFile = async (id) => {
      setLoading(true);
      setError(null);
      try {
        const res = await fileClient.getFileById(id);
        setFile(res.data);
      } catch (error) {
        console.error('Error fetching user file:', error);
        setError('Could not load the file. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    getFile(id);
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div className="text-red-600">{error}</div>;
  }

  if (!file) {
    return <div>File not found</div>;
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-lg font-semibold">{file.filename}</h1>
      <p><strong>Owner:</strong> {file.owner.username}</p>
      <p><strong>Language:</strong> {file.language}</p>
      <br />
      <CodeEditor file={file} />
    </div>
  );
}
