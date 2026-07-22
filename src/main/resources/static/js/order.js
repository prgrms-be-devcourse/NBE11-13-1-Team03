// 메뉴 목록 조회, 장바구니 상태 관리, 주문 생성 API 연동을 담당한다.
const PLACEHOLDER_IMAGE = "https://i.imgur.com/HKOFQYa.jpeg";

// menuId -> { menuId, name, price, stock, quantity }
const cart = new Map();
let menus = [];

document.addEventListener("DOMContentLoaded", () => {
    loadMenus();
    document.getElementById("checkout-btn").addEventListener("click", submitOrder);
});

async function loadMenus() {
    try {
        const response = await fetch("/api/menus");
        if (!response.ok) throw new Error("메뉴를 불러오지 못했습니다.");

        menus = await response.json();
        renderMenus();
    } catch (err) {
        showError(err.message);
    }
}

function renderMenus() {
    const list = document.getElementById("menu-list");
    list.innerHTML = "";

    menus.forEach((menu) => {
        const soldOut = menu.stock <= 0;

        const item = document.createElement("li");
        item.className = "list-group-item d-flex mt-2 align-items-center";
        item.innerHTML = `
            <div class="col-2"><img class="img-fluid" src="${PLACEHOLDER_IMAGE}" alt=""></div>
            <div class="col">
                <div class="row text-muted">${escapeHtml(menu.description ?? "")}</div>
                <div class="row">${escapeHtml(menu.name)}</div>
            </div>
            <div class="col text-center price">${menu.price.toLocaleString()}원</div>
            <div class="col text-center stock">${soldOut ? "품절" : `재고 ${menu.stock}개`}</div>
            <div class="col text-end action">
                <button type="button" class="btn btn-small btn-outline-dark add-btn" ${soldOut ? "disabled" : ""}>
                    추가
                </button>
            </div>
        `;

        item.querySelector(".add-btn").addEventListener("click", () => addToCart(menu));
        list.appendChild(item);
    });
}

function addToCart(menu) {
    const existing = cart.get(menu.menuId);
    const currentQuantity = existing ? existing.quantity : 0;

    if (currentQuantity + 1 > menu.stock) {
        showError(`재고가 부족합니다. (남은 재고: ${menu.stock}개)`);
        return;
    }

    cart.set(menu.menuId, {
        menuId: menu.menuId,
        name: menu.name,
        price: menu.price,
        quantity: currentQuantity + 1,
    });

    clearError();
    renderCart();
}

function removeFromCart(menuId) {
    cart.delete(menuId);
    renderCart();
}

function renderCart() {
    const cartList = document.getElementById("cart-list");
    cartList.innerHTML = "";

    let total = 0;
    cart.forEach((item) => {
        total += item.price * item.quantity;

        const row = document.createElement("div");
        row.className = "row";
        row.innerHTML = `
            <h6 class="p-0 d-flex justify-content-between align-items-center">
                <span>${escapeHtml(item.name)} <span class="badge bg-dark">${item.quantity}개</span></span>
                <button type="button" class="btn btn-sm btn-link text-danger p-0 remove-btn">삭제</button>
            </h6>
        `;
        row.querySelector(".remove-btn").addEventListener("click", () => removeFromCart(item.menuId));
        cartList.appendChild(row);
    });

    document.getElementById("total-amount").textContent = `${total.toLocaleString()}원`;
}

async function submitOrder() {
    clearError();

    const email = document.getElementById("email").value.trim();
    const address = document.getElementById("address").value.trim();
    const zipCode = document.getElementById("postcode").value.trim();

    if (!email || !address || !zipCode) {
        showError("이메일, 주소, 우편번호를 모두 입력해주세요.");
        return;
    }

    if (cart.size === 0) {
        showError("장바구니가 비어 있습니다.");
        return;
    }

    const request = {
        email,
        address,
        zipCode,
        items: Array.from(cart.values()).map((item) => ({
            menuId: item.menuId,
            quantity: item.quantity,
        })),
    };

    try {
        const response = await fetch("/api/orders", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(request),
        });

        const body = await response.json();

        if (!response.ok) {
            throw new Error(body.message ?? "주문에 실패했습니다.");
        }

        alert(`주문이 완료됐습니다.\n주문번호: ${body.orderId}\n총 금액: ${body.totalAmount.toLocaleString()}원`);

        cart.clear();
        renderCart();
        document.getElementById("email").value = "";
        document.getElementById("address").value = "";
        document.getElementById("postcode").value = "";

        await loadMenus(); // 차감된 재고를 반영해 다시 불러온다.
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
