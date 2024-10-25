"use client";
import React, { useState, useEffect } from 'react';
import AceEditor from "react-ace";
import useWebSocket from '@/lib/useWebSocket';
import { useAuthGuard } from "@/lib/auth/use-auth";
import fileClient from '@/lib/fileClient';
import SaveDialog from './dialogs/save-dialog';
import { Button } from '@/components/ui/button';

import "ace-builds/src-noconflict/theme-github";
import "ace-builds/src-noconflict/mode-javascript";
import "ace-builds/src-noconflict/ext-language_tools";

const CodeEditor = ({ file }) => {
  const [code, setCode] = useState('');
  const [output, setOutput] = useState('');
  const { user } = useAuthGuard({ middleware: "auth" });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const isViewer = file?.sharedWith?.some(shared => shared.user.email === user.email && shared.permission === 'VIEW');
  const isOwner = file?.sharedWith?.some(shared => shared.user.email === user.email && shared.permission === 'OWNER');

  const handleReceiveMessage = (message) => {
    const { command, content: receivedContent, user } = message;

    switch (command) {
      case 'EDIT':
        setCode(receivedContent);
        break;
      case 'SAVE':
        setCode(receivedContent);
        console.log(`File saved by ${user}`);
        break;
      case 'EXECUTE':
        setOutput(receivedContent);
        break;
      default:
        setError(`Unknown command: ${command}`);
        console.error(`Unknown command: ${command}`);
        break;
    }
  };

  const { sendMessage, connected } = useWebSocket(file.id, handleReceiveMessage);

  useEffect(() => {
    console.log('File :', file);
    setLoading(true);
    fileClient.getFileContent(file.id)
      .then(res => {
        console.log(res.data);
        setCode(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to load file content.');
        console.error(err);
        setLoading(false);
      });
  }, [file.id]);

  const handleSave = () => {
    setDialogOpen(true);
  };

  const onSave = (description) => {
    sendMessage('SAVE', code, user.email, description);
    setDialogOpen(false);
  };

  const runCode = () => {
    setLoading(true);
    sendMessage('EXECUTE', code, user.email);
    setLoading(false);
  };

  const onChange = (editor) => {
    if (connected) {
      sendMessage('EDIT', editor, user.email);
    } else {
      console.warn('Not connected to WebSocket');
    }
  };

  return (
    <div className="editor-container">
      {error && <div className="error-message">{error}</div>}

      <div className="flex h-screen p-4 bg-gray-100">
        <div className="flex flex-col flex-1 mr-4">
          <div className="flex mb-4 space-x-2 action-buttons">
            {!isViewer && (
              <>
                <Button onClick={runCode} className="action-button" disabled={loading || !isOwner}>
                  {loading ? 'Running...' : 'Run Code'}
                </Button>
                <Button onClick={handleSave} className="action-button">Save</Button>
              </>
            )}
          </div>
          <div className="flex-1 overflow-hidden bg-white border border-gray-300 rounded-lg shadow-md">
            <AceEditor
              mode={file.language}
              theme="github"
              name="codeEditor"
              onChange={onChange}
              fontSize={16}
              showPrintMargin={false}
              showGutter={true}
              highlightActiveLine={true}
              value={code}
              readOnly={isViewer}
              setOptions={{
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true,
                enableSnippets: true,
                showLineNumbers: true,
                tabSize: 2,
              }}
              style={{ width: '100%', height: '100%' }}
            />
          </div>
        </div>

        <div className="flex flex-col flex-1">
          <h4 className="mb-4 text-xl font-semibold">Output:</h4>
          <div className="flex-1 overflow-hidden bg-white border border-gray-300 rounded-lg shadow-md">
            <pre className="p-4 text-sm output-text">{output || 'No output yet.'}</pre>
          </div>
        </div>
      </div>

      <SaveDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        onSave={onSave}
      />
    </div>
  );
};

export default CodeEditor;