/**
 * userManage.js
 */

console.log(window.tui);
console.log(window.tui && window.tui.DatePicker);

document.addEventListener("DOMContentLoaded", async () => {
	/* ------------------------------------------------------------------
	 * 0) DOM 유틸
	 * ------------------------------------------------------------------ */
	const $ = (sel) => document.querySelector(sel);

	/* ------------------------------------------------------------------
	 * 1) Toast UI Grid 생성
	 * ------------------------------------------------------------------ */
	let grid;

	function initGrid() {
	  grid = new tui.Grid({
	    el: document.getElementById('userList'),
	    bodyHeight: 300,
	    rowHeaders: ['checkbox'],
	    columns: [
			{ header: "사원번호", name: "userId"},
			{ header: "성명", name: "userName"},
			{ header: "부서명", name: "dept"},
			{ header: "입사일", name: "hireDate"},
			// 직위/직급 (c1~c6 → 한글)
			{
			  header: "직위/직급",
			  name: "jobTitle",
			  formatter({ value }) {
			    const jobTitleMap = {
			      c1: "사원",
			      c2: "주임",
			      c3: "대리",
			      c4: "과장",
			      c5: "차장",
			      c6: "부장"
			    };
			    return jobTitleMap[value] || value; // 없으면 그냥 원래 값(c1 등) 보여주기
			  }
			},
			// 직책 (d1~d2 → 한글)
			{
			  header: "직책",
			  name: "position",
			  formatter({ value }) {
			    const positionMap = {
			      d1: "팀원",
			      d2: "팀장"
			    };
			    return positionMap[value] || value;
			  }
			},
			{ header: "연락처", name: "tel" },
			{ header: "Email", name: "email" }



	    ]
	  });
	}
	
	async function loadUserList(){
		const response = await fetch('/api/hr/empAllList');
		const data = await response.json();
		grid.resetData(data);
		grid.refreshLayout();
	}
	
	initGrid();
	loadUserList();
	

	/* ------------------------------------------------------------------
	 * 2) input 클릭시 달력 선택창 뜨게 하기
	 * ------------------------------------------------------------------ */
	
	// 2-1) 공통 함수
	function setupNativeDatePicker(wrapperId, inputId) {
	  const wrapper = document.getElementById(wrapperId);
	  const input = document.getElementById(inputId);
	  if (!wrapper || !input) return;

	  // wrapper 아무 곳이나 클릭해도 달력 뜨게
	  wrapper.addEventListener('click', () => {
	    if (input.showPicker) {
	      input.showPicker();      // 크롬/엣지에서 달력 팝업
	    } else {
	      input.focus();           // 지원 안 하는 브라우저용 최소한의 처리
	    }
	  });

	  // input이 포커스를 얻었을 때도 자동으로 달력 띄우기 (원하면)
	  input.addEventListener('focus', () => {
	    if (input.showPicker) {
	      input.showPicker();
	    }
	  });
	}

	// 입사일 / 퇴사일 세팅
	setupNativeDatePicker('hireDateWrapper', 'hireDateInput');
	setupNativeDatePicker('leaveDateWrapper', 'leaveDateInput');
	
		

	/* ------------------------------------------------------------------
	 * 2) 목록 조회
	 * ------------------------------------------------------------------ */
/*	async function loadList() {
		const keywordEl = $("#keyword");
		const deptEl = $("#dept");
		const retiredYnEl = $("#retiredYn");

		const params = new URLSearchParams({
			keyword: keywordEl ? keywordEl.value : "",
			dept: deptEl ? deptEl.value : "",
			retiredYn: retiredYnEl && retiredYnEl.checked ? "Y" : "N",
		});

		const res = await fetch(`/hr/emp/list?${params.toString()}`);
		const data = await res.json();
		grid.resetData(data);
	}

	const btnSearch = $("#btnSearch");
	if (btnSearch) btnSearch.addEventListener("click", loadList);
	loadList();*/

	/* ------------------------------------------------------------------
	 * 3) 행 클릭 → 상세 조회
	 * ------------------------------------------------------------------ */
	grid.on("click", async (ev) => {
		if (ev.rowKey == null) return;

		const row = grid.getRow(ev.rowKey);
		const empNo = row.empNo;
		if (!empNo) return;

		const res = await fetch(`/hr/emp/${empNo}`);
		const detail = await res.json();

		// ✅ 새 HTML의 id들로 바인딩
		if ($("#empNo")) $("#empNo").value = detail.empNo ?? "";
		if ($("#empName")) $("#empName").value = detail.empName ?? "";
		if ($("#phone")) $("#phone").value = detail.phone ?? "";
		if ($("#email")) $("#email").value = detail.email ?? "";
		if ($("#hireDate")) $("#hireDate").value = detail.hireDate ?? "";
		if ($("#hireType")) $("#hireType").value = detail.hireType ?? "NEW";
		if ($("#deptCode")) $("#deptCode").value = detail.deptCode ?? "";
		if ($("#positionCode"))
			$("#positionCode").value = detail.positionCode ?? "";
		if ($("#dutyCode")) $("#dutyCode").value = detail.dutyCode ?? "";
		if ($("#workStatus"))
			$("#workStatus").value = detail.workStatus ?? "WORK";
		if ($("#zip")) $("#zip").value = detail.zip ?? "";
		if ($("#addr")) $("#addr").value = detail.addr ?? "";

		// ✅ 새로 바뀐 retireDate / reason 바인딩
		if ($("#retireDate"))
			$("#retireDate").value = detail.retireDate ?? "";
		if ($("#reason")) $("#reason").value = detail.reason ?? "";

		// ✅ 추가정보
		if ($("#bankName")) $("#bankName").value = detail.bankName ?? "";
		if ($("#accountNo")) $("#accountNo").value = detail.accountNo ?? "";
		if ($("#remark")) $("#remark").value = detail.remark ?? "";
	});

	/* ------------------------------------------------------------------
	 * 4) 저장(등록/수정)
	 * ------------------------------------------------------------------ */
	const btnSave = $("#btnSave");
	if (btnSave) {
		btnSave.addEventListener("click", async () => {
			const payload = {
				empNo: $("#empNo")?.value ?? "",
				empName: $("#empName")?.value ?? "",
				phone: $("#phone")?.value ?? "",
				email: $("#email")?.value ?? "",
				hireDate: $("#hireDate")?.value ?? "",
				hireType: $("#hireType")?.value ?? "NEW",
				deptCode: $("#deptCode")?.value ?? "",
				positionCode: $("#positionCode")?.value ?? "",
				dutyCode: $("#dutyCode")?.value ?? "",
				workStatus: $("#workStatus")?.value ?? "WORK",
				zip: $("#zip")?.value ?? "",
				addr: $("#addr")?.value ?? "",
				retireDate: $("#retireDate")?.value ?? "",
				reason: $("#reason")?.value ?? "",
				bankName: $("#bankName")?.value ?? "",
				accountNo: $("#accountNo")?.value ?? "",
				remark: $("#remark")?.value ?? "",
			};

			const res = await fetch("/hr/emp", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload),
			});

			if (res.ok) {
				alert("저장 완료");
				loadList();
			} else {
				alert("저장 실패");
			}
		});
	}

	/* ------------------------------------------------------------------
	 * 5) 탭 전환 (data-tab 기준으로 tab-xxx 보여주기)
	 * ------------------------------------------------------------------ */
	document.querySelectorAll(".tab-btn").forEach((btn) => {
		btn.addEventListener("click", () => {
			document
				.querySelectorAll(".tab-btn")
				.forEach((b) => b.classList.remove("active"));
			document
				.querySelectorAll(".tab-panel")
				.forEach((p) => p.classList.remove("active"));

			btn.classList.add("active");
			const targetId = "tab-" + btn.dataset.tab; // basic/cert/career
			const panel = $("#" + targetId);
			if (panel) panel.classList.add("active");
		});
	});

	/* ------------------------------------------------------------------
	 * 6) 사진 업로드 미리보기
	 * ------------------------------------------------------------------ */
	const photoInput = $("#userPhoto");
	if (photoInput) {
		photoInput.addEventListener("change", (e) => {
			const file = e.target.files?.[0];
			if (!file) return;

			const url = URL.createObjectURL(file);
			const img = $("#photoPreview");
			if (!img) return;

			img.src = url;
			img.classList.remove("d-none");
			$(".photo-uploader__placeholder")?.classList.add("d-none");
		});
	}

	/* ------------------------------------------------------------------
	 * 7) 초기화
	 * ------------------------------------------------------------------ */
	const btnReset = $("#btnReset");
	if (btnReset) {
		btnReset.addEventListener("click", () => {
			// 기본정보 탭 안 input/select만 비움
			document
				.querySelectorAll("#tab-basic input, #tab-basic select")
				.forEach((el) => {
					if (el.type === "checkbox") el.checked = false;
					else el.value = "";
				});

			// 사진 미리보기도 초기화
			const img = $("#photoPreview");
			if (img) {
				img.src = "";
				img.classList.add("d-none");
				$(".photo-uploader__placeholder")?.classList.remove("d-none");
			}
		});
	}

	/* ------------------------------------------------------------------
	 * (옵션) 엑셀 export/upload 버튼이 나중에 다시 생기면 여기만 살리면 됨
	 * 현재 HTML에는 없으므로 null 가드만 둠
	 * ------------------------------------------------------------------ */
	const exportBtn = $("#exportBtn");
	if (exportBtn) {
		exportBtn.addEventListener("click", () => {
			const now = new Date();
			const y = now.getFullYear();
			const m = String(now.getMonth() + 1).padStart(2, "0");
			const d = String(now.getDate()).padStart(2, "0");
			const today = `${y}${m}${d}`;

			grid.export("xlsx", {
				fileName: `사원목록_${today}`,
				useFormattedValue: true,
			});
		});
	}

	const excelUploadBtn = $("#excelUploadBtn");
	const excelFileInput = $("#excelFileInput");
	if (excelUploadBtn && excelFileInput) {
		excelUploadBtn.addEventListener("click", () =>
			excelFileInput.click()
		);
		excelFileInput.addEventListener("change", (e) => {
			const file = e.target.files?.[0];
			if (!file) return;
			alert(`선택된 파일: ${file.name}`);
		});
	}
	
	
	/* ------------------------------------------------------------------
	 * 8) 자격증 / 경력사항 동적 그리드 (추가 +, 수정 버튼)
	 * ------------------------------------------------------------------ */
	function setupSubGrid({ tbodyId, templateId, addBtnId, countId }) {
	  const tbody = document.getElementById(tbodyId);
	  const template = document.getElementById(templateId);
	  const addBtn = document.getElementById(addBtnId);
	  const countEl = countId ? document.getElementById(countId) : null;

	  if (!tbody || !template) return;

	  function updateCount() {
	    if (!countEl) return;
	    const rowCount = tbody.querySelectorAll("tr").length;
	    countEl.textContent = rowCount;
	  }

	  function addRow() {
	    const clone = template.content.firstElementChild.cloneNode(true);
	    tbody.appendChild(clone);
	    updateCount();
	  }

	  // 처음에 한 줄 깔고 싶으면 주석 해제
	  // addRow();

	  if (addBtn) {
	    addBtn.addEventListener("click", () => addRow());
	  }

	  // 향후 '수정' 동작용 – 지금은 포커스만 이동
	  tbody.addEventListener("click", (e) => {
	    const editBtn = e.target.closest(".btn-row-edit");
	    if (!editBtn) return;

	    const tr = editBtn.closest("tr");
	    const firstInput = tr?.querySelector("input, select, textarea");
	    if (firstInput) firstInput.focus();

	    // TODO: 나중에 "등록 후 수정만 가능" 로직 넣을 때 여기서
	    //       readonly 처리 / 상태 플래그 등을 제어하면 됨.
	    console.log("edit clicked row:", tr);
	  });
	}

	// 자격증
	setupSubGrid({
	  tbodyId: "certTbody",
	  templateId: "certRowTemplate",
	  addBtnId: "btnCertAdd",
	  countId: "certCount",
	});

	// 경력사항
	setupSubGrid({
	  tbodyId: "careerTbody",
	  templateId: "careerRowTemplate",
	  addBtnId: "btnCareerAdd",
	  countId: "careerCount",
	});
	
	
	/* ------------------------------------------------------------------
	 * 9) 사원 이력조회 모달
	 * ------------------------------------------------------------------ */
	const historyModal = document.querySelector("#historyModal");
	const historyCloseBtn = document.querySelector("#btnHistoryClose");
	const historyBackdrop = historyModal?.querySelector(".modal-layer__backdrop");
	const btnHistory = document.querySelector("#btnHistory");

	// 열기
	if (btnHistory && historyModal) {
	  btnHistory.addEventListener("click", () => {
	    // TODO: 선택한 행 정보에서 이름/사번 가져와서 세팅
	    const row = grid.getRow(grid.getFocusedCell()?.rowKey);
	    if (row) {
	      const nameEl = document.querySelector("#historyEmpName");
	      const noEl = document.querySelector("#historyEmpNo");
	      if (nameEl) nameEl.textContent = row.empName ?? "";
	      if (noEl) noEl.textContent = row.empNo ?? "";
	    }

	    historyModal.hidden = false;
	  });
	}

	// 닫기(버튼/백드롭)
	function closeHistoryModal() {
	  if (historyModal) historyModal.hidden = true;
	}

	if (historyCloseBtn) historyCloseBtn.addEventListener("click", closeHistoryModal);
	if (historyBackdrop) historyBackdrop.addEventListener("click", closeHistoryModal);

	// 모달 내부 탭 전환
	document.querySelectorAll(".history-tab-btn").forEach((btn) => {
	  btn.addEventListener("click", () => {
	    document
	      .querySelectorAll(".history-tab-btn")
	      .forEach((b) => b.classList.remove("active"));
	    document
	      .querySelectorAll(".history-tab-panel")
	      .forEach((p) => p.classList.remove("active"));

	    btn.classList.add("active");
	    const targetId = "history-tab-" + btn.dataset.tab; // dept / salary
	    document.getElementById(targetId)?.classList.add("active");
	  });
	});

	
	/* ------------------------------------------------------------------
	 * 10) 사원카드(인쇄) 모달
	 * ------------------------------------------------------------------ */
	const printPreviewModal = document.getElementById('printPreviewModal');
	const btnPrint = document.getElementById('btnPrint');              // 기존 인쇄 버튼
	const btnPrintPreviewClose = document.getElementById('btnPrintPreviewClose');
	const btnPrintPreviewConfirm = document.getElementById('btnPrintPreviewConfirm');

	const btnPrintPrev = document.getElementById('btnPrintPrev');
	const btnPrintNext = document.getElementById('btnPrintNext');
	const printPageCurrent = document.getElementById('printPageCurrent');
	const printPageTotal = document.getElementById('printPageTotal');
	const printPreviewPage = document.getElementById('printPreviewPage');

	// 나중에 체크된 사원들 카드 HTML을 여기 배열에 담는 구조로 가면 됨
	let printPages = [];   // ['<div>첫번째 카드</div>', '<div>두번째 카드</div>', ...]
	let printPageIndex = 0;

	function openPrintPreview() {
	  // TODO: 선택된 사원들로 printPages 채우기
	  // 예시 (당장은 플레이스홀더만):
	  if (printPages.length === 0) {
	    printPages = ['<div class="print-preview__placeholder">사원 카드 미리보기</div>'];
	  }

	  renderPrintPage();
	  printPreviewModal.removeAttribute('hidden');
	}

	function closePrintPreview() {
	  printPreviewModal.setAttribute('hidden', '');
	}

	// 현재 페이지 렌더링
	function renderPrintPage() {
	  const total = printPages.length;
	  const html = printPages[printPageIndex];

	  printPreviewPage.innerHTML = html;
	  printPageCurrent.textContent = printPageIndex + 1;
	  printPageTotal.textContent = total;

	  // 화살표 활성/비활성
	  btnPrintPrev.classList.toggle('is-disabled', printPageIndex === 0);
	  btnPrintNext.classList.toggle('is-disabled', printPageIndex === total - 1);
	}

	// 이벤트 바인딩
	btnPrint?.addEventListener('click', openPrintPreview);
	btnPrintPreviewClose?.addEventListener('click', closePrintPreview);
	btnPrintPreviewConfirm?.addEventListener('click', closePrintPreview);

	btnPrintPrev?.addEventListener('click', () => {
	  if (printPageIndex > 0) {
	    printPageIndex--;
	    renderPrintPage();
	  }
	});

	btnPrintNext?.addEventListener('click', () => {
	  if (printPageIndex < printPages.length - 1) {
	    printPageIndex++;
	    renderPrintPage();
	  }
	});
	
	
	
	/* ------------------------------------------------------------------
	 * 11) Kakao Address API
	 * ------------------------------------------------------------------ */
	function findAddress() {
	  new daum.Postcode({
	    oncomplete: function (data) {
	      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	      // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
	      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	      let roadAddr = data.roadAddress; // 도로명 주소 변수
	      let zonecode = data.zonecode; // 도로명 주소 변수

	      // 우편번호와 주소 정보를 해당 필드에 넣는다.
	      document.getElementById("address").value = roadAddr;
	      document.getElementById("zipCode").value = zonecode;
	    }
	  }).open();
	}

	// 지도 검색을 눌렀을 때 Kakao Address API 켜지도록 설정
	document.getElementById("btnZipSearch").addEventListener('click', findAddress);

	
	/* ------------------------------------------------------------------
	 * 12) 사원번호 생성
	 * ------------------------------------------------------------------ */
/*	const datePart = formatDate(orderDate);
	const lastList = await conn.query(sqlList.selectLastOutordNo, [`OO${datePart}%`]);
	let seq = 1;
	if (lastList.length > 0) {
	  const lastNo = lastList[0].OUTORD_NO;
	  const lastSeq = parseInt(lastNo.slice(-5)); // 마지막 5자리 추출
	  seq = lastSeq + 1;
	}
	let outordDate = formatFullDate(orderDate);
	let outdelDate = formatFullDate(deliveryDate);

	// 신규 발주번호 생성 (EMP + YYMMDD + 5자리SEQ)
	// 00 -> EMP (사원번호)
	const outordNo = `EMP${datePart}${String(seq).padStart(5, "0")}`;*/

	



	
	
});