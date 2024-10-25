"use client";
import React, { useState, useEffect } from 'react';
import fileVersionClient from '@/lib/fileVersionClient';
import AceEditor from "react-ace";
import CommentList from '@/components/lists/commentList';
import "ace-builds/src-noconflict/theme-github";
import "ace-builds/src-noconflict/mode-javascript";
import "ace-builds/src-noconflict/ext-language_tools";

export default function Version({ params }) {
    const { fileId, versionId } = params;
    const [version, setVersion] = useState(null);
    const [code, setCode] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const versionResponse = await fileVersionClient.getVersion(fileId, versionId);
                const codeResponse = await fileVersionClient.getVersionContent(fileId, versionId);
                setVersion(versionResponse.data);
                setCode(codeResponse.data);
            } catch (err) {
                setError('Failed to load version details. Please try again later.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        
        fetchData();
    }, [fileId, versionId]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="loader">Loading version...</div>
            </div>
        );
    }

    if (error || !version) {
        return (
            <div className="text-center text-red-600 mt-8">
                <p>{error || 'Version not found.'}</p>
            </div>
        );
    }

    return (
        <div className="max-w-3xl p-4 mx-auto bg-white shadow-lg rounded-lg">
            <h4 className="mb-4 text-2xl font-bold">{version.versionName}</h4>
            <div className="mb-4">
                <p className="font-medium"><strong>Modified By:</strong> {version.modifiedBy.username}</p>
                <p className="font-medium"><strong>Description:</strong> {version.description}</p>
                <p className="font-medium"><strong>Timestamp:</strong> {new Date(version.timestamp).toLocaleString()}</p>
            </div>

            <div className="my-4 overflow-hidden border border-gray-300 rounded-lg h-[300px] ">
                <AceEditor
                    mode={version.file.language}
                    theme="github"
                    name="codeEditor"
                    fontSize={14}
                    showPrintMargin
                    showGutter
                    highlightActiveLine
                    value={code}
                    setOptions={{
                        showLineNumbers: true,
                        readOnly: true,
                    }}
                    style={{ width: '100%', height: '100%' }} 
                />
            </div>


            <div className="mt-8">
                <h2 className="text-xl font-semibold mb-2">Comments</h2>
                <div className="h-[300px] overflow-y-auto border border-gray-300 rounded-lg p-4">
                    <CommentList versionId={versionId} />
                </div>
            </div>
        </div>
    );
}
