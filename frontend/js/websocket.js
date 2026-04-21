// WebSocket Configuration
let stompClient = null;
let isConnected = false;

// Connect to WebSocket
function connectWebSocket() {
    const socket = new SockJS('https://fsd-cloudchat.onrender.com/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    console.log('WebSocket connected');
    isConnected = true;
    
    // Subscribe to public topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    
    // Announce user joined
    const joinMessage = {
        channelId: localStorage.getItem('selectedChannelId') || '',
        senderId: currentUser.id,
        senderName: currentUser.username,
        type: 'JOIN'
    };
    stompClient.send('/app/chat.addUser', {}, JSON.stringify(joinMessage));
}

function onError(error) {
    console.error('WebSocket error:', error);
    isConnected = false;
    setTimeout(connectWebSocket, 5000); // Retry after 5 seconds
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    handleReceivedMessage(message);
}

function sendChatMessage(channelId, content) {
    if (!stompClient || !isConnected) {
        console.error('WebSocket not connected');
        return;
    }
    
    const message = {
        id: null,
        channelId: channelId,
        senderId: currentUser.id,
        senderName: currentUser.username,
        content: content,
        timestamp: null,
        type: 'CHAT'
    };
    
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
}

function sendTypingIndicator(channelId) {
    if (!stompClient || !isConnected) return;
    
    const typingMessage = {
        channelId: channelId,
        senderId: currentUser.id,
        senderName: currentUser.username,
        type: 'TYPING'
    };
    
    stompClient.send('/app/chat.typing', {}, JSON.stringify(typingMessage));
}

function disconnectWebSocket() {
    if (stompClient !== null && isConnected) {
        stompClient.disconnect();
    }
    isConnected = false;
}
