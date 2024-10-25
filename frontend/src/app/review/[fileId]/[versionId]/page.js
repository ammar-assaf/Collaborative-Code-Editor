"use client";
import React, { useState, useEffect, useRef } from 'react';
import fileVersionClient from '@/lib/fileVersionClient';
import commentClient from '@/lib/commentClient';
import AceEditor from "react-ace";
import { useAuthGuard } from "@/lib/auth/use-auth";
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";

import "ace-builds/src-noconflict/theme-github";
import "ace-builds/src-noconflict/mode-javascript";
import "ace-builds/src-noconflict/ext-language_tools";

export default function Review({ params }) {
    const { fileId, versionId } = params;
    const [version, setVersion] = useState();
    const [code, setCode] = useState("");
    const [lineNumber, setLineNumber] = useState(0);
    const [selectedLine, setSelectedLine] = useState(""); 
    const [comment, setComment] = useState("");
    const editorRef = useRef(null);
    const { user } = useAuthGuard({ middleware: "auth" });


    const getLastLineNumber = () => {
        if (editorRef.current) {
            const editor = editorRef.current.editor;
            const currentLineNumber = editor.getSession().getLength();
            setLineNumber(currentLineNumber);
            console.log("line " + currentLineNumber); // Log the current line number
        }
    };

    const getVersionContent = async () => {
        try {
            const res = await fileVersionClient.getVersionContent(fileId, versionId);
            setCode(res.data);
        } catch (error) {
            console.error('Error fetching file content:', error);
        }
    };

    const getVersion = async () => {
        try {
            const res = await fileVersionClient.getVersion(fileId, versionId);
            setVersion(res.data);
            console.log(res.data);
        } catch (error) {
            console.error('Error fetching file content:', error);
        }
    };

    useEffect(() => {
        getVersion();
        getVersionContent();
    }, [fileId, versionId]);

    const handleEditorChange = (newCode) => {
        setCode(newCode);
        getLastLineNumber();
    };

    const handleCommentSubmit = async () => {
        try {

            const commentData = {
                content: comment,
                lineNumber: selectedLine,
                author: user,
                version: version
            };
            const res = await commentClient.addComment(versionId, commentData);
            console.log(res.data);  
            setComment("");
            setSelectedLine(''); 
            alert("Comment added");


        } catch (error) {
            if (error.response) {
                console.error('Error creating comment:', error.response.data);
                console.error('Status code:', error.response.status);
            } else {
                console.error('Error creating comment:', error.message);
            }
        }
    };

    useEffect(() => {
        if (code) {
            getLastLineNumber();
        }
    }, [code]);

    return (
        <div className="p-6">
            <h1 className="mb-4 text-2xl font-semibold">{version?.versionName}</h1>
            <p><strong>Modified By:</strong> {version?.modifiedBy.username}</p>
            <p><strong>Description:</strong> {version?.description}</p>
            <p><strong>Timestamp:</strong> {new Date(version?.timestamp).toLocaleString()}</p>

            <AceEditor
                ref={editorRef}
                mode={version?.file.language}
                theme="github"
                name="UNIQUE_ID_OF_DIV"
                fontSize={14}
                showPrintMargin
                showGutter
                highlightActiveLine
                value={code}
                onChange={handleEditorChange}
                setOptions={{
                    showLineNumbers: true,
                    readOnly: true,
                }}
            />
            <div className="grid w-full gap-2 mt-4">
                <Select value={selectedLine}  onValueChange={setSelectedLine}>
                    <SelectTrigger>
                        <SelectValue placeholder="Select a line number" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectLabel>Line Numbers</SelectLabel>
                            {[...Array(lineNumber).keys()].map((num) => (
                                <SelectItem key={num + 1} value={num + 1}>
                                    {num + 1}
                                </SelectItem>
                            ))}
                        </SelectGroup>
                    </SelectContent>
                </Select>

                <Textarea
                    placeholder="Type your comment here."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                />
                <Button onClick={handleCommentSubmit}>Add comment</Button>
            </div>
        </div>
    );
}
