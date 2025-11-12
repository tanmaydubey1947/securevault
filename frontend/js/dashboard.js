document.addEventListener('DOMContentLoaded', async () => {
  // Retrieve token from session storage
  const token = sessionStorage.getItem('authToken');
  const email = sessionStorage.getItem('userMail');

  // Redirect to login if no token found
  if (!token) {
    window.location.href = 'login.html';
    return;
  }

  try {
    // Fetch user info from your backend API
    const response = await fetch(`http://localhost:8080/user/getUserDetails`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const user = await response.json();

    // Fill data on the page
    document.getElementById('userName').textContent = user.fullName || 'User';
    document.getElementById('userEmail').textContent = user.email || 'N/A';
    document.getElementById('userPhone').textContent = user.phoneNumber || 'N/A';
    document.getElementById('accountStatus').textContent = user.accountStatus || 'N/A';
    document.getElementById('availableAmount').textContent = user.availableAmount != null ? user.availableAmount : 'N/A';
    document.getElementById('pendingAmount').textContent = user.pendingAmount != null ? user.pendingAmount : 'N/A';
    document.getElementById('kycStatus').textContent = user.kycStatus || 'N/A';

  } catch (error) {
    console.error('Error fetching user info:', error);
    alert('Session expired or invalid token. Please log in again.');
    sessionStorage.removeItem('authToken');
    window.location.href = 'login.html';
  }

  // Logout button logic
  document.getElementById('logoutBtn').addEventListener('click', () => {
    sessionStorage.removeItem('authToken');
    window.location.href = 'login.html';
  });

  const sendForm = document.getElementById('send-wallet-box');
  sendForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const receiverMail = document.getElementById('receiverEmail').value.trim();
    const amount = parseFloat(document.getElementById('totalAmt').value);

    if(!receiverMail || isNaN(amount) || amount <= 0) {
      alert('Please enter valid receiver and amount.')
      return;
    }

    try {
      const sendResponse = await fetch('http://localhost:8080/transaction/sendToWallet', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },

        body: JSON.stringify({
          receiverMail,
          amount: amount
        })
      });

      if (!sendResponse.ok) {
        const errorData = await sendResponse.json();
        console.log(errorData);
        throw new Error(errorData.message || 'Transaction failed.');
      }

      const result = await sendResponse.json();
      alert(`Successfully Sent Money, trx Id: ${result.transactionId || 'Funds sent successfully!'}`);

      location.reload();

    } catch(err) {
      console.error('Error sending funds: ', err);
      alert(`❌ Failed to send funds: ${err.message}`);
    }
  })
});
