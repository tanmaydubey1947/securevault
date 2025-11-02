document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
  
    if (!token) {
      alert('Invalid or missing token in URL.');
      window.location.href = 'login.html';
      return;
    }
  
    const form = document.getElementById('resetForm');
    form.addEventListener('submit', async e => {
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
        const response = await fetch(`${BASE_URL}/auth/reset-password`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ token, newPassword })
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
  });
  