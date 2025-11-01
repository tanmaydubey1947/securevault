document.addEventListener('DOMContentLoaded', () => {
  const registerForm = document.getElementById('registerForm');

  if (registerForm) {
    registerForm.addEventListener('submit', async e => {
      e.preventDefault();
      const fullName = document.getElementById('fullName').value;
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      const phoneNumber = document.getElementById('phoneNumber').value;

      try {
        await apiRequest('/user/register', 'POST', { fullName, email, password, phoneNumber }, false);
        alert('Registration successful! Please check your mail for verification.');
        window.location.href = 'login.html';
      } catch {
        alert('Registration failed.');
      }
    });
  }

  // const token = sessionStorage.getItem('authToken');

  // const response = await fetch('http://localhost:8080/api/some-endpoint', {
  //   method: 'GET',
  //   headers: {
  //     'Authorization': `Bearer ${token}`,
  //     'Content-Type': 'application/json'
  //   }
  // });

});
