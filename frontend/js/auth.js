document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
  
    if (loginForm) {
      loginForm.addEventListener('submit', async e => {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
  
        try {
          const res = await apiRequest('/auth/authenticate', 'POST', { email, password }, false);
          localStorage.setItem('authToken', res.token);
          window.location.href = 'dashboard.html';
        } catch {
          alert('Login failed.');
        }
      });
    }
  
    if (registerForm) {
      registerForm.addEventListener('submit', async e => {
        e.preventDefault();
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
  
        try {
          await apiRequest('/auth/register', 'POST', { name, email, password }, false);
          alert('Registration successful! Please login.');
          window.location.href = 'login.html';
        } catch {
          alert('Registration failed.');
        }
      });
    }
  });
  