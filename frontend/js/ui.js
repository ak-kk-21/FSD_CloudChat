// UI State
let servers = [];
let currentServerId = null;
let currentChannelId = null;
let channels = [];

// Render servers in sidebar
function renderServers() {
    const serversList = document.getElementById('servers-list');
    if (!serversList) return;
    
    serversList.innerHTML = '';
    
    servers.forEach(server => {
        const serverElement = document.createElement('div');
        serverElement.className = 'server-item';
        if (currentServerId === server.id) {
            serverElement.classList.add('selected');
        }
        
        serverElement.innerHTML = `<div class="server-icon">${server.name.charAt(0).toUpperCase()}</div>`;
        serverElement.title = server.name;
        serverElement.onclick = () => selectServer(server.id);
        serversList.appendChild(serverElement);
    });
}

// Select a server and load its channels
async function selectServer(serverId) {
    currentServerId = serverId;
    localStorage.setItem('selectedServerId', serverId);
    
    // Update UI
    renderServers();
    
    // Load channels
    await loadChannels(serverId);
}

// Load channels for a server
async function loadChannels(serverId) {
    try {
        channels = await getChannels(serverId);
        renderChannels();
        
        // Update header
        const server = servers.find(s => s.id === serverId);
        const nameText = document.getElementById('current-server-name-text');
        if (nameText) nameText.textContent = server ? server.name : 'Server';
        
        // Show buttons
        const addChannelBtn = document.getElementById('add-channel-btn');
        const leaveServerBtn = document.getElementById('leave-server-btn');
        if (addChannelBtn) addChannelBtn.style.display = 'block';
        if (leaveServerBtn) leaveServerBtn.style.display = 'block';
        
        // Show invite code if available
        const inviteDisplay = document.getElementById('server-invite-code-display');
        if (inviteDisplay && server && server.inviteCode) {
            inviteDisplay.innerHTML = `
                <div class="invite-pill" onclick="copyTextToClipboard('${server.inviteCode}')" title="Click to copy invite code">
                    <span>Invite:</span>
                    <strong>${server.inviteCode}</strong>
                    <span class="copy-icon">📋</span>
                </div>
            `;
            inviteDisplay.style.display = 'block';
        } else if (inviteDisplay) {
            inviteDisplay.style.display = 'none';
        }
        
        // Select first channel if available
        if (channels.length > 0) {
            selectChannel(channels[0].id);
        }
    } catch (error) {
        console.error('Failed to load channels:', error);
    }
}

// Render channels in sidebar
function renderChannels() {
    const channelsList = document.getElementById('channels-list');
    if (!channelsList) return;
    
    channelsList.innerHTML = '';
    
    channels.forEach(channel => {
        const channelElement = document.createElement('div');
        channelElement.className = 'channel-item';
        if (currentChannelId === channel.id) {
            channelElement.classList.add('selected');
        }
        channelElement.innerHTML = channel.name;
        channelElement.onclick = () => selectChannel(channel.id);
        channelsList.appendChild(channelElement);
    });
}

// Select a channel and load messages
async function selectChannel(channelId) {
    currentChannelId = channelId;
    localStorage.setItem('selectedChannelId', channelId);
    
    renderChannels();
    
    // Update chat header
    const channel = channels.find(c => c.id === channelId);
    document.getElementById('current-channel-name').innerHTML = channel ? `# ${channel.name}` : 'Select a channel';
    
    // Load messages
    await loadMessages(channelId);
    
    // Enable send button
    document.getElementById('send-btn').disabled = false;
}

// Load messages for a channel
async function loadMessages(channelId) {
    try {
        const data = await getMessages(channelId);
        renderMessages(data.messages || []);
    } catch (error) {
        console.error('Failed to load messages:', error);
        renderMessages([]);
    }
}

// Render messages in chat area
function renderMessages(messages) {
    const container = document.getElementById('messages-container');
    if (!container) return;
    
    container.innerHTML = '';
    
    messages.forEach(message => {
        const messageElement = createMessageElement(message);
        container.appendChild(messageElement);
    });
    
    container.scrollTop = container.scrollHeight;
}

