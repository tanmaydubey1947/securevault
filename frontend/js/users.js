document.addEventListener('DOMContentLoaded', () => {

  // ✅ REGISTER FORM HANDLER
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', async e => {
      e.preventDefault();

      const fullName = document.getElementById('fullName').value.trim();
      const email = document.getElementById('email').value.trim();
      const password = document.getElementById('password').value.trim();
      const phoneNumber = document.getElementById('phoneNumber').value.trim();

      try {
        await apiRequest('/user/register', 'POST', { fullName, email, password, phoneNumber }, false);
        alert('Registration successful! Please check your email for verification.');
        window.location.href = 'login.html';
      } catch (err) {
        console.error(err);
        alert('Registration failed.');
      }
    });
  }

  // ✅ FORGOT PASSWORD FORM HANDLER
  const forgotForm = document.getElementById('forgotForm');
  if (forgotForm) {
    forgotForm.addEventListener('submit', async e => {
      e.preventDefault();

      const email = document.getElementById('email').value.trim();
      if (!email) {
        alert('Please enter your email.');
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/user/generateResetToken`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email })
        });

        if (!response.ok) throw new Error('Failed to send reset link.');

        alert('A password reset link has been sent to your email.');
        forgotForm.reset();
      } catch (err) {
        console.error('Error sending reset link:', err);
        alert('Error sending reset link. Please try again.');
      }
    });
  }

  // ✅ RESET PASSWORD FORM HANDLER
  const resetForm = document.getElementById('resetForm');
  if (resetForm) {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');

    if (!token) {
      alert('Invalid or missing token in URL.');
      window.location.href = 'login.html';
      return;
    }

    resetForm.addEventListener('submit', async e => {
      e.preventDefault();

      const newPassword = document.getElementById('newPassword').value.trim();
      const confirmPassword = document.getElementById('confirmPassword').value.trim();

      if (!newPassword || !confirmPassword) {
        alert('Please enter both password fields.');
        return;
      }

      if (newPassword !== confirmPassword) {
        alert('Passwords do not match.');
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/user/resetCredentials`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ token, newPassword, confirmPassword })
        });

        if (!response.ok) {
          const err = await response.text();
          throw new Error(err || 'Failed to reset password');
        }

        alert('Password reset successful! You can now log in.');
        window.location.href = 'login.html';
      } catch (error) {
        console.error('Error resetting password:', error);
        alert('Password reset failed. The link may be invalid or expired.');
      }
    });
  }

});
