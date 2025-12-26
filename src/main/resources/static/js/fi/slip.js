

const custCodeSearch  = document.getElementById("custCode");
const custNameSearch  = document.getElementById("custName");

const btnOpenCustModal = document.getElementById("btnOpenCustModal"); // 거래처검색 버튼
const btnSave          = document.getElementById("btnSave");      // 수금 저장 버튼 (harp.html에서 id 맞춰 주세요)

let custSearchByEnter = false;

// ==============================
// Toast Grid 정의
// ==============================

const gridColumns = [
  { header: '일자', name: 'slipDate', width: 110 },
  { header: '회계계정', name: 'accountCode', hidden:true },
  { header: '회계계정', name: 'accountName', width: 160 },
  { header: '금액', name: 'amount', editor:'text', align:'right', width:120 },
  { header: '거래처', name: 'custName', width:150 },
  { header: '적요', name: 'remark', editor:'text', minWidth:150 }
];

// 차변 Grid
const debitGrid = new tui.Grid({
  el: document.getElementById('debitGrid'),
  scrollX: false,
  scrollY: true,
  bodyHeight: 'fitToParent',
  rowHeaders: ['checkbox'],
  columns: gridColumns
});

// 대변 Grid
const creditGrid = new tui.Grid({
  el: document.getElementById('creditGrid'),
  scrollX: false,
  scrollY: true,
  bodyHeight: 'fitToParent',
  rowHeaders: ['checkbox'],
  columns: gridColumns
});


// 모달 열기
function openCustModalOnly(e) {
	
    e.preventDefault();
	openCustModal();
    getCustList();    // 전체 목록 or 필요한 대로

}


// 거래처 모달 검색 결과 전달 받음
function searchCustModal() {
    const custCodeKeyword = custCodeSearch.value.trim();
    // 1) 모달 쪽 검색 키워드 입력
    const schCustCode = document.getElementById('schCustCode');

    if (schCustCode) schCustCode.value = custCodeKeyword;

    // 2) 모달 JS의 검색 함수 호출
    if (typeof searchCust === 'function') {
        custSearchByEnter = true;
        searchCust(true);
    }
}

function closeCustModal() {
	custModal.hidden = true;
	custModal.classList.add('hidden');
}

// Enter 입력 시 검색
function handleEnter(e) {
    if(e.key === 'Enter') {
        e.preventDefault();
        searchCustModal();
    }
}


function deleteCheckedRows(grid) {
	  const checked = grid.getCheckedRowKeys();

	  if (checked.length === 0) {
	    alert("삭제할 행을 선택하세요.");
	    return;
	  }

	  grid.removeRows(checked);
	}

// ==============================
// 계정 추가 버튼 기능
// ==============================

function addRowToGrid(grid, accountSelectId) {

	const selectEl = document.getElementById(accountSelectId);
	const accountCode = selectEl.value;

	if (!accountCode) {
	  alert("회계계정을 선택해주세요.");
	  return;
	}

	// 선택된 옵션의 텍스트 = 계정명
	const accountName = selectEl.options[selectEl.selectedIndex].textContent;


  grid.appendRow({
    slipDate: document.getElementById("slipDate").value,
    accountCode: accountCode,
	accountName: accountName,
    custName: document.getElementById("custName").value,
    remark: document.getElementById("remarkTop").value,
    amount: ""
  });
}

(function initSlipDate() {
  const slipDateEl = document.getElementById("slipDate");
  if (slipDateEl && !slipDateEl.value) {
    const today = new Date().toISOString().split("T")[0]; // yyyy-MM-dd
    slipDateEl.value = today;
  }
})();

