let normalPayment = null; // 전역에 저장해서 재사용

// 🔹 일반결제 위젯 초기화 (한 번만)
async function initNormalPayment() {
    if (normalPayment) {
        return normalPayment; // 이미 초기화되어 있으면 재사용
    }

    // 서버에서 주문 정보 가져오기
    const response = await fetch("/api/payments/request", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
    });
    const data = await response.json();

    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
    const tossPayments = TossPayments(clientKey);

    const customerKey = "qcE3458DWgfhY2eSgXQot";
    const widgets = tossPayments.widgets({ customerKey });

    await widgets.setAmount({
        currency: "KRW",
        value: data.amount,
    });

    await Promise.all([
        widgets.renderPaymentMethods({
            selector: "#payment-method",
            variantKey: "DEFAULT",
        }),
        widgets.renderAgreement({
            selector: "#agreement",
            variantKey: "AGREEMENT",
        }),
    ]);

    // 👉 한 번 만든 걸 전역에 저장
    normalPayment = { widgets, data };
    return normalPayment;
}

// 페이지 로드시 한 번만 초기화 (선택)
/*initNormalPayment();*/


// 🔹 자동결제(카드 등록) 쪽
const clientKeyBilling = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq";
const customerKeyBilling = "iJ-B6Z9YnXC9tFpC8R56k";
const tossPaymentsBilling = TossPayments(clientKeyBilling);
const payment = tossPaymentsBilling.payment({ customerKey: customerKeyBilling });

async function requestBillingAuth() {
    await payment.requestBillingAuth({
        method: "CARD",
        successUrl: window.location.origin + "/api/billing/success",
        failUrl: window.location.origin + "/api/fail",
        customerEmail: "customer123@gmail.com",
        customerName: "김토스",
    });
}


// 🔹 결제 버튼 클릭 시 분기
document.getElementById("payBtn").addEventListener("click", async () => {
    const auto = document.getElementById("autoBilling").checked;

    if (auto) {
        // ✅ 자동결제(카드 등록)
        await requestBillingAuth();
    } else {
        // ✅ 일반결제 (위젯 재사용)
        const normal = await initNormalPayment();  // 여기서는 재사용만
        await normal.widgets.requestPayment({
            orderId: normal.data.orderId,
            orderName: normal.data.orderName,
            successUrl: normal.data.successUrl,
            failUrl: normal.data.failUrl,
        });
    }
});
