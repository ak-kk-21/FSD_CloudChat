// Main application entry point

// Initialize app when page loads
document.addEventListener('DOMContentLoaded', async () => {
    // Check if user is logged in
    if (!checkExistingLogin()) {
        window.location.href = 'login.html';
        return;
    }
    
    // Update UI with user info
    document.getElementById('current-username').textContent = currentUser.username;
    
    // Setup event listeners
    setupEventListeners();
    
    // Load servers
    await loadServers();
    
    // Connect WebSocket
    connectWebSocket();
});

function setupEventListeners() {
    // Send message button
    const sendBtn = document.getElementById('send-btn');
    if (sendBtn) {
        sendBtn.onclick = sendMessage;
    }
    
    // Message input (Enter to send)
    const messageInput = document.getElementById('message-input');
    if (messageInput) {
        messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
        messageInput.addEventListener('input', onMessageInput);
    }
    
    // Add server button
    const addServerBtn = document.getElementById('add-server-btn');
    if (addServerBtn) {
        addServerBtn.onclick = showServerModal;
    }
    
    // Logout button
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.onclick = () => {
            disconnectWebSocket();
            logout();
        };
    }
    
    // Close modals when clicking outside
    window.onclick = (event) => {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    };
}