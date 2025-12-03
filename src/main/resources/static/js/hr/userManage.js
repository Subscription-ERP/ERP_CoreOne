/**
 * userManage.js
 */

document.addEventListener("DOMContentLoaded", async () => {
	
	/* ------------------------------------------------------------------
	 * 0) common.js : 공통코드, 부서
	 * ------------------------------------------------------------------ */
	const divId = {'0A':'hireType', '0C':'jobTitle', '0D':'position', '0E':'userStatus'}
	getCmCodeOptions(divId);
	//getDeptOptions();
	
	// 부서 select 두 군데: 상세폼 + 검색폼
	getDeptOptions2([".form-grid #dept", "#dept-search"]);
	
	/* ------------------------------------------------------------------
	 * 1) DOM 유틸
	 * ------------------------------------------------------------------ */
	const $ = (sel) => document.querySelector(sel);

	/* ------------------------------------------------------------------
	 * 2) Toast UI Grid 생성
	 * ------------------------------------------------------------------ */
	let grid;

	function initGrid() {
	  grid = new tui.Grid({
	    el: document.getElementById('userList'),
	    bodyHeight: 300,
	    rowHeaders: ['checkbox'],
	    columns: [
			{ header: "사원번호", name: "userId", align: 'center', sortable: true},
			{ header: "성명", name: "userName"},
			{ header: "부서", name: "deptName"},
			{ header: "입사일", name: "hireDate", align: 'center' },
			{ header: "직위/직급", name: "jobTitleName"},
			{ header: "직책", name: "positionName"},
			{ header: "연락처", name: "tel" },
			{ header: "Email", name: "email" }

	    ]
	  });
	}
	
	
	/* ------------------------------------------------------------------
	 * 사원 전체 조회 + 스피너
	 * ------------------------------------------------------------------ */
	async function loadUserList(){
		globalLoader.style.display = "flex";
		const response = await fetch('/api/hr/userAllList');
		const data = await response.json();
		grid.resetData(data);
		grid.refreshLayout();
		globalLoader.style.display = "none";
	}
	
	initGrid();
	loadUserList();

	
	/* ------------------------------------------------------------------
	 * 사원 검색 (조회버튼 클릭)
	 * ------------------------------------------------------------------ */
	const btnSearch = document.querySelector("#btnSearch");

	btnSearch.addEventListener('click', () => {
	  const keyName    = document.querySelector("#keyName").value.trim();
	  const deptSearch = document.querySelector("#dept-search").value;
	  const leavedYN   = document.querySelector("#leavedYN").checked ? 'Y' : '';

	  const params = new URLSearchParams();
	  if (keyName)    params.append('keyName', keyName);
	  if (deptSearch) params.append('deptSearch', deptSearch);
	  if (leavedYN)   params.append('leavedYN', leavedYN);

	  fetch(`/api/hr/userSearch?${params.toString()}`)
	    .then(res => res.json())
	    .then(data => {
	      grid.resetData(data);
	      grid.refreshLayout();
	    })
	    .catch(err => console.error(err));
	});
	
	
	/* ------------------------------------------------------------------
	 * input 클릭시 달력 선택창 뜨게 하기
	 * ------------------------------------------------------------------ */
	
	// 공통 함수
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
	 * 사원 상세조회 (기본정보/자격증/경력사항/이력)
	 * ------------------------------------------------------------------ */
	
	 let currentUserDetail = null;
	
     grid.on('click', (ev) => {
		const rowKey = ev.rowKey;
		
		if(rowKey == null) return;
		
		const row = grid.getRow(rowKey);
		const userId = row.userId;
		
		fetch(`/api/hr/userDetail?userId=${userId}`)
			.then(response => response.json())
			.then(data => {
				currentUserDetail = data; 
				InputUserBasicInfo(data);
				InputCertification(data.certificationList);
				InputWorkExperience(data.workExperienceList);
				//InputUserHistory(data.historyList);
			});
	});
	
	// 기본정보
	function InputUserBasicInfo(data){
		if (!data){
			resetData();
			return;
		}
		const fr= document.querySelector(".form-allwrapper");
		fr.querySelector("#userId").value = data.userId;
		fr.querySelector("#userName").value = data.userName;
		fr.querySelector("#tel").value = data.tel;
		fr.querySelector("#email").value = data.email;
		fr.querySelector("#hireDate").value = data.hireDate;
		fr.querySelector("#hireType").value = data.hireType;
		fr.querySelector("#leaveDate").value = data.leaveDate;
		fr.querySelector("#leaveReason").value = data.leaveReason;
		fr.querySelector("#zipCode").value = data.zipCode;
		fr.querySelector("#address").value = data.address;
		fr.querySelector("#dept").value = data.dept;
		fr.querySelector("#jobTitle").value = data.jobTitle;
		fr.querySelector("#position").value = data.position;
		fr.querySelector("#familyCount").value = data.familyCount;
		fr.querySelector("#childrenCount").value = data.childrenCount;
		
		const householder = fr.querySelector("#householder");
		// DB 값이 'Y'면 체크, 아니면 해제
		householder.checked = (data.householder === 'Y');
		
		fr.querySelector("#bankName").value = data.bankName;
		fr.querySelector("#accountNo").value = data.accountNo;
		fr.querySelector("#accountHolder").value = data.accountHolder;
		fr.querySelector("#salary").value = data.salary;
		fr.querySelector("#userStatus").value = data.userStatus;
		//fr.querySelector("#userFile").value = data.userFile;
		fr.querySelector("#remark").value = data.remark;
		
		
		// DB 사원사진 보여주기
		const photoPreview = document.querySelector("#photoPreview");
		const photoPlaceholder = document.querySelector(".photo-uploader__placeholder");

		if (photoPreview) {
		  if (data.userPhoto) {
		    // 예: data.userPhoto = "user/photo/uuid_abc.jpg"
		    photoPreview.src = `/upload/${data.userPhoto}`;
		    photoPreview.classList.remove("d-none");
		    photoPlaceholder?.classList.add("d-none");
		  } else {
		    photoPreview.src = "";
		    photoPreview.classList.add("d-none");
		    photoPlaceholder?.classList.remove("d-none");
			
		  }
		}
		
		  // 첨부파일 정보 표시 (버튼에 경로/파일명 저장)
		  const fileDownloadEl = document.querySelector("#userFileDownload");

		  if (fileDownloadEl) {
		    if (data.userFile) {
		      // data.userFile 예: "user/file/uuid_이력서.pdf"
		      const filePath = `/upload/${data.userFile}`;

		      // 클릭 시 사용할 실제 URL을 dataset에 저장
		      fileDownloadEl.dataset.filePath = filePath;
		      fileDownloadEl.classList.remove("d-none");

		      // 옵션: 호버 시 파일명 보이게 (툴팁)
		      const fileName = data.userFile.split('/').pop();
		      fileDownloadEl.title = fileName;
		    } else {
		      // 파일 없으면 버튼 숨기고 관련 데이터 제거
		      fileDownloadEl.dataset.filePath = "";
		      fileDownloadEl.classList.add("d-none");
		      fileDownloadEl.removeAttribute("title");
		    }
		  }
			
		
	}
	
	
	// 자격증 
	function InputCertification(certList) {
	  const tbody = document.querySelector('#certTbody');
	  const template = document.querySelector('#certRowTemplate');
	  const countEl = document.querySelector('#certCount');

	  // 0) certList 없으면 바로 "데이터 없음"
	  if (!Array.isArray(certList)) {
	    tbody.innerHTML = `
	      <tr class="empty-row">
	        <td colspan="8" class="text-center text-muted py-3">
	          데이터가 없습니다. 추가해주세요.
	        </td>
	      </tr>
	    `;
	    if (countEl) countEl.textContent = 0;
	    return;
	  }

	  // 1) null/빈 데이터 제거해서 "실제 데이터만" 남기기
	  const validList = certList.filter((item) => {
	    if (!item) return false; // null, undefined 제거

	    // 모든 필드가 비어 있는 객체는 의미 없는 행으로 판단
	    const hasValue =
	      (item.certiName && item.certiName.trim() !== '') ||
	      (item.issueOrgName && item.issueOrgName.trim() !== '') ||
	      (item.getDate && item.getDate.trim() !== '') ||
	      (item.licenseNo && item.licenseNo.trim() !== '') ||
	      (item.expireDate && item.expireDate.trim() !== '') ||
	      (item.remark && item.remark.trim() !== '');

	    return hasValue;
	  });

	  // 2) 유효한 데이터가 하나도 없으면 "데이터 없음" 표시
	  if (validList.length === 0) {
	    tbody.innerHTML = `
	      <tr class="empty-row">
	        <td colspan="8" class="text-center text-muted py-3">
	          데이터가 없습니다. 추가해주세요.
	        </td>
	      </tr>
	    `;
	    if (countEl) countEl.textContent = 0;
	    return;
	  }

	  // 3) 유효 데이터가 있을 때만 테이블 렌더링
	  tbody.innerHTML = '';
	  validList.forEach((item) => {
	    const fragment = template.content.cloneNode(true);
	    const row = fragment.querySelector('tr');

	    row.querySelector('input[name="certiName"]').value    = item.certiName    ?? '';
	    row.querySelector('input[name="issueOrgName"]').value = item.issueOrgName ?? '';
	    row.querySelector('input[name="getDate"]').value      = item.getDate      ?? '';
	    row.querySelector('input[name="licenseNo"]').value    = item.licenseNo    ?? '';
	    row.querySelector('input[name="expireDate"]').value   = item.expireDate   ?? '';
	    row.querySelector('input[name="remark"]').value       = item.remark       ?? '';
		
		// 첨부파일 있을시 다운로드버튼 생성
		const fileBtn = row.querySelector('.cert-file-download');

		if (fileBtn) {
		  if (item.certiFile) {                    
		    const filePath = item.certiFile;       // 예: "cert/uuid_자격증.pdf"

		    // 버튼 보이게
		    fileBtn.classList.remove('d-none');

		    // 나중에 클릭 시 사용할 수 있도록 경로 저장
		    fileBtn.dataset.filePath = `/upload/${filePath}`;

		    // 옵션: 호버 시 파일명 보이게 (툴팁)
		    const fileName = filePath.split('/').pop();
		    fileBtn.title = fileName;
		  } else {
		    // 파일 없으면 버튼 숨기기 + 이전 정보 제거
		    fileBtn.classList.add('d-none');
		    fileBtn.removeAttribute('data-file-path');
		    fileBtn.removeAttribute('title');
		  }
		}
		

	    tbody.appendChild(fragment);
	  });

	  if (countEl) countEl.textContent = validList.length;
	}

	
	// 경력사항
	function InputWorkExperience(workList) {
	  const tbody = document.querySelector('#careerTbody');
	  const template = document.querySelector('#careerRowTemplate');
	  const countEl = document.querySelector('#careerCount');

	  // 0) 배열이 아니면 바로 "데이터 없음"
	  if (!Array.isArray(workList)) {
	    tbody.innerHTML = `
	      <tr class="empty-row">
	        <td colspan="8" class="text-center text-muted py-3">
	          데이터가 없습니다. 추가해주세요.
	        </td>
	      </tr>
	    `;
	    if (countEl) countEl.textContent = 0;
	    return;
	  }

	  // 1) null/빈 객체 제거해서 "실제 데이터 있는 것만" 필터링
	  const validList = workList.filter((item) => {
	    if (!item) return false; // null, undefined 제거

	    const hasValue =
	      (item.wexCompanyName && item.wexCompanyName.trim() !== '') ||
	      (item.wexDept && item.wexDept.trim() !== '') ||
	      (item.wexJobTitle && item.wexJobTitle.trim() !== '') ||
	      (item.wexHireDate && item.wexHireDate.trim() !== '') ||
	      (item.wexLeaveDate && item.wexLeaveDate.trim() !== '') ||
	      (item.wexMainDuty && item.wexMainDuty.trim() !== '') ||
	      (item.wexSalary && item.wexSalary.toString().trim() !== '');

	    return hasValue;
	  });

	  // 2) 유효한 데이터 0개면 placeholder + count 0
	  if (validList.length === 0) {
	    tbody.innerHTML = `
	      <tr class="empty-row">
	        <td colspan="8" class="text-center text-muted py-3">
	          데이터가 없습니다. 추가해주세요.
	        </td>
	      </tr>
	    `;
	    if (countEl) countEl.textContent = 0;
	    return;
	  }

	  // 3) 유효 데이터 있을 때만 테이블 렌더링
	  tbody.innerHTML = '';
	  validList.forEach((item) => {
	    const fragment = template.content.cloneNode(true);
	    const row = fragment.querySelector('tr');

	    row.querySelector('input[name="wexCompanyName"]').value = item.wexCompanyName ?? '';
	    row.querySelector('input[name="wexDept"]').value        = item.wexDept ?? '';
	    row.querySelector('input[name="wexJobTitle"]').value    = item.wexJobTitle ?? '';
	    row.querySelector('input[name="wexHireDate"]').value    = item.wexHireDate ?? '';
	    row.querySelector('input[name="wexLeaveDate"]').value   = item.wexLeaveDate ?? '';
	    row.querySelector('input[name="wexMainDuty"]').value    = item.wexMainDuty ?? '';
	    row.querySelector('input[name="wexSalary"]').value      = item.wexSalary ?? '';

	    tbody.appendChild(fragment);
	  });

	  if (countEl) countEl.textContent = validList.length;
	}
	
	
	/* ------------------------------------------------------------------
	 * 초기화
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
			
			// 자격증 초기화
			const certTbody = document.querySelector('#certTbody');
			certTbody.innerHTML = `
			  <tr class="empty-row">
			    <td colspan="8" class="text-center text-muted py-3">
			      데이터가 없습니다. 추가해주세요.
			    </td>
			  </tr>
			`;
			document.querySelector("#certCount").textContent = 0;
			
			// 경력사항 초기화
			const wex_tbody = document.querySelector('#careerTbody');
			wex_tbody.innerHTML = 			`
			  <tr class="empty-row">
			    <td colspan="8" class="text-center text-muted py-3">
			      데이터가 없습니다. 추가해주세요.
			    </td>
			  </tr>
			`;
			document.querySelector("#careerCount").textContent = 0;
			
		});
	}
	
	
	
	/* ------------------------------------------------------------------
	 * 자격증 / 경력사항 동적 그리드 (추가 +, 수정 버튼)
	 * ------------------------------------------------------------------ */
	function setupSubGrid({ tbodyId, templateId, addBtnId, countId }) {
	  const tbody = document.getElementById(tbodyId);
	  const template = document.getElementById(templateId);
	  const addBtn = document.getElementById(addBtnId);
	  const countEl = countId ? document.getElementById(countId) : null;

	  if (!tbody || !template) return;

	  function updateCount() {
	    if (!countEl) return;
	    const rowCount = tbody.querySelectorAll("tr:not(.empty-row)").length;
	    countEl.textContent = rowCount;
	  }

	  function addRow() {
		// "데이터가 없습니다" placeholder 있으면 제거
		const emptyRow = tbody.querySelector('.empty-row');
		if (emptyRow) emptyRow.remove();
		
	    const clone = template.content.firstElementChild.cloneNode(true);
	    tbody.appendChild(clone);
	    updateCount();
	  }

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
	 * 탭 전환 
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
	 * 사진 업로드 미리보기
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
	 * Kakao Address API
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
	 * 사원 이력조회 모달
	 * ------------------------------------------------------------------ */
	const historyModal = document.querySelector("#historyModal");
	const historyCloseBtn = document.querySelector("#btnHistoryClose");
	const historyBackdrop = historyModal?.querySelector(".modal-layer__backdrop");
	const btnHistory = document.querySelector("#btnHistory");

	// 열기
	if (btnHistory && historyModal) {
	  btnHistory.addEventListener("click", () => {

	    // 1) 상세조회 데이터 없으면 막기
        if (!currentUserDetail) {
	      showToast("사원 상세정보가 없습니다. 먼저 사원을 선택해 주세요.", 'warning');
	      return;
	    }

	    // 2) 성명 / 사번 세팅
	    const nameEl = document.querySelector("#historyModal #userName");
	    const noEl   = document.querySelector("#historyModal #userId");

	    if (nameEl) nameEl.textContent = currentUserDetail.userName ?? "";
	    if (noEl)   noEl.textContent   = currentUserDetail.userId ?? "";

	    // 3) 이력 테이블 렌더링
		const list = currentUserDetail.historyList || [];
		renderHistoryTables(list);

	    // 4) 모달 열기
	    historyModal.hidden = false;
	  });
	}

	// 사원 이력 테이블 그리기
	function renderHistoryTables(historyList) {
	  const deptBody = document.querySelector("#historyDeptTbody");
	  const salaryBody = document.querySelector("#historySalaryTbody");

	  if (!deptBody || !salaryBody) return;

	  deptBody.innerHTML = "";
	  salaryBody.innerHTML = "";

	  // historyList가 null이거나 빈 배열인 경우
	  if (!Array.isArray(historyList) || historyList.length === 0) {
	    deptBody.innerHTML = `<tr><td colspan="6" class="text-center">부서 이력이 없습니다.</td></tr>`;
	    salaryBody.innerHTML = `<tr><td colspan="6" class="text-center">급여 이력이 없습니다.</td></tr>`;
	    return;
	  }

	  historyList.forEach(h => {
	    if (!h) return; // 항목 자체가 null일 때 skip

	    // 🔹 부서 이력 (f1)
	    if (h.histType === "f1") {
	      const tr = document.createElement("tr");
	      tr.innerHTML = `
	        <td>${h.applyDate ?? ""}</td>
	        <td>${h.prevDeptName ?? ""}</td>
	        <td>${h.prevJobTitleName ?? ""}</td>
	        <td>${h.newDeptName ?? ""}</td>
	        <td>${h.newJobTitleName ?? ""}</td>
	        <td>${h.deptChangeReason ?? ""}</td>
	      `;
	      deptBody.appendChild(tr);
	    }

	    // 🔹 급여 이력 (f2)
	    if (h.histType === "f2") {
	      const tr = document.createElement("tr");
	      tr.innerHTML = `
	        <td>${h.applyDate ?? ""}</td>
	        <td>${formatNumber(h.baseSalary) || ""}</td>
	        <td>${formatNumber(h.totalSalary) || ""}</td>
	        <td>${h.payTypeName ?? ""}</td>
	        <td>${h.salaryChangeReason ?? ""}</td>
	        <td>${h.updatedBy ?? h.createdBy ?? ""}</td>
	      `;
	      salaryBody.appendChild(tr);
	    }
	  });

	  // 🔥 혹시 f1 또는 f2가 아예 없는 경우에 대비
	  if (deptBody.children.length === 0) {
	    deptBody.innerHTML = `<tr><td colspan="6" class="text-center">부서 이력이 없습니다.</td></tr>`;
	  }
	  if (salaryBody.children.length === 0) {
	    salaryBody.innerHTML = `<tr><td colspan="6" class="text-center">급여 이력이 없습니다.</td></tr>`;
	  }
	}

	// 금액 포맷용 (65000000 -> "65,000,000")
	function formatNumber(value) {
	  if (value === null || value === undefined) return "";
	  return value.toLocaleString();
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
	 * 사원카드(인쇄) 모달
	 * ------------------------------------------------------------------ */
	const printPreviewModal = document.getElementById('printPreviewModal');
	const btnPrint = document.getElementById('btnPrint');              
	const btnPrintPreviewClose = document.getElementById('btnPrintPreviewClose');
	const btnPrintPreviewConfirm = document.getElementById('btnPrintPreviewConfirm');

	const btnPrintPrev = document.getElementById('btnPrintPrev');
	const btnPrintNext = document.getElementById('btnPrintNext');
	const printPageCurrent = document.getElementById('printPageCurrent');
	const printPageTotal = document.getElementById('printPageTotal');
	const printPreviewPage = document.getElementById('printPreviewPage');

	// 선택된 사원 카드 미리보기용 배열
	let printPages = [];   
	let printPageIndex = 0;

	// 인쇄 버튼 클릭 시
	btnPrint?.addEventListener('click', () => {
	  // 1) 체크된 사원들 가져오기
	  const checkedRows = grid.getCheckedRows();

	  if (!checkedRows || checkedRows.length === 0) {
	    showToast('인쇄할 사원을 먼저 선택해 주세요.', 'warning');
	    return;
	  }

	  // 2) 각 사원의 userId 로 PDF URL 만들기 → iframe HTML 생성
	  printPages = checkedRows.map(row => {
	    const userId = row.userId;
	    const url = `/api/hr/userCard/preview?userId=${encodeURIComponent(userId)}`;

	    return `
	      <iframe
	        src="${url}"
	        style="width:100%;height:100%;border:none;"
	      ></iframe>
	    `;
	  });

	  // 3) 첫 페이지로 세팅 후 모달 오픈
	  printPageIndex = 0;
	  renderPrintPage();
	  printPreviewModal.removeAttribute('hidden');
	});


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

	  btnPrintPrev.classList.toggle('is-disabled', printPageIndex === 0);
	  btnPrintNext.classList.toggle('is-disabled', printPageIndex === total - 1);
	}

	btnPrintPreviewClose.addEventListener('click', closePrintPreview);
	
	// 확인버튼 : 선택된 사원카드 PDF 모두 다운로드
	btnPrintPreviewConfirm.addEventListener('click', () => {
		// 현재 체크된 행 다시 조회
		const checkedRows = grid.getCheckedRows();
		
		// 각 사원에 대해 a 태그를 만들어 클릭 -> 브라우저가 다운로드 처리
		checkedRows.forEach(row => {
			const userId = row.userId;
			const url = `/api/hr/userCard/download?userId=${encodeURIComponent(userId)}`;
			
			const a = document.createElement('a');
			a.href = url;
			a.download = `userCard-${userId}.pdf`;
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
		});
		
		// 다운로드 후 모달 닫기
		closePrintPreview();
	});

	// 페이지네이션
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
	 * 이력 모달 - 인쇄 
	 * ------------------------------------------------------------------ */

	const btnHistoryPrint = document.querySelector("#btnHistoryPrint");

	if (btnHistoryPrint) {
	  btnHistoryPrint.addEventListener("click", () => {
		const userId = currentUserDetail.userId;
		window.open(`/api/hr/userHistory/preview?userId=${encodeURIComponent(userId)}`, "_blank");
	  });
	}
	
	/* ------------------------------------------------------------------
	 * 저장(등록/수정)
	 * ------------------------------------------------------------------ */
	const btnSave = $("#btnSave");
	if (btnSave) {
	  btnSave.addEventListener("click", async () => {
	    const fr = document.querySelector(".form-allwrapper");

	    // 1) 기본정보 수집
	    const householderChecked = fr.querySelector("#householder").checked ? "Y" : "N";

	    const userPayload = {
	      companyCode: "0000",  // TODO: 나중에 로그인 회사코드로 대체
	      userId: fr.querySelector("#userId")?.value ?? "",
	      userName: fr.querySelector("#userName")?.value ?? "",
	      tel: fr.querySelector("#tel")?.value ?? "",
	      email: fr.querySelector("#email")?.value ?? "",
	      hireDate: fr.querySelector("#hireDate")?.value ?? "",
	      hireType: fr.querySelector("#hireType")?.value ?? "",
	      leaveDate: fr.querySelector("#leaveDate")?.value ?? "",
	      leaveReason: fr.querySelector("#leaveReason")?.value ?? "",
	      zipCode: fr.querySelector("#zipCode")?.value ?? "",
	      address: fr.querySelector("#address")?.value ?? "",
	      dept: fr.querySelector("#dept")?.value ?? "",
	      jobTitle: fr.querySelector("#jobTitle")?.value ?? "",
	      position: fr.querySelector("#position")?.value ?? "",
	      familyCount: fr.querySelector("#familyCount")?.value || null,
	      childrenCount: fr.querySelector("#childrenCount")?.value || null,
	      householder: householderChecked,
	      bankName: fr.querySelector("#bankName")?.value ?? "",
	      accountNo: fr.querySelector("#accountNo")?.value ?? "",
	      accountHolder: fr.querySelector("#accountHolder")?.value ?? "",
	      salary: fr.querySelector("#salary")?.value || null,
	      userStatus: fr.querySelector("#userStatus")?.value ?? "",
	      remark: fr.querySelector("#remark")?.value ?? "",
	      // userPhoto / userFile 컬럼은 파일 업로드 후 서버에서 path 세팅
	    };

	    // 2) 자격증 리스트
	    const certificationList = [];
	    document
	      .querySelectorAll("#certTbody tr:not(.empty-row)")
	      .forEach(tr => {
	        const certiName    = tr.querySelector('input[name="certiName"]')?.value.trim() ?? "";
	        const issueOrgName = tr.querySelector('input[name="issueOrgName"]')?.value.trim() ?? "";
	        const getDate      = tr.querySelector('input[name="getDate"]')?.value.trim() ?? "";
	        const licenseNo    = tr.querySelector('input[name="licenseNo"]')?.value.trim() ?? "";
	        const expireDate   = tr.querySelector('input[name="expireDate"]')?.value.trim() ?? "";
	        const remark       = tr.querySelector('input[name="remark"]')?.value.trim() ?? "";

	        if (!certiName && !issueOrgName && !getDate &&
	            !licenseNo && !expireDate && !remark) {
	          return; // 완전 빈 행은 스킵
	        }

	        certificationList.push({
	          certiName,
	          issueOrgName,
	          getDate,
	          licenseNo,
	          expireDate,
	          remark
	        });
	      });

	    // 3) 경력사항 리스트
	    const workExperienceList = [];
	    document
	      .querySelectorAll("#careerTbody tr:not(.empty-row)")
	      .forEach(tr => {
	        const wexCompanyName = tr.querySelector('input[name="wexCompanyName"]')?.value.trim() ?? "";
	        const wexDept        = tr.querySelector('input[name="wexDept"]')?.value.trim() ?? "";
	        const wexJobTitle    = tr.querySelector('input[name="wexJobTitle"]')?.value.trim() ?? "";
	        const wexHireDate    = tr.querySelector('input[name="wexHireDate"]')?.value.trim() ?? "";
	        const wexLeaveDate   = tr.querySelector('input[name="wexLeaveDate"]')?.value.trim() ?? "";
	        const wexMainDuty    = tr.querySelector('input[name="wexMainDuty"]')?.value.trim() ?? "";
	        const wexSalary      = tr.querySelector('input[name="wexSalary"]')?.value.trim() ?? "";

	        if (!wexCompanyName && !wexDept && !wexJobTitle &&
	            !wexHireDate && !wexLeaveDate && !wexMainDuty && !wexSalary) {
	          return;
	        }

	        workExperienceList.push({
	          wexCompanyName,
	          wexDept,
	          wexJobTitle,
	          wexHireDate,
	          wexLeaveDate,
	          wexMainDuty,
	          wexSalary
	        });
	      });

	    // 4) 최종 JSON 객체
	    const payload = {
	      ...userPayload,
	      certificationList,
	      workExperienceList
	    };

	    // 5) FormData 생성
	    const formData = new FormData();

	    // (1) user JSON을 하나의 Part로 넣기
	    formData.append(
	      "user", // @RequestPart("user") 와 매칭
	      new Blob([JSON.stringify(payload)], { type: "application/json" })
	    );

	    // (2) 기본정보 파일들
	    const photoFile = document.querySelector("#userPhoto")?.files[0];
	    if (photoFile) {
	      formData.append("userPhoto", photoFile); // @RequestPart("userPhoto")
	    }

	    const attachFile = document.querySelector("#userFile")?.files[0];
	    if (attachFile) {
	      formData.append("userFile", attachFile); // @RequestPart("userFile")
	    }

	    // (3) 자격증 파일들 (여러 개 가능)
	    const certRows = document.querySelectorAll("#certTbody tr:not(.empty-row)");
	    certRows.forEach(tr => {
	      const fileInput = tr.querySelector('input[type="file"][name="certiFile"]');
	      if (!fileInput || !fileInput.files[0]) return;

	      const file = fileInput.files[0];
	      // 같은 key로 여러 번 append → List<MultipartFile> certiFiles 로 바인딩
	      formData.append("certiFiles", file);  // @RequestPart("certiFiles")
	    });

		
		// userId 유무로 신규/수정 구분
		const isNew = !payload.userId || payload.userId.trim() === "";
		const url = isNew ? "/api/hr/userRegister" : "/api/hr/userModify";
		
	    try {
	      const res = await fetch(url, {
	        method: "POST",
	        body: formData,   // ⚠ 여기서 절대 headers에 Content-Type 넣지 말기!
	      });

		  if (res.ok) {
		      const text = await res.text();
		      if (text === "success") {
		        showToast(
		          isNew ? "사원 정보가 등록되었습니다." : "사원 정보가 수정되었습니다.",
		          "success"
		        );
		        loadUserList();
		        btnReset?.click();
		      } else {
		        showToast(
		          isNew ? "등록 처리에 실패했습니다." : "수정 처리에 실패했습니다.",
		          "error"
		        );
		      }
		    } else {
		      showToast("서버 오류가 발생했습니다.", "error");
		    }
		  } catch (err) {
		    console.error(err);
		    showToast("통신 중 오류가 발생했습니다.", "error");
		  }
		  
		  
	  });
	}


	
	
	
	/* ------------------------------------------------------------------
	 * 사원-기본정보 첨부파일 / 자격증 첨부파일 다운로드 
	 * ------------------------------------------------------------------ */
	document.addEventListener("click", (e) => {
	  const btn = e.target.closest("#userFileDownload");
	  if (!btn) return;

	  const filePath = btn.dataset.filePath;
	  if (!filePath) return;

	  // 파일명 추출 (예: /upload/user/file/uuid_이력서.pdf -> uuid_이력서.pdf)
	  const fileName = filePath.split("/").pop() || "download";

	  // 가상의 <a> 태그를 만들어 강제 다운로드
	  const a = document.createElement("a");
	  a.href = filePath;
	  a.download = fileName;   // 다운로드 시 저장될 파일명
	  document.body.appendChild(a);
	  a.click();
	  document.body.removeChild(a);
	});

	// 자격증 첨부파일 다운로드 버튼 클릭 핸들러 (무조건 다운로드)
	const certTbodyEl = document.querySelector("#certTbody");
	if (certTbodyEl) {
	  certTbodyEl.addEventListener("click", (e) => {
	    const btn = e.target.closest(".cert-file-download");
	    if (!btn) return;

	    const filePath = btn.dataset.filePath;
	    if (!filePath) return;

	    const fileName = filePath.split("/").pop() || "download";

	    const a = document.createElement("a");
	    a.href = filePath;
	    a.download = fileName;
	    document.body.appendChild(a);
	    a.click();
	    document.body.removeChild(a);
	  });
	}	


	/* ------------------------------------------------------------------
	 * (옵션) 엑셀 export/upload 버튼이 나중에 다시 생기면 여기만 살리면 됨
	 * 현재 HTML에는 없으므로 null 가드만 둠
	 * ------------------------------------------------------------------ */
/*	const exportBtn = $("#exportBtn");
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
	*/
	
	
	

	



	
	
});