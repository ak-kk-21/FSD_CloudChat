// Authentication page logic
document.addEventListener('DOMContentLoaded', () => {
    // Check if already logged in
    if (checkExistingLogin()) {
        window.location.href = 'index.html';
        return;
    }
    
    // Setup login form
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('login-email').value;
            const password = document.getElementById('login-password').value;
            
            try {
                await login(email, password);
                window.location.href = 'index.html';
            } catch (error) {
                document.getElementById('login-error').textContent = error.message;
            }
        });
    }
    
    // Setup register form
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('register-username').value;
            const email = document.getElementById('register-email').value;
            const password = document.getElementById('register-password').value;
            
            try {
                await register(username, email, password);
                window.location.href = 'index.html';
            } catch (error) {
                document.getElementById('register-error').textContent = error.message;
            }
        });
    }
});