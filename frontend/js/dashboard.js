document.addEventListener('DOMContentLoaded', async () => {
    const nameEl = document.getElementById('userName');
    const balanceEl = document.getElementById('balance');
    const txList = document.getElementById('txList');
    const logoutBtn = document.getElementById('logoutBtn');
  
    try {
      const user = await apiRequest('/user/profile');
      const wallet = await apiRequest('/wallet');
      const transactions = await apiRequest('/wallet/transactions');
  
      nameEl.textContent = `Hello, ${user.name}`;
      balanceEl.textContent = `$${wallet.balance.toFixed(2)}`;
  
      txList.innerHTML = transactions.map(tx => `
        <li>${tx.type} - $${tx.amount} (${tx.date})</li>
      `).join('');
  
    } catch (err) {
      console.error(err);
      window.location.href = 'login.html';
    }
  
    logoutBtn.addEventListener('click', () => {
      localStorage.removeItem('authToken');
      window.location.href = 'login.html';
    });
  });
  