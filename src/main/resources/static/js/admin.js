// AdminOrderController 테스트용: 주문 검색/단건 조회/상태 변경 API 연동.
const STATUS_OPTIONS = ["ORDERED", "SHIPPING", "DELIVERED", "CANCELED"];

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("search-btn").addEventListener("click", searchOrders);
    document.getElementById("lookup-btn").addEventListener("click", lookupOrderById);
    searchOrders(); // 초기 진입 시 전체 목록을 한 번 보여준다.
});

async function searchOrders() {
    clearError();

    const email = document.getElementById("filter-email").value.trim();
    const status = document.getElementById("filter-status").value;
    const menuName = document.getElementById("filter-menuName").value.trim();

    const params = new URLSearchParams();
    if (email) params.set("email", email);
    if (status) params.set("status", status);
    if (menuName) params.set("menuName", menuName);

    try {
        const response = await fetch(`/api/admin/orders?${params.toString()}`);
        const body = await response.json();

        if (!response.ok) throw new Error(body.message ?? "조회에 실패했습니다.");

        renderOrders(body);
    } catch (err) {
        showError(err.message);
    }
}

async function lookupOrderById() {
    clearError();

    const orderId = document.getElementById("lookup-orderId").value.trim();
    if (!orderId) {
        showError("조회할 주문번호를 입력해주세요.");
        return;
    }

    try {
        const response = await fetch(`/api/admin/orders/${orderId}`);
        const body = await response.json();

        if (!response.ok) throw new Error(body.message ?? "조회에 실패했습니다.");

        renderOrders([body]);
    } catch (err) {
        showError(err.message);
    }
}

function renderOrders(orders) {
    const tbody = document.getElementById("order-table-body");
    tbody.innerHTML = "";

    if (orders.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted">조회된 주문이 없습니다.</td></tr>`;
        return;
    }

    orders.forEach((order) => {
        const itemsText = order.items.map((item) => `${item.menuName} x${item.quantity}`).join(", ");

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${order.orderId}</td>
            <td>${escapeHtml(order.email)}</td>
            <td>${escapeHtml(order.address)} (${escapeHtml(order.zipCode)})</td>
            <td>${escapeHtml(itemsText)}</td>
            <td class="text-end">${order.totalPrice.toLocaleString()}원</td>
            <td><span class="badge bg-secondary">${order.status}</span></td>
            <td>
                <div class="d-flex gap-1">
                    <select class="form-select form-select-sm status-select">
                        ${STATUS_OPTIONS.map((s) => `<option value="${s}" ${s === order.status ? "selected" : ""}>${s}</option>`).join("")}
                    </select>
                    <button type="button" class="btn btn-sm btn-outline-dark text-nowrap change-status-btn">변경</button>
                </div>
            </td>
        `;

        tr.querySelector(".change-status-btn").addEventListener("click", () =>
            changeStatus(order.orderId, tr.querySelector(".status-select").value)
        );

        tbody.appendChild(tr);
    });
}

async function changeStatus(orderId, status) {
    clearError();

    try {
        const response = await fetch(`/api/admin/orders/${orderId}/status`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ status }),
        });
        const body = await response.json();

        if (!response.ok) throw new Error(body.message ?? "상태 변경에 실패했습니다.");

        alert(`주문 ${body.id}의 상태가 ${body.status}(으)로 변경됐습니다.`);
        await searchOrders();
    } catch (err) {
        showError(err.message);
    }
}

function showError(message) {
    const box = document.getElementById("error-message");
    box.textContent = message;
    box.style.display = "block";
}

function clearError() {
    const box = document.getElementById("error-message");
    box.textContent = "";
    box.style.display = "none";
}

function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
}
