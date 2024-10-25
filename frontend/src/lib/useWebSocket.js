import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';

const useWebSocket = (fileId, onMessageReceived) => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false);

    useEffect(() => {
        const socket = new SockJS('http://backend:8080/ws/code-editor');

        const loadStomp = async () => {
            const { default: Stomp } = await import('stompjs');
            const client = Stomp.over(socket);

            client.connect({}, () => {
                setConnected(true);
                setStompClient(client);
                console.log('Connected to WebSocket');

                client.subscribe(`/topic/files/${fileId}`, (message) => {
                    const parsedMessage = JSON.parse(message.body);
                    onMessageReceived(parsedMessage);
                    console.log('Received message:', parsedMessage);
                });
            }, (error) => {
                console.error('WebSocket connection error:', error);
            });
        };

        loadStomp();

        return () => {
            if (stompClient) {
                stompClient.disconnect(() => {
                    setConnected(false);
                    console.log('Disconnected from WebSocket');
                });
            }
        };
    }, [fileId]);

    const sendMessage = (command, content, userEmail, description = null) => {
        if (stompClient && connected) {

            const message = {
                fileId,
                command,
                content,
                userEmail,
                description,
            };

            stompClient.send(`/app/files/${fileId}`, {}, JSON.stringify(message));
            console.log('Sent message:', message);
        } else {
            console.error('WebSocket is not connected');
        }
    };

    return { sendMessage, connected };
};

export default useWebSocket;
