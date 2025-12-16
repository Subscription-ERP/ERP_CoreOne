let normalPayment = null; // 전역에 저장해서 재사용
let isModalRendered = false; // 모달에 위젯 렌더링 1회만

// ==============================
// ✅ 모달 DOM
// ==============================
const payModal = document.getElementById("oneTimePayModal");
const closePayModalBtn = document.getElementById("closePayModal");
const modalPayBtn = document.getElementById("modalPayBtn");

function openPayModal() {
  if (!payModal) return;
  payModal.classList.remove("is-hidden");
}

function closePayModal() {
  if (!payModal) return;
  payModal.classList.add("is-hidden");
}

// 닫기 버튼
if (closePayModalBtn) {
  closePayModalBtn.addEventListener("click", closePayModal);
}

// 배경 클릭 시 닫기(선택)
if (payModal) {
  payModal.addEventListener("click", (e) => {
    // backdrop 클릭이면 닫기 (내용 클릭은 유지)
    if (e.target.classList.contains("pay-modal-backdrop")) {
      closePayModal();
    }
  });
}

// ESC로 닫기(선택)
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape" && payModal && !payModal.classList.contains("is-hidden")) {
    closePayModal();
  }
});


// ==============================
// ✅ 일반결제(간편결제) 위젯 초기화 (모달에서만 렌더링)
// ==============================
async function initNormalPayment() {
  if (normalPayment && isModalRendered) {
    return normalPayment;
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

  // ✅ 모달 안 selector로 렌더링 (모달 HTML 안에 있어야 함)
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

  normalPayment = { widgets, data };
  isModalRendered = true;
  return normalPayment;
}


// ==============================
// ✅ 자동결제(정기결제) - 카드 등록(빌링키)
// ==============================
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


// ==============================
// ✅ UI 토글 로직 (페이지에서 위젯 영역은 더 이상 안 씀)
// ==============================
const payTypeInput = document.getElementById("payType");
const methodButtons = document.querySelectorAll(".method-btn");
const methodDesc = document.getElementById("methodDesc");

// 안내 박스만 제어 (ONE_TIME일 때도 안내 문구만 바꿔줌)
const billingGuideArea = document.getElementById("billingGuideArea");
const oneTimeGuideArea = document.getElementById("oneTimeGuideArea"); // 있으면 사용 (없어도 됨)

const descText = {
  SUBSCRIPTION: "정기결제를 선택하면 카드 등록(빌링키)이 진행되며, 다음 결제부터 자동 결제됩니다.",
  ONE_TIME: "간편결제(1회)는 이번 결제만 진행되며 자동결제는 등록되지 않습니다.",
};

function setPayType(payType) {
  if (payTypeInput) payTypeInput.value = payType;
  if (methodDesc) methodDesc.textContent = descText[payType] ?? "";

  // 안내 영역 토글(있을 때만)
  if (billingGuideArea && oneTimeGuideArea) {
    if (payType === "SUBSCRIPTION") {
      billingGuideArea.classList.remove("is-hidden");
      oneTimeGuideArea.classList.add("is-hidden");
    } else {
      billingGuideArea.classList.add("is-hidden");
      oneTimeGuideArea.classList.remove("is-hidden");
    }
  }
}

methodButtons.forEach((btn) => {
  btn.addEventListener("click", () => {
    methodButtons.forEach((b) => b.classList.remove("is-active"));
    btn.classList.add("is-active");

    const payType = btn.dataset.paytype;
    setPayType(payType);
  });
});

// 초기값: SUBSCRIPTION
setPayType("SUBSCRIPTION");


// ==============================
// ✅ 결제하기 버튼 클릭: 선택된 방식대로 처리
// ==============================
document.getElementById("payBtn").addEventListener("click", async () => {
  const payType = payTypeInput ? payTypeInput.value : "SUBSCRIPTION";

  // ✅ 정기결제: 바로 빌링키 등록
  if (payType === "SUBSCRIPTION") {
    await requestBillingAuth();
    return;
  }

  // ✅ 간편결제(1회): 모달 열고 위젯 렌더링만
  openPayModal();
  await initNormalPayment();
});


// ==============================
// ✅ 모달 안 "결제 진행" 버튼에서 실제 결제 요청
// ==============================
if (modalPayBtn) {
  modalPayBtn.addEventListener("click", async () => {
    const normal = await initNormalPayment();

    await normal.widgets.requestPayment({
      orderId: normal.data.orderId,
      orderName: normal.data.orderName,
      successUrl: normal.data.successUrl,
      failUrl: normal.data.failUrl,
    });
  });
}
