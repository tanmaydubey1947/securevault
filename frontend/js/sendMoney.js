document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('sendMoneyForm');
    
    form.addEventListener('submit', async e => {
      e.preventDefault();
      const recipient = document.getElementById('recipient').value.trim();
      const amount = parseFloat(document.getElementById('amount').value);
  
      try {
        const res = await apiRequest('/wallet/send', 'POST', { recipient, amount });
        alert(`✅ Sent $${amount} successfully!`);
        window.location.href = 'dashboard.html';
      } catch {
        alert('❌ Transaction failed.');
      }
    });
  });
  