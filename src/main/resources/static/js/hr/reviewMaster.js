/**
 * reviewMaster.js (인사평가 기준관리)
 */

document.addEventListener("DOMContentLoaded", async () => {


	/* ------------------------------------------------------------------
	 * common.js
	 * ------------------------------------------------------------------ */
	// input 클릭시 달력 선택창 뜨게 하기
	setupNativeDatePicker('reviewStartDate-searchWrapper', 'reviewStartDate-search');
	setupNativeDatePicker('reviewEndDate-searchWrapper', 'reviewEndDate-search');
	setupNativeDatePicker('reviewStartDateWrapper', 'reviewStartDate');
	setupNativeDatePicker('reviewEndDateWrapper', 'reviewEndDate');

	// 동적 테이블 (평가항목)
	setupSubGrid({
		tbodyId: "evalTbody",
		templateId: "evalRowTemplate",
		addBtnId: "btnEvalAdd",
	});



	/* ------------------------------------------------------------------
	 * Toast UI Grid 생성
	 * ------------------------------------------------------------------ */
	let grid;

	function initGrid() {
		grid = new tui.Grid({
			el: document.getElementById('reviewList'),
			bodyHeight: 180,
			rowHeaders: ['rowNum'],
			columns: [
				{ header: "인사평가 템플릿명", name: "reviewMasterName" },
				{ header: "시작일", name: "reviewStartDate" },
				{ header: "종료일", name: "reviewEndDate" },
				{ header: "사용여부", name: "useYn" },
			]
		});
	}
	
	
	
	/* ------------------------------------------------------------------
	 * 인사평가목록 전체조회
	 * ------------------------------------------------------------------ */
	async function loadUserList() {
		globalLoader.style.display = "flex";
		const response = await fetch('/api/review');
		const data = await response.json();
		grid.resetData(data);
		grid.refreshLayout();
		globalLoader.style.display = "none";
	}

	initGrid();
	loadUserList();


	/* ------------------------------------------------------------------
	 * 인사평가 상세조회
	 * ------------------------------------------------------------------ */

	let currentReviewDetail = null;

	grid.on('click', (ev) => {
		const rowKey = ev.rowKey;

		if (rowKey == null) return;

		const row = grid.getRow(rowKey);
		const rmCode = row.reviewMasterCode;

		fetch(`/api/review/detail?rmCode=${rmCode}`)
			.then(response => response.json())
			.then(data => {
				currentReviewDetail = data;
				InputReivewInfo(data);
				InputEvalItem(data.evalItemList);
			});
	});

	// 기본정보
	function InputReivewInfo(data) {
		if (!data) {
			resetData();
			return;
		}

		const fr = document.querySelector(".form-allwrapper");
		fr.querySelector("#reviewMasterName").value = data.reviewMasterName;
		fr.querySelector("#reviewStartDate").value = data.reviewStartDate;
		fr.querySelector("#reviewEndDate").value = data.reviewEndDate;
		fr.querySelector("#remark").value = data.remark;

		// 라디오 버튼
		const useYnRadio = fr.querySelector(`input[name="useYn"][value="${data.useYn}"]`);
		if (useYnRadio) {
			useYnRadio.checked = true;
		}
	}

	// 평가항목
	function InputEvalItem(evalItemList) {
		const tbody = document.querySelector("#evalTbody");
		const template = document.querySelector("#evalRowTemplate");

		// evalItem 데이터 없을시
		if (!Array.isArray(evalItemList)) {
			tbody.innerHTML = `
				      <tr class="empty-row">
				        <td colspan="8" class="text-center text-muted py-3">
				          데이터가 없습니다. 추가해주세요.
				        </td>
				      </tr>
				    `;
			return;
		}

		// 1) null/빈 데이터 제거해서 "실제 데이터만" 남기기
		const validList = evalItemList.filter((item) => {
			if (!item) return false; // null, undefined 제거

			// 모든 필드가 비어 있는 객체는 의미 없는 행으로 판단
			const hasValue =
				(item.evalSeq && item.evalSeq !== '') ||
				(item.reviewMasterCode && item.reviewMasterCode.trim() !== '') ||
				(item.evalName && item.evalName.trim() !== '') ||
				(item.evalDetail && item.evalDetail.trim() !== '') ||
				(item.evalWeight && item.evalWeight !== '');

			return hasValue;
		});

		// 유효 데이터가 없을시 
		if (validList.length === 0) {
			tbody.innerHTML = `
			      <tr class="empty-row">
			        <td colspan="8" class="text-center text-muted py-3">
			          데이터가 없습니다. 추가해주세요.
			        </td>
			      </tr>
			    `;
			return;
		}

		// 유효 데이터가 있을 때만 테이블 렌더링
		tbody.innerHTML = '';

		validList.forEach((item, index) => {
			const fragment = template.content.cloneNode(true);
			const row = fragment.querySelector('tr');

			// 번호 1,2,3.. (index+1)
			const seqInput = row.querySelector('#evalNum');
			  if (seqInput) {
			    seqInput.value = index + 1;
			    seqInput.readOnly = true;  // 사용자 수정막기
			  }
			
			//row.querySelector('input[name="evalSeq"]').value = item.evalSeq ?? '';
			row.querySelector('input[name="evalName"]').value = item.evalName ?? '';
			row.querySelector('input[name="evalDetail"]').value = item.evalDetail ?? '';
			row.querySelector('input[name="evalWeight"]').value = item.evalWeight ?? '';

			tbody.appendChild(fragment);
		});

		updateWeightSum();
		renumberEvalSeq();
	}

	/* ------------------------------------------------------------------
	 * 번호 재정렬
	 * ------------------------------------------------------------------ */
	function renumberEvalSeq() {
	  const rows = document.querySelectorAll('#evalTbody tr:not(.empty-row)');

	  rows.forEach((tr, idx) => {
	    const no = idx + 1;

	    // 1) 화면 표시용 번호 (#evalNum)
	    const numInput = tr.querySelector('#evalNum');
	    if (numInput) {
	      numInput.value = no;
	      numInput.readOnly = true;
	      numInput.disabled = true;   // 표시용 번호는 서버에 보낼 필요 없음
	    }

	    // 2) 필요한 경우 실제 seq 값도 세팅 (Optional)
	    const seqHidden = tr.querySelector('input[name="evalSeq"]');
	    if (seqHidden) {
	      seqHidden.value = no;
	    }
	  });
	}
	
	/* ------------------------------------------------------------------
	 * 평가 항목 삭제 
	 * ------------------------------------------------------------------ */
	const evalTbody = document.querySelector('#evalTbody');

	if (evalTbody) {
	  evalTbody.addEventListener('click', (e) => {
	    // 삭제 버튼(.eval-del) 찾기
	    const delBtn = e.target.closest('.eval-del');
	    if (!delBtn) return; // 삭제 버튼을 누른 게 아니면 종료

	    // 실제 행(tr)
	    const row = delBtn.closest('tr');
	    if (!row) return;

	    // 행 삭제
	    row.remove();

	    // 삭제 후 행 번호 재정렬
	    renumberEvalSeq();

	    // 가중치 합계 재계산
	    updateWeightSum();

	    // 행이 0개면 placeholder 다시 넣기
	    const rows = evalTbody.querySelectorAll('tr');
	    if (rows.length === 0) {
	      evalTbody.innerHTML = `
	        <tr class="empty-row">
	          <td colspan="5" class="text-center text-muted py-3">
	            데이터가 없습니다. 추가해주세요.
	          </td>
	        </tr>
	      `;
	      document.querySelector('#weightSum').textContent = "0";
	    }
	  });
	}
	

	/* ------------------------------------------------------------------
	 * 가중치 자동 합계
	 * ------------------------------------------------------------------ */

	function updateWeightSum() {
		// 템플릿에서 name을 evalWeight로 통일한다고 가정
		const weightInputs = document.querySelectorAll('input[name="evalWeight"]');
		let sum = 0;

		weightInputs.forEach(input => {
			const value = Number(input.value);
			if (!isNaN(value)) sum += value;
		});

		const totalEl = document.querySelector('#weightSum');
		if (!totalEl) return;

		totalEl.textContent = sum;

		if (sum === 100) {
			totalEl.style.color = 'var(--base-color)'; // 정상
		} else if (sum > 100) {
			totalEl.style.color = 'red';              // 초과
			showToast('가중치는 100%를 맞춰주세요', 'error');
		} else {
			totalEl.style.color = 'orange';           // 부족
		}
	}

	function attachWeightEvents() {
		const tbody = document.querySelector('#evalTbody');
		if (!tbody) return;

		tbody.addEventListener('input', (e) => {
			if (e.target.name === 'evalWeight') {
				updateWeightSum();
			}
		});
	}

	attachWeightEvents();


	/* ------------------------------------------------------------------
	 * 초기화 (검색, 저장) 
	 * ------------------------------------------------------------------ */

	const btnReset = document.querySelector("#btnReset");   // 저장쪽 초기화 버튼
	const bteResetSearch = document.querySelector("#btnResetSearch"); //검색쪽 초기화 버튼

	if (btnReset) {
		btnReset.addEventListener("click", () => {

			// .form-allwrapper 아래의 input 모두 초기화
			const wrapper = document.querySelector(".form-allwrapper");
			if (!wrapper) return;

			// input 초기화 (라디오 빼고)
			wrapper.querySelectorAll("input").forEach((el) => {
				if (el.type === "radio") return;

				if (el.type === "checkbox") {
					el.checked = false;
				} else {
					el.value = "";
				}
			});

			// useYn 라디오 Y로 다시 체크
			const useYnY = wrapper.querySelector('input[name="useYn"][value="Y"]');
			if (useYnY) {
				useYnY.checked = true;
			}

			// 평가항목 테이블 초기화
			const evalTbody = document.querySelector("#evalTbody")
			evalTbody.innerHTML = `
						      <tr class="empty-row">
						        <td colspan="8" class="text-center text-muted py-3">
						          데이터가 없습니다. 추가해주세요.
						        </td>
						      </tr>
						    `;

			// 가중치
			const weightSumEl = document.querySelector("#weightSum");
			if (weightSumEl) {
				weightSumEl.textContent = "0";  // 숫자 0으로 초기화
				weightSumEl.style.color = "";   // 색상도 초기화하고 싶으면
			}

		})
	}

	if (bteResetSearch) {
		bteResetSearch.addEventListener("click", () => {

			document.querySelectorAll(".card-ui--search input")
				.forEach((el) => {
					if (el.type === "checkbox") el.checked = false;
					else el.value = "";
				});

				loadUserList();
		})
	}

	/* ------------------------------------------------------------------
	 * 검색
	 * ------------------------------------------------------------------ */

	const btnSearch = document.querySelector("#btnSearch");
	
	if(btnSearch){
		btnSearch.addEventListener("click", () => {
			const params = new URLSearchParams();
			
			// 템플릿명
			const name = document.querySelector("#reviewMasterNameSearch").value.trim();
			if(name) params.append('reviewMasterName', name);
			
			// 날짜 (시작일, 종료일)
			const start = document.querySelector("#reviewStartDate-search").value;
			const end = document.querySelector("#reviewEndDate-search").value;
			
			if(start) params.append('reviewStartDate', start);
			if(end) params.append('reviewEndDate', end);
			
			// 사용여부
			const checked = document.querySelectorAll('input[name="searchUseYn"]:checked');
			checked.forEach((chk) => {
			  params.append('useYnList', chk.value);  
			});
			
			fetch(`/api/review/search?${params.toString()}`)
				.then(res => res.json())
				.then(data => {
					grid.resetData(data);
					grid.refreshLayout();
				}).catch(err => console.error(err));
			
		});
	}
	
	

	
	/* ------------------------------------------------------------------
	 * 등록 / 수정 공통 처리
	 * ------------------------------------------------------------------ */
	const btnSave = document.querySelector("#btnSave");

	if (btnSave) {
	  btnSave.addEventListener("click", async () => {
	    const fr = document.querySelector(".form-allwrapper");

	    // 1) 기본정보
	    const reviewPayload = {
	      companyCode: "0000",
	      reviewMasterName: fr.querySelector("#reviewMasterName")?.value ?? "",
	      reviewStartDate: fr.querySelector("#reviewStartDate")?.value ?? "",
	      reviewEndDate: fr.querySelector("#reviewEndDate")?.value ?? "",
	      useYn: fr.querySelector('input[name="useYn"]:checked')?.value ?? "",
	      remark: fr.querySelector("#remark")?.value ?? "",
	    };

	    // 2) 평가항목
	    const evalItemList = [];
	    document
	      .querySelectorAll("#evalTbody tr:not(.empty-row)")
	      .forEach((tr) => {
	        const evalName = tr.querySelector('input[name="evalName"]')?.value ?? "";
	        const evalDetail = tr.querySelector('input[name="evalDetail"]')?.value ?? "";
	        const evalWeight = tr.querySelector('input[name="evalWeight"]')?.value ?? "";

	        if (evalName || evalDetail || evalWeight) {
	          evalItemList.push({
	            evalName,
	            evalDetail,
	            evalWeight,
	          });
	        }
	      });

	    // 3) 등록인지 수정인지 판단 (그리드에서 하나 선택해서 들어온 상태면 수정)
	    const isModify = !!(currentReviewDetail && currentReviewDetail.reviewMasterCode);

	    // 4) 최종 payload
	    const payload = {
	      ...reviewPayload,
	      evalItemList,
	    };

	    // 수정일 때는 reviewMasterCode를 함께 보냄
	    if (isModify) {
	      payload.reviewMasterCode = currentReviewDetail.reviewMasterCode;
	    }

	    try {
	      // 5) URL 분기 – 등록 vs 수정
	      const url = isModify
	        ? "/api/review/modifyMaster"
	        : "/api/review/registerMaster";

	      const res = await fetch(url, {
	        method: "POST",
	        headers: {
	          "Content-Type": "application/json;charset=UTF-8",
	        },
	        body: JSON.stringify(payload),
	      });

	      if (!res.ok) {
	        throw new Error("서버 오류");
	      }

	      const data = await res.json();

	      if (data.status === "success") {
	        showToast(
	          isModify
	            ? "인사평가 기준이 수정되었습니다."
	            : "인사평가 기준이 등록되었습니다.",
	          "success"
	        );

	        // 그리드 새로고침
	        await loadUserList();

	        // 폼 초기화 (등록/수정 후 새거 작성 모드로 돌릴지 유지할지는 취향)
	        btnReset?.click();

	        // 수정 끝났으니 상태 초기화
	        currentReviewDetail = null;

	      } else {
	        showToast(data.message || "저장 중 오류가 발생했습니다.", "error");
	      }
	    } catch (err) {
	      console.error(err);
	      showToast("저장 중 오류가 발생했습니다.", "error");
	    }
	  });
	}

/*	const btnSave = document.querySelector("#btnSave");

	if (btnSave) {
	  btnSave.addEventListener("click", async () => {
	    const fr = document.querySelector(".form-allwrapper");

	    // 1) 기본정보
	    const reviewPayload = {
	      companyCode: "0000",
	      reviewMasterName: fr.querySelector("#reviewMasterName")?.value ?? "",
	      reviewStartDate: fr.querySelector("#reviewStartDate")?.value ?? "",
	      reviewEndDate: fr.querySelector("#reviewEndDate")?.value ?? "",
	      useYn: fr.querySelector('input[name="useYn"]:checked')?.value ?? "",
	      remark: fr.querySelector("#remark")?.value ?? "",
	    };

	    // 2) 평가항목
	    const evalItemList = [];
	    document
	      .querySelectorAll("#evalTbody tr:not(.empty-row)")
	      .forEach((tr) => {
	        const evalName = tr.querySelector('input[name="evalName"]')?.value ?? "";
	        const evalDetail = tr.querySelector('input[name="evalDetail"]')?.value ?? "";
	        const evalWeight = tr.querySelector('input[name="evalWeight"]')?.value ?? "";

	        if (evalName || evalDetail || evalWeight) {
	          evalItemList.push({
	            evalName,
	            evalDetail,
	            evalWeight,
	          });
	        }
	      });

	    // 3) 최종 payload
	    const payload = {
	      ...reviewPayload,
	      evalItemList,
	    };


	    try {
	      const url = "/api/review/registerMaster"; 

	      const res = await fetch(url, {
	        method: "POST",
	        headers: {
	          "Content-Type": "application/json;charset=UTF-8",
	        },
	        body: JSON.stringify(payload),
	      });

	      if (!res.ok) {
	        throw new Error("서버 오류");
	      }

	      const data = await res.json();

	      if (data.status === "success") {
	        showToast("인사평가 기준이 등록되었습니다.", "success");
			loadUserList();
			btnReset?.click();

	      } else {
	        showToast(data.message || "등록 중 오류가 발생했습니다.", "error");
	      }
	    } catch (err) {
	      console.error(err);
	      showToast("등록 중 오류가 발생했습니다.", "error");
	    }
	  });
	}
*/
	
	
	/* ------------------------------------------------------------------
	 * 평가항목 번호 자동 세팅 (행 추가 감지)
	 * ------------------------------------------------------------------ */
	const evalTbodyEl = document.querySelector("#evalTbody");

	if (evalTbodyEl) {
	  const observer = new MutationObserver(() => {
	    renumberEvalSeq();   // tr 추가될 때마다 번호 자동 배치
	  });

	  observer.observe(evalTbodyEl, {
	    childList: true,     // 자식 변경 감지
	  });
	}
	
	
	
	
	
	


});



