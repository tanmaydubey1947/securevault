document.addEventListener('DOMContentLoaded', async () => {
    // Retrieve token from session storage
    const token = sessionStorage.getItem('authToken');
  
    // Redirect to login if no token found
    if (!token) {
      window.location.href = 'login.html';
      return;
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
  
      if(!receiverMail || isNaN(amount)) {
        alert('Please enter valid receiver and amount.')
        return;
      }
  
      try {
        const sendResponse = await fetch('http://localhost:8080/transaction/adjustment', {
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
  