function saveSlip() {

  const companyCode = document.getElementById("loginCompanyCode").value;
  const loginUserId = document.getElementById("loginUserId").value;

  const slipDate   = document.getElementById("slipDate").value;
  const custCode   = document.getElementById("custCode").value.trim();
  const remarkTop  = document.getElementById("remarkTop").value;

  // ===== 1. 그리드 데이터 수집 =====
  const debitRows  = debitGrid.getData();
  const creditRows = creditGrid.getData();

  const detailList = [];

  // 숫자 파싱 유틸 (1,000 형식도 처리)
  const parseAmount = (v) => {
    if (v === null || v === undefined) return 0;
    const s = v.toString().replace(/,/g, '').trim();
    if (!s) return 0;
    const n = Number(s);
    return isNaN(n) ? 0 : n;
  };

  // 차변/대변 공통 push 함수
  const pushRows = (rows, drCrType) => {
    rows.forEach(row => {
      const amount = parseAmount(row.amount);
      if (amount <= 0) {
        return; // 0 이하는 저장 안 함
      }

      detailList.push({
        drCrType: drCrType,               // "D" 또는 "C"
        slipAccount: row.accountCode,     // 현재는 계정코드/명 공통으로 사용
        amount: amount,
        // 향후 detail remark 컬럼 추가 시를 대비해서 같이 보내둠 (VO에 없으면 무시됨)
        remark: row.remark
      });
    });
  };

  pushRows(debitRows,  "D");
  pushRows(creditRows, "C");

  if (detailList.length === 0) {
    alert("차변/대변에 금액이 있는 행을 한 건 이상 입력해주세요.");
    return;
  }

  // ===== 2. 차대변 합계 검증 (프론트에서 1차 체크) =====
  const drTotal = detailList
    .filter(d => d.drCrType === "D")
    .reduce((sum, d) => sum + d.amount, 0);

  const crTotal = detailList
    .filter(d => d.drCrType === "C")
    .reduce((sum, d) => sum + d.amount, 0);

  if (drTotal !== crTotal) {
    alert("차변 합계(" + drTotal.toLocaleString() + ")와\n"
        + "대변 합계(" + crTotal.toLocaleString() + ")가 일치하지 않습니다.");
    return;
  }

  // ===== 3. 마스터 + 디테일 payload 구성 =====
  const payload = {
    companyCode: companyCode,
    slipDate: slipDate,          // LocalDate로 매핑됨 (yyyy-MM-dd)
    fiscalPeriod: slipDate,      // 별도 회계기간 없으면 전표일자와 동일하게 전송
    custCode: custCode,
    summary: remarkTop,          // 상단 적요 → SLIP_MASTER.SUMMARY
    status: "0",                 // 기본값(미결 등)으로 사용
    createdBy: loginUserId,
    updatedBy: loginUserId,
    detailList: detailList
  };

  // ===== 4. 저장 호출 =====
  fetch("/api/fi/slip", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert("전표가 저장되었습니다.\n전표번호: " + data.slipNo);

        // 필요 시 화면 초기화
         debitGrid.clear();
         creditGrid.clear();
         document.getElementById("remarkTop").value = "";
      } else {
        alert("저장에 실패했습니다.\n" + (data.message || ""));
      }
    })
    .catch(err => {
      console.error(err);
      alert("저장 중 오류가 발생했습니다.");
    });
}

function setFiAccount(){
	fetch("/api/com/typeattribute?groupCode=FI_ACCOUNT&attribute=C")
	.then(res => res.json())
	.then(result => {
		result.forEach(item => {
			const creditselect = document.getElementById('creditAccount');
			const creditoption = document.createElement("option");
			creditoption.value = item.code;
			creditoption.textContent = item.codeName; 
			creditselect.appendChild(creditoption);
		});
	})
	fetch("/api/com/typeattribute?groupCode=FI_ACCOUNT&attribute=D")
	.then(res => res.json())
	.then(result => {
		result.forEach(item => {
			const debitselect = document.getElementById('debitAccount');
			const debitoption = document.createElement("option");
			debitoption.value = item.code;
			debitoption.textContent = item.codeName; 
			debitselect.appendChild(debitoption);
		});
	})
}
setFiAccount();

document.addEventListener("DOMContentLoaded", function () {
	custCodeSearch.addEventListener('keydown', handleEnter);
	btnOpenCustModal.addEventListener('click', openCustModalOnly);
	btnCustClose.addEventListener('click', closeCustModal);
	window.handleSelectedCust = function(row) {
	    custCodeSearch.value = row.custCode;
		custNameSearch.value = row.custName;

	};

    window.afterCustSearch = function(result) {
        const byEnter = custSearchByEnter === true;
        custSearchByEnter = false;

        if (byEnter) {
            if (result.length === 1) {
                returnOnlyOne(result);
            } else if (result.length > 1) {
                openCustModal();
                searchCust(false);
            } else {
                showToast('검색 결과가 없습니다.', 'warning');
            }
        } else {
            openCustModal();
        }
    };
    
	document.getElementById("btnDebitAdd").addEventListener("click", () => {
	  addRowToGrid(debitGrid, "debitAccount");
	});
	
	document.getElementById("btnCreditAdd").addEventListener("click", () => {
	  addRowToGrid(creditGrid, "creditAccount");
	});
	
	document.getElementById("btnDebitDelete").addEventListener("click", () => {
	  deleteCheckedRows(debitGrid);
	});
	
	// 대변 삭제
	document.getElementById("btnCreditDelete").addEventListener("click", () => {
	  deleteCheckedRows(creditGrid);
	});

	document.getElementById("btnSave").addEventListener("click", saveSlip);

})