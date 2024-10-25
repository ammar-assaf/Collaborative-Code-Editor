"use client";
import fileVersionClient from '@/lib/fileVersionClient';
import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { useRouter } from 'next/navigation';

export default function VersionList({ fileId }) {
  const [versions, setVersions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const router = useRouter();

  useEffect(() => {
    const fetchVersions = async () => {
      try {
        const res = await fileVersionClient.getFileVersions(fileId);
        if (Array.isArray(res.data)) {
          setVersions(res.data);
          console.log(res.data);
        } else {
          console.error('Expected res.data to be an array', res.data);
          setError('No versions found.');
        }
      } catch (error) {
        console.error('Error fetching file versions:', error);
        setError('Error fetching file versions.');
      } finally {
        setLoading(false);
      }
    };

    fetchVersions();
  }, [fileId]);

  const handleCardClick = (version) => {
    router.push(`/version/${version.file.id}/${version.id}`);
  };

  const handleButtonClick = (version) => {
    router.push(`/review/${version.file.id}/${version.id}`);
  };

  return (
    <div className="max-w-3xl mx-auto mt-8">
      {loading && <div className="text-center">Loading versions...</div>}
      {error && <div className="text-center text-red-600">{error}</div>}
      {!loading && !error && versions.length === 0 && (
        <div className="text-center">No versions available.</div>
      )}
      {versions.map((ver, index) => (
        <Card 
          key={ver.versionId} 
          onClick={() => handleCardClick(ver)} 
          className="mb-4 transition-shadow cursor-pointer hover:shadow-lg"
        >
          <CardHeader>
            <CardTitle>{ver.versionName}</CardTitle>
            <CardDescription>File Name: {ver.file.filename}</CardDescription>
          </CardHeader>
          <CardContent>
            <p>Modified By: {ver.modifiedBy.username}</p>
            <p>Description: {ver.description}</p>
          </CardContent>
          <CardFooter className="flex justify-between items-center">
            <p>Timestamp: {new Date(ver.timestamp).toLocaleString()}</p>
            <Button onClick={(e) => { e.stopPropagation(); handleButtonClick(ver); }}>
              Review
            </Button>
          </CardFooter>
        </Card>
      ))}
    </div>
  );
}
