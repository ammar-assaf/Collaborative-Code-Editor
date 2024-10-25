"use client";
import React, { useState, useEffect } from 'react';
import fileClient from '@/lib/fileClient';
import VersionList from '@/components/lists/versionList';

export default function History({ params }) {
  const { id } = params;
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const getFile = async () => {
      try {
        const res = await fileClient.getFileById(id);
        setFile(res.data);
      } catch (err) {
        setError('Failed to load file information.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    getFile();
  }, [id]);

  if (loading) {
    return <div className="text-center">Loading...</div>;
  }

  if (error || !file) {
    return <div className="text-center text-red-600">{error || 'File not found'}</div>;
  }

  return (
    <div className="max-w-2xl p-6 mx-auto">
      <h1 className="mb-4 text-2xl font-semibold">{file.filename}</h1>
      <div className="mb-2">
        <p><strong>Owner:</strong> {file.owner.username}</p>
        <p><strong>Language:</strong> {file.language}</p>
      </div>
      <hr className="my-4" />
      <VersionList fileId={id} />
    </div>
  );
}
