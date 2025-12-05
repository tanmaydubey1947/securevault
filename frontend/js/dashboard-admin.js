document.addEventListener("DOMContentLoaded", () => {

    const token = sessionStorage.getItem("authToken");
    if (!token) window.location.href = "login.html";

    document.getElementById("logoutBtn").onclick = () => {
        sessionStorage.removeItem("authToken");
        window.location.href = "login.html";
    };

    // -------------------------
    // 1) WALLET ADJUSTMENT
    // -------------------------
    document.getElementById("send-wallet-box").addEventListener("submit", async (e) => {
        e.preventDefault();

        const receiverMail = document.getElementById("receiverEmail").value;
        const amount = parseFloat(document.getElementById("totalAmt").value);

        const res = await fetch("http://localhost:8080/transaction/adjustment", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type":"application/json"
            },
            body: JSON.stringify({ receiverMail, amount })
        });

        const data = await res.json();
        if (!res.ok) return alert(data.message);

        alert("Success: " + data.transactionId);
        location.reload();
    });

    // -------------------------
    // 2) FETCH USERS
    // -------------------------
    async function loadUsers() {
        const res = await fetch("http://localhost:8080/user/getAllUsers", {
            headers: { "Authorization": `Bearer ${token}` }
        });
        const users = await res.json();
        displayUsers(users);
    }

    function displayUsers(users){
        const box = document.getElementById("usersList");
        box.innerHTML = "";

        users.forEach(user => {
            const card = document.createElement("div");
            card.className = "user-card";

            card.innerHTML = `
                <h3>${user.fullName} <span style="color:#777;">(ID: ${user.id})</span></h3>
                <p><b>Email:</b> ${user.email}</p>
                <p><b>Phone:</b> ${user.phoneNumber}</p>
                <p><b>Role:</b> ${user.role}</p>
                <p><b>Status:</b> ${user.accountStatus}</p>
                <p><b>KYC:</b> ${user.kycStatus}</p>
                <p><b>Created:</b> ${user.createdAt.split("T")[0]}</p>

                <div class="wallet-summary">
                    <div class="wallet-box">
                        <h4>Available</h4>
                        <p>$${user.availableAmount || 0}</p>
                    </div>
                    <div class="wallet-box pending">
                        <h4>Pending</h4>
                        <p>$${user.pendingAmount || 0}</p>
                    </div>
                </div>
            `;

            box.appendChild(card);
        });

        document.querySelectorAll(".role-btn").forEach(btn => {
            btn.onclick = () => {
                document.getElementById("userIdForRole").value = btn.dataset.id;
                window.scrollTo({ top: document.body.scrollHeight, behavior: "smooth" });
            };
        });
    }


    // -----------------------------
    // 3) LOAD TRANSACTIONS
    // -----------------------------
    async function loadTransactions(){
        const res = await fetch("http://localhost:8080/transaction/getAllTransactions", {
            headers:{ "Authorization":`Bearer ${token}` }
        });

        const trx = await res.json();
        displayTransactions(trx);
    }

    function displayTransactions(trx){
        const wrap = document.getElementById("transactionTableWrapper");

        // Desktop Table
        let html = `
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th><th>Type</th><th>Amount</th><th>Sender</th>
                        <th>Receiver</th><th>Status</th><th>Date</th>
                    </tr>
                </thead>
                <tbody>
        `;

        trx.forEach(t => {
            html += `
                <tr>
                    <td>${t.transactionId}</td>
                    <td>${t.transactionType}</td>
                    <td>$${t.amount}</td>
                    <td>${t.sender}</td>
                    <td>${t.receiver}</td>
                    <td>${t.transactionStatus}</td>
                    <td>${t.createdAt.split("T")[0]}</td>
                </tr>
            `;
        });

        html += `</tbody></table>`;

        // Mobile Cards
        trx.forEach(t => {
            html += `
                <div class="transaction-card">
                    <p><b>ID:</b> ${t.transactionId}</p>
                    <p><b>Type:</b> ${t.transactionType}</p>
                    <p><b>Amount:</b> $${t.amount}</p>
                    <p><b>Sender:</b> ${t.sender}</p>
                    <p><b>Receiver:</b> ${t.receiver}</p>
                    <p><b>Status:</b> ${t.transactionStatus}</p>
                    <p><b>Date:</b> ${t.createdAt.split("T")[0]}</p>
                </div>
            `;
        });

        wrap.innerHTML = html;
    }

    // INITIAL LOAD
    loadUsers();
    loadTransactions();
});