// Create a single message element
function createMessageElement(message) {
    const messageDiv = document.createElement('div');
    const isOwn = message.senderId === currentUser.id;
    
    messageDiv.className = `message ${isOwn ? 'message-own' : 'message-other'}`;
    
    const timestamp = new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    const avatarChar = message.senderName.charAt(0).toUpperCase();
    
    messageDiv.innerHTML = `
        <div class="message-header">
            <span class="sender-name">${message.senderName}</span>
        </div>
        <div class="message-content">
            <p>${escapeHtml(message.content)}</p>
        </div>
        <div class="timestamp">${timestamp}</div>
    `;
    
    return messageDiv;
}

// Handle incoming WebSocket messages
function handleReceivedMessage(message) {
    // Only show messages for current channel
    if (message.channelId !== currentChannelId && message.type !== 'JOIN') {
        return;
    }
    
    const container = document.getElementById('messages-container');
    
    if (message.type === 'JOIN') {
        const systemDiv = document.createElement('div');
        systemDiv.className = 'system-message';
        systemDiv.textContent = message.content;
        container.appendChild(systemDiv);
    } else if (message.type === 'TYPING') {
        const typingDiv = document.getElementById('typing-indicator');
        typingDiv.textContent = `${message.senderName} is typing...`;
        setTimeout(() => {
            if (typingDiv.textContent === `${message.senderName} is typing...`) {
                typingDiv.textContent = '';
            }
        }, 1000);
    } else if (message.type === 'CHAT') {
        const messageElement = createMessageElement(message);
        container.appendChild(messageElement);
        container.scrollTop = container.scrollHeight;
    }
}

// Send a message
function sendMessage() {
    const input = document.getElementById('message-input');
    const content = input.value.trim();
    
    if (!content || !currentChannelId) return;
    
    sendChatMessage(currentChannelId, content);
    input.value = '';
}

// Create server modal functions
function showServerModal() {
    document.getElementById('server-name').value = '';
    document.getElementById('server-is-private').checked = false;
    document.getElementById('server-create-success').style.display = 'none';
    document.getElementById('create-server-btn').style.display = 'block';
    document.getElementById('server-modal').style.display = 'flex';
}

async function handleCreateServer() {
    const name = document.getElementById('server-name').value.trim();
    const isPrivate = document.getElementById('server-is-private').checked;
    
    if (!name) {
        alert('Server name is required');
        return;
    }
    
    try {
        const result = await createServer({ 
            name: name,
            isPublic: !isPrivate
        });
        await loadServers();
        
        // Display success UI
        document.getElementById('create-server-btn').style.display = 'none';
        const successDiv = document.getElementById('server-create-success');
        document.getElementById('new-server-invite-code').textContent = result.inviteCode;
        successDiv.style.display = 'block';
        
        // Select the newly created server
        await selectServer(result.id);
        
    } catch (error) {
        alert('Failed to create server: ' + error.message);
    }
}

async function handleLeaveServer() {
    if (!currentServerId) return;
    
    const server = servers.find(s => s.id === currentServerId);
    if (!confirm(`Are you sure you want to leave ${server ? server.name : 'this server'}?`)) {
        return;
    }
    
    try {
        await leaveServer(currentServerId);
        currentServerId = null;
        localStorage.removeItem('selectedServerId');
        
        // Hide channel items
        document.getElementById('channels-list').innerHTML = '';
        document.getElementById('current-server-name-text').textContent = 'Select a server';
        document.getElementById('add-channel-btn').style.display = 'none';
        document.getElementById('leave-server-btn').style.display = 'none';
        document.getElementById('server-invite-code-display').style.display = 'none';
        
        await loadServers();
    } catch (error) {
        alert('Failed to leave server: ' + error.message);
    }
}

function copyInviteToClipboard() {
    const code = document.getElementById('new-server-invite-code').textContent;
    copyTextToClipboard(code);
}

function copyTextToClipboard(text) {
    // Robust clipboard fallback for incognito/local-file mode
    if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(text).then(() => {
            alert('Invite code copied to clipboard!');
        }).catch(err => {
            fallbackCopy(text);
        });
    } else {
        fallbackCopy(text);
    }
}

