// API Configuration
const API_BASE_URL = 'https://fsd-cloudchat.onrender.com/api';

// Store current user data
let currentUser = {
    id: null,
    username: null,
    email: null
};

// Generic fetch wrapper with error handling
async function apiRequest(endpoint, method, body = null, requiresAuth = true) {
    const headers = {
        'Content-Type': 'application/json'
    };
    
    if (requiresAuth && currentUser.id) {
        headers['userId'] = currentUser.id;
        headers['userName'] = currentUser.username;
    }
    
    const options = {
        method: method,
        headers: headers
    };
    
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Request failed');
        }
        
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Auth API Calls
async function register(username, email, password) {
    const data = await apiRequest('/auth/register', 'POST', {
        username: username,
        email: email,
        password: password
    }, false);
    
    currentUser.id = data.userId;
    currentUser.username = data.username;
    currentUser.email = data.email;
    
    // Save to localStorage for persistence
    localStorage.setItem('currentUser', JSON.stringify(currentUser));
    
    return data;
}

async function login(email, password) {
    const data = await apiRequest('/auth/login', 'POST', {
        email: email,
        password: password
    }, false);
    
    currentUser.id = data.userId;
    currentUser.username = data.username;
    currentUser.email = data.email;
    
    localStorage.setItem('currentUser', JSON.stringify(currentUser));
    
    return data;
}

function logout() {
    currentUser = { id: null, username: null, email: null };
    localStorage.removeItem('currentUser');
    localStorage.removeItem('selectedServerId');
    localStorage.removeItem('selectedChannelId');
    window.location.href = 'login.html';
}

function checkExistingLogin() {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        return true;
    }
    return false;
}

// Server API Calls
async function getServers() {
    return await apiRequest('/servers', 'GET');
}

async function getPublicServers() {
    return await apiRequest('/servers/public', 'GET');
}

async function createServer(serverData) {
    return await apiRequest('/servers', 'POST', serverData);
}

async function getChannels(serverId) {
    return await apiRequest(`/servers/${serverId}/channels`, 'GET');
}

async function createChannel(serverId, channelData) {
    return await apiRequest(`/servers/${serverId}/channels`, 'POST', channelData);
}

async function joinServer(serverId) {
    return await apiRequest(`/servers/${serverId}/join`, 'POST');
}

async function joinServerByInvite(code) {
    return await apiRequest(`/servers/invite/${code}/join`, 'POST');
}

async function leaveServer(serverId) {
    return await apiRequest(`/servers/${serverId}/leave`, 'POST');
}

// Message API Calls
async function getMessages(channelId) {
    return await apiRequest(`/channels/${channelId}/messages`, 'GET');
}
