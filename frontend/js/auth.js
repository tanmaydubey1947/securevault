document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
  
    if (loginForm) {
      loginForm.addEventListener('submit', async e => {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
  
        try {
          const res = await apiRequest('/auth/authenticate', 'POST', { email, password }, false);
          sessionStorage.setItem('authToken', res.token);
          sessionStorage.setItem('userMail', email);
          window.location.href = 'dashboard.html';
        } catch {
          alert('Login failed.');
        }
      });
    }
  });
  