function fallbackCopy(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    
    // Ensure the textarea is off-screen
    textArea.style.position = "fixed";
    textArea.style.left = "-9999px";
    textArea.style.top = "0";
    document.body.appendChild(textArea);
    
    textArea.focus();
    textArea.select();
    
    try {
        const successful = document.execCommand('copy');
        if (successful) {
            alert('Invite code copied to clipboard!');
        } else {
            alert('Failed to copy. Please copy manually.');
        }
    } catch (err) {
        alert('Failed to copy. Please copy manually.');
    }
    
    document.body.removeChild(textArea);
}



function showJoinInviteModal() {
    document.getElementById('invite-code-input').value = '';
    document.getElementById('join-invite-modal').style.display = 'flex';
}

async function handleJoinViaInvite() {
    const code = document.getElementById('invite-code-input').value.trim();
    if (!code) {
        alert('Please enter an invite code');
        return;
    }
    try {
        await joinServerByInvite(code);
        await loadServers();
        closeModal('join-invite-modal');
    } catch (error) {
        alert('Failed to join server: ' + error.message);
    }
}

// Server Discovery functions
async function handleDiscoverServers() {
    try {
        const data = await getPublicServers();
        const publicServers = data.servers || [];
        
        const listContainer = document.getElementById('public-servers-list');
        listContainer.innerHTML = '';
        
        if (publicServers.length === 0) {
            listContainer.innerHTML = '<p>No public servers available to join.</p>';
        } else {
            // Filter out servers user is already a member of
            const availableServers = publicServers.filter(s => 
                !s.members || !s.members.includes(currentUser.id)
            );
            
            if (availableServers.length === 0) {
                listContainer.innerHTML = '<p>You are already a member of all public servers.</p>';
            } else {
                availableServers.forEach(server => {
                    const serverDiv = document.createElement('div');
                    serverDiv.className = 'public-server-item';
                    serverDiv.innerHTML = `
                        <div class="public-server-info">
                            <h4>${escapeHtml(server.name)}</h4>
                        </div>
                        <button onclick="handleJoinServer('${server.id}')">Join</button>
                    `;
                    listContainer.appendChild(serverDiv);
                });
            }
        }
        
        document.getElementById('discover-modal').style.display = 'flex';
    } catch (error) {
        alert('Failed to load public servers: ' + error.message);
    }
}

async function handleJoinServer(serverId) {
    try {
        await joinServer(serverId);
        await loadServers(); // reload user's servers
        closeModal('discover-modal');
    } catch (error) {
        alert('Failed to join server: ' + error.message);
    }
}

// Create channel modal functions
function showChannelModal() {
    if (!currentServerId) {
        alert('Select a server first');
        return;
    }
    document.getElementById('channel-modal').style.display = 'flex';
}

async function handleCreateChannel() {
    const name = document.getElementById('channel-name').value.trim();
    const type = document.getElementById('channel-type').value;
    
    if (!name) {
        alert('Channel name is required');
        return;
    }
    
    try {
        await createChannel(currentServerId, { name, type });
        await loadChannels(currentServerId);
        closeModal('channel-modal');
        document.getElementById('channel-name').value = '';
    } catch (error) {
        alert('Failed to create channel: ' + error.message);
    }
}

// Load all servers for current user
async function loadServers() {
    try {
        const data = await getServers();
        servers = data.servers || [];
        renderServers();
        
        // Restore last selected server
        const savedServerId = localStorage.getItem('selectedServerId');
        if (savedServerId && servers.find(s => s.id === savedServerId)) {
            await selectServer(savedServerId);
        } else if (servers.length > 0) {
            await selectServer(servers[0].id);
        }
    } catch (error) {
        console.error('Failed to load servers:', error);
    }
}

// Helper functions
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Typing indicator handler
let typingTimeout;
function onMessageInput() {
    if (currentChannelId) {
        sendTypingIndicator(currentChannelId);
        clearTimeout(typingTimeout);
        typingTimeout = setTimeout(() => {}, 1000);
    }
}