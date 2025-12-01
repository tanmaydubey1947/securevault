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


        const userDetails = await fetch(`http://localhost:8080/user/getUserDetails`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${res.token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!userDetails.ok) {
          throw new Error("Failed to fetch user details.")
        }

        const user = await userDetails.json();
        console.log("Control reached here")

        if (user.role === 'ROLE_ADMIN') {
          window.location.href = 'dashboard-admin.html';
        } else {
          window.location.href = 'dashboard-user.html';
        }
      } catch {
        alert('Login failed.');
      }
    });
  }
});
