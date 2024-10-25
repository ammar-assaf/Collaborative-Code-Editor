"use client";
import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import commentClient from '@/lib/commentClient';

export default function CommentList({ versionId }) {
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchComments = async () => {
            try {
                const res = await commentClient.getComments(versionId);
                if (Array.isArray(res.data)) {
                    setComments(res.data);
                } else {
                    setComments([]);
                }
            } catch (err) {
                console.error(err);
                setError('Failed to load comments. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchComments();
    }, [versionId]);

    if (loading) {
        return (
            <div className="flex justify-center items-center mt-8">
                <div className="loader">Loading comments...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="text-red-600 text-center mt-8">
                <p>{error}</p>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto mt-8 p-4">
            {comments.length === 0 ? (
                <p className="text-center text-gray-500">No comments available.</p>
            ) : (
                comments.map((comment, index) => (
                    <Card key={index} className="mb-4 transition-transform transform hover:scale-105">
                        <CardHeader>
                            <CardTitle>Comment {index + 1}</CardTitle>
                            <CardDescription>Author: {comment.author.username}</CardDescription>
                        </CardHeader>
                        <CardContent>
                            <p className="text-gray-700">Line Number: <strong>{comment.lineNumber}</strong></p>
                            <p className="text-gray-800">Content: {comment.content}</p>
                        </CardContent>
                        <CardFooter>
                            <p className="text-gray-500">Timestamp: {new Date(comment.timestamp).toLocaleString()}</p>
                        </CardFooter>
                    </Card>
                ))
            )}
        </div>
    );
}
