/**
 * reviewManage.js (인사평가관리)
 */

document.addEventListener("DOMContentLoaded", () => {


	/* ------------------------------------------------------------------
	 * 전역 변수
	 * ------------------------------------------------------------------ */
	let reviewGrid;              // 인사평가 목록 
	let teamGrid;                // 팀원 목록
	let currentReviewMasterCode = null;   // 현재 선택된 평가 템플릿 코드


	/* ------------------------------------------------------------------
	 * 초기 실행
	 * ------------------------------------------------------------------ */
	initReviewMasterGrid();  // 상단 그리드
	initTeamGrid();          // 하단 그리드
	loadReviewMasterList();  // 인사평가 목록 조회


	/* ------------------------------------------------------------------
	 * 그리드 초기화
	 * ------------------------------------------------------------------ */
	// 인사평가 목록
	function initReviewMasterGrid() {
		reviewGrid = new tui.Grid({
			el: document.getElementById('reviewList'),
			bodyHeight: 'fitToParent',
			scrollX: false,
			scrollY: true,
			rowHeaders: ['rowNum'],
			columns: [
				{ header: "코드", name: "reviewMasterCode", hidden: true },
				{ header: "인사평가명", name: "reviewMasterName", width: 300 },
				{ header: "시작일", name: "reviewStartDate", align: 'center' },
				{ header: "종료일", name: "reviewEndDate", align: 'center' },
				{ header: "상태", name: "reivewStatus", align: 'center' },
				{
					header: "평가하기",
					name: "evalBtn",
					align: "center",
					formatter: () =>
						'<a class="grid-eval-btn" style="text-decoration: underline; cursor: pointer;">평가</a>'
				}
			]
		});

		// 셀 클릭 이벤트: "평가" 버튼만 동작
		reviewGrid.on("click", (ev) => {
			const target = ev.nativeEvent.target;

			// 버튼(.grid-eval-btn)이 아니면 무시
			if (!target.classList.contains("grid-eval-btn")) {
				return;
			}

			const rowData = reviewGrid.getRow(ev.rowKey);
			if (!rowData || !rowData.reviewMasterCode) return;

			// 현재 선택된 템플릿
			currentReviewMasterCode = rowData.reviewMasterCode;

			// 제목
			const title = document.querySelector("#evalTitle");
			if (title) {
				title.style.display = "block";
				title.textContent = rowData.reviewMasterName;
			}

			// 선택한 템플릿 코드로 팀원 목록 조회
			loadTeamMemberList(currentReviewMasterCode);

			// 하단 상세 영역 초기화
			renderEvalItems([]);
			fillDetailBasicInfo({
				targetUserName: "",
				targetDeptName: "",
				finalScore: ""
			});
		});
	}


	// 팀원 목록 
	function initTeamGrid() {
		teamGrid = new tui.Grid({
			el: document.getElementById("evalListGrid"),
			bodyHeight: 'fitToParent',
			scrollX: false,
			scrollY: true,
			rowHeaders: ["rowNum"],
			columns: [
				{ header: "사원번호", name: "targetUserId", align: 'center' },
				{ header: "사원명", name: "targetUserName" },
				{ header: "부서", name: "targetDept", hidden: true },
				{ header: "부서명", name: "targetDeptName", align: 'center' },
				{ header: "직위/직급", name: "targetJobTitle", hidden: true },
				{ header: "직위/직급명", name: "targetJobTitleName", align: 'center' },
				{
					header: "평가상태",
					name: "reviewStatus",
					align: "center",
					formatter: ({ value }) => {
						const v = String(value ?? "").trim();

						if (v === "" || v === "0") return "미평가";
						if (v === "1") return "진행중";
						if (v === "2") return "완료";

						return v;
					}
				},
				{ header: "총점", name: "finalScore", align: 'center' }
			]
		});

		// 팀원 클릭시 사원정보+평가항목 조회
		teamGrid.on("click", async (ev) =>  {
			const rowData = teamGrid.getRow(ev.rowKey);
			if (!rowData) return;

			// 기본정보 채우기
			fillDetailBasicInfo(rowData); // 사원명/부서/총점 채우기

			// 템플릿 항목 그리기
			if (currentReviewMasterCode) {
				await loadEvalItemList(currentReviewMasterCode);
			}

			// 3) 저장된 결과가 있는 경우에만 서버에서 가져와 매핑
			  if (rowData.reviewCode) {
			    try {

			      const url =
			        `/api/review/manage/detail?` +
			        `reviewCode=${encodeURIComponent(rowData.reviewCode)}` +
			        `&targetUserId=${encodeURIComponent(rowData.targetUserId)}`;

			      const res = await fetch(url);   // ← await
			      if (!res.ok) {
			        throw new Error("HTTP " + res.status);
			      }

			      const reviewData = await res.json();  // ← await

			      applyReviewResultToForm(reviewData);

			    } catch (err) {
			      console.error(err);
			      showToast("저장된 인사평가를 불러오는 중 오류가 발생했습니다.", "error");
			    } 
			  } else {
			    // 미평가일 때 폼 초기화
			    const finalScoreInput = document.querySelector("#finalScore");
			    const commentInput = document.querySelector("#evalComment");
			    if (finalScoreInput) finalScoreInput.value = "";
			    if (commentInput) commentInput.value = "";
				document.querySelectorAll('#reviewTbody .score-radio input[type="radio"]').forEach(r => (r.checked = false));
			  }


		});

	}


	/* ------------------------------------------------------------------
	 * API 호출(목록/팀원/평가항목)
	 * ------------------------------------------------------------------ */
	// 인사평가 목록 + 상태조회
	async function loadReviewMasterList() {
		try {
			if (window.globalLoader) globalLoader.style.display = "flex";
			
			const response = await fetch("/api/review/manage/status");
			if (!response.ok) {
				throw new Error("HTTP " + response.status);
			}

			const data = await response.json();
			reviewGrid.resetData(data);

		} catch (err) {
			console.error(err);
			if (window.showToast) {
				showToast("인사평가 목록을 불러오지 못했습니다.", "error");
			}
		} finally {
			if (window.globalLoader) globalLoader.style.display = "none";
		}
	}

	// 선택한 템플릿 평가항목 조회
	async function loadEvalItemList(reviewMasterCode) {
		try {

			const url = `/api/review/manage/evalItem?reviewMasterCode=${encodeURIComponent(reviewMasterCode)}`;
			const response = await fetch(url);
			if (!response.ok) {
				throw new Error("HTTP " + response.status);
			}

			const data = await response.json();

			// 조회된 리스트 테이블에 렌더링
			renderEvalItems(data);

		} catch (err) {
			console.error(err);
			if (window.showToast) {
				showToast("평가항목을 불러오지 못했습니다.", "error");
			}

			// 에러 시도 빈 테이블로 초기화
			renderEvalItems([]);

		} 
	}


	// 선택한 템플릿의 팀원 목록 + 평가정보 조회
	async function loadTeamMemberList(reviewMasterCode) {
		try {

			const url =
				`/api/review/manage/teamMember` +
				`?reviewMasterCode=${encodeURIComponent(reviewMasterCode)}`;

			const response = await fetch(url);
			if (!response.ok) {
				throw new Error("HTTP " + response.status);
			}

			const data = await response.json();
			teamGrid.resetData(data);

		} catch (err) {
			console.error(err);
			if (window.showToast) {
				showToast("팀원 평가 목록을 불러오지 못했습니다.", "error");
			}
			teamGrid.resetData([]);
		} 
	}


	/* ------------------------------------------------------------------
	 * 화면 렌더링 (평가항목 테이블, 상세정보)
	 * ------------------------------------------------------------------ */
	// 라디오 평가점수 헬퍼함수 : 라디오 그룹 name 세팅 (행마다 유니크)
	function bindScoreRadioGroupName(rowEl, groupKey) {
	  const radios = rowEl.querySelectorAll('.score-radio input[type="radio"]');
	  radios.forEach(r => (r.name = `evalScore_${groupKey}`));
	}

	// 라디오 평가점수 헬퍼함수 : 저장된 점수(A~E)를 라디오에 반영
	function setScoreRadioValue(rowEl, grade) {
	  if (!grade) return;
	  const radio = rowEl.querySelector(`.score-radio input[type="radio"][value="${grade}"]`);
	  if (radio) radio.checked = true;
	}
		
	// 평가항목 테이블 렌더링
	function renderEvalItems(list) {
		const tbody = document.getElementById("reviewTbody");
		const countEl = document.getElementById("reviewCount");
		const template = document.getElementById("reviewRowTemplate");

		if (!tbody || !template) return;

		// 기존 행 비우기
		tbody.innerHTML = "";

		// 데이터 없을 때
		if (!list || list.length === 0) {
			const tr = document.createElement("tr");
			tr.classList.add("empty-row");
			tr.innerHTML = `
				  <td colspan="5" class="text-center text-muted py-3">
					데이터가 없습니다.
				  </td>`;
			tbody.appendChild(tr);

			if (countEl) countEl.textContent = "0";
			return;
		}

		// 데이터 있을 때: 템플릿으로 행 생성
		list.forEach((item, index) => {
			const rowEl = template.content.firstElementChild.cloneNode(true);

			const seqInput = rowEl.querySelector('input[name="evalSeq"]');
			const nameInput = rowEl.querySelector('input[name="evalName"]');
			const detailInput = rowEl.querySelector('input[name="evalDetail"]');
			const weightInput = rowEl.querySelector('input[name="evalWeight"]');
			//const scoreSelect = rowEl.querySelector('select[name="evalScore"]');

			if (seqInput) seqInput.value = item.evalSeq ?? (index + 1);
			if (nameInput) nameInput.value = item.evalName ?? "";
			if (detailInput) detailInput.value = item.evalDetail ?? "";
			if (weightInput) weightInput.value = item.evalWeight ?? "";

			const groupKey = seqInput?.value || (index + 1);
			bindScoreRadioGroupName(rowEl, groupKey);
			
			setScoreRadioValue(rowEl, item.evalScore);

			tbody.appendChild(rowEl);
		});

		// 뱃지 숫자 업데이트
		if (countEl) {
			const rowCount = tbody.querySelectorAll("tr:not(.empty-row)").length;
			countEl.textContent = rowCount;
		}

		bindScoreChangeEvents(); // 점수 change 이벤트 바인딩
	}


	// 상세영역 : 사원 기본정보 채우기
	function fillDetailBasicInfo(rowData) {
		const userNameInput = document.getElementById("userName");
		const deptNameInput = document.getElementById("deptName");
		const totalScoreInput = document.getElementById("finalScore");

		if (userNameInput) userNameInput.value = rowData.targetUserName || "";
		if (deptNameInput) deptNameInput.value = rowData.targetDeptName || "";
		if (totalScoreInput) totalScoreInput.value = rowData.finalScore != null ? rowData.finalScore : "";

	}


	/* ------------------------------------------------------------------
	 * 총점 계산 & 등급 변환
	 * ------------------------------------------------------------------ */
	// A~E 점수 변환
	const scoreMap = {
		"A": 5,
		"B": 4,
		"C": 3,
		"D": 2,
		"E": 1
	};

	// 총점 계산 함수
	function calculateFinalScore() {
		let total = 0;

		// 모든 평가행을 순회
		const rows = document.querySelectorAll("#reviewTbody tr:not(.empty-row)");

		rows.forEach(row => {
			const checked = row.querySelector('.score-radio input[type="radio"]:checked');
			const grade = checked ? checked.value : "";
			const weightInput = row.querySelector('input[name="evalWeight"]');
			const weight = weightInput ? parseFloat(weightInput.value || "0") : 0;

			if(grade && scoreMap[grade]){
				total += scoreMap[grade] * (weight / 100);
			}

		});

		return total;

	}

	// 최종점수 -> A~E 등급 변환
	function convertFinalToGrade(score) {
		if (score >= 4.5) return "A";
		if (score >= 3.5) return "B";
		if (score >= 2.5) return "C";
		if (score >= 1.5) return "D";
		return "E";
	}


	// 점수 변화 감지하여 자동 업데이트(input #finalScore)
	function bindScoreChangeEvents() {
		const radios = document.querySelectorAll('#reviewTbody .score-radio input[type="radio"]');

		radios.forEach(sel => {
			sel.addEventListener("change", () => {
				const total = calculateFinalScore();
				const finalGrade = convertFinalToGrade(total);

				// 화면에 반영
				document.querySelector("#finalScore").value = finalGrade;

			})
		})
	}


	/* ------------------------------------------------------------------
	 * ai 코멘트
	 * ------------------------------------------------------------------ */
	document.querySelector("#btnAiComment")
		.addEventListener("click", async () => {

			const member = getSelectedMemberInfo();
			if (!member) return;

			const rows = document.querySelectorAll('#reviewTbody tr:not(.empty-row)');
			const items = [];

			rows.forEach(row => {
				const seqInput = row.querySelector('input[name="evalSeq"]');
				const nameInput = row.querySelector('input[name="evalName"]');
				const detailInput = row.querySelector('input[name="evalDetail"]');
				const weightInput = row.querySelector('input[name="evalWeight"]');
				const checked = row.querySelector('.score-radio input[type="radio"]:checked');
				
				const grade = checked ? checked.value : '';

				// 점수 선택된 항목만 AI로 보냄
				if (grade) {
					items.push({
						itemNo: seqInput ? Number(seqInput.value || '0') : null,
						itemName: nameInput ? nameInput.value || '' : '',
						description: detailInput ? detailInput.value || '' : '',
						weight: weightInput ? Number(weightInput.value || '0') : 0,
						grade: grade
					});
				}
			});

			if (items.length === 0) {
				showToast('점수가 입력된 항목이 없습니다.', 'warning');
				return;
			}


			const requestBody = {
				userName: member.userName,
				jobTitle: member.jobTitle,
				items: items
			};

			try {
				if (window.globalLoader) globalLoader.style.display = "flex";
				const res = await fetch('/api/review/ai-comment', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(requestBody)
				});

				if (!res.ok) {
					throw new Error('AI 코멘트 생성 호출 실패');
				}

				const commentText = await res.text();  // 컨트롤러가 String 리턴하니까

				document.getElementById('evalComment').value = commentText;

			} catch (e) {
				console.error(e);
				showToast('AI 코멘트 생성 중 오류가 발생했습니다.', 'error');
			} finally {
				if (window.globalLoader) globalLoader.style.display = "none";
			}

		});

	// 포커스된 사원 정보 가져오기
	function getSelectedMemberInfo() {
		const focused = teamGrid.getFocusedCell();   // 현재 포커스된 셀
		if (!focused) {
			showToast('AI 코멘트를 만들 팀원을 먼저 선택해 주세요.', 'warning');
			return null;
		}

		const row = teamGrid.getRow(focused.rowKey);
		if (!row) {
			showToast('선택된 팀원 정보를 찾을 수 없습니다.', 'error');
			return null;
		}

		return {
			userId: row.targetUserId,
			reviewCode: row.reviewCode || null,     
			userName: row.targetUserName,
			jobTitle: row.targetJobTitleName,   // 직위/직급명
			deptName: row.targetDeptName
		};
	}

	
	
	
	/* ------------------------------------------------------------------
	 * 초기화 (검색, 저장)
	 * ------------------------------------------------------------------ */
	const btnDetailReset = document.querySelector("#btnDetailReset");
	const btnResetSearch = document.querySelector("#btnResetSearch");

	// 저장
/*	if (btnDetailReset) {
		btnDetailReset.addEventListener("click", () => {

			// .form-allwrapper 아래의 input 모두 초기화
			const wrapper = document.querySelector(".form-allwrapper");
			if (!wrapper) return;

			wrapper.querySelectorAll("input").forEach((el) => {
				el.value = "";
			})

			// 평가항목 점수 초기화
			const tbody = document.getElementById("reviewTbody");
			tbody.innerHTML = `
			  <tr class="empty-row">
			    <td colspan="5" class="text-center text-muted py-3">
			      데이터가 없습니다.
			    </td>
			  </tr>
			`;
			const count = document.querySelector("#reviewCount");
			if (count) {
				count.textContent = "0";
			}



		})
	}*/

	// 팀원목록 초기화 함수	
	function resetRightPanel() {
	  // 1) 현재 선택된 템플릿 초기화
	  currentReviewMasterCode = null;

	  // 2) 팀원 목록 그리드 비우기
	  if (teamGrid) {
	    teamGrid.resetData([]);
	    teamGrid.blur?.();
	    teamGrid.refreshLayout?.();
	  }

	  // 3) 우측 제목 숨김/초기화
	  const title = document.querySelector("#evalTitle");
	  if (title) {
	    title.textContent = "";
	    title.style.display = "none";
	  }

	  // 4) 평가항목 테이블 비우기 + 카운트 0
	  renderEvalItems([]); 

	  // 5) 상세 입력폼 초기화
	  fillDetailBasicInfo({
	    targetUserName: "",
	    targetDeptName: "",
	    finalScore: ""
	  });

	  const commentInput = document.querySelector("#evalComment");
	  if (commentInput) commentInput.value = "";
	}
	
	
	// 검색
	if (btnResetSearch) {
		btnResetSearch.addEventListener("click", () => {

			document.querySelectorAll(".card-ui--search input")
				.forEach((el) => {
					if (el.type === "checkbox") el.checked = false;
					else el.value = "";
				})
			loadReviewMasterList();
			resetRightPanel();
			
		})
	}


	/* ------------------------------------------------------------------
	 * 검색
	 * ------------------------------------------------------------------ */
	const btnSearch = document.querySelector("#btnSearch");

	if (btnSearch) {
		btnSearch.addEventListener("click", () => {
			const params = new URLSearchParams();

			// 템플릿명
			const name = document.querySelector("#reviewMasterNameSearch").value.trim();
			if (name) params.append('reviewMasterName', name);

			fetch(`/api/review/manage/search?${params.toString()}`)
				.then(res => res.json())
				.then(data => {
					reviewGrid.resetData(data);
					reviewGrid.refreshLayout();
				}).catch(err => console.error(err));

		});
	}

	/* ------------------------------------------------------------------
	 * 저장(등록/수정) 
	 * ------------------------------------------------------------------ */

	const btnDetailSave = document.querySelector("#btnDetailSave");

	if (btnDetailSave) {
		btnDetailSave.addEventListener("click", async () => {

			// 선택 템플릿 확인
			if (!currentReviewMasterCode) {
				showToast("먼저 인사평가 템플릿을 선택해 주세요.", 'warning');
				return;
			}

			// 현재 선택된 팀원 정보 가져오기
			const member = getSelectedMemberInfo();
			if (!member) {
				return;
			}

			// 평가항목 점수 수집 + 유효성검사(전체항목 선택 필수)
			const rows = document.querySelectorAll("#reviewTbody tr:not(.empty-row)");  
			const hrReviewResultList = [];

			if(!rows || rows.length === 0){
				showToast("평가항목이 없습니다.", "warning");
				return;
			}
			
			// 하나라도 점수가 비면 저장 막기
			let missingCount = 0;
			
			rows.forEach((row) => {
			  const seqInput = row.querySelector('input[name="evalSeq"]');
			  const checked = row.querySelector('.score-radio input[type="radio"]:checked');

			  if (!checked) {
			    missingCount++;
			    return;
			  }

			  hrReviewResultList.push({
				evalSeq: Number(seqInput?.value || 0),
			    evalItemScore: checked.value                              // A~E
			  });
			});

			if (missingCount > 0) {
			  showToast(`평가 점수가 선택되지 않은 항목이 ${missingCount}개 있습니다. 모든 항목을 선택해 주세요.`, "warning");
			  return;
			}

			// 최종점수,코멘트
			const finalScoreInput = document.querySelector("#finalScore");
			const commentInput = document.querySelector("#evalComment");

			// 등록인지 수정인지 판단
			const isModify = !!member.reviewCode;
			
			const payload = {
			    reviewMasterCode: currentReviewMasterCode,
			    targetUserId: member.userId,
			    reviewCode: member.reviewCode || null,
			    finalScore: finalScoreInput ? finalScoreInput.value : "",
			    reviewComment: commentInput ? commentInput.value : "",
			    hrReviewResultList: hrReviewResultList
			  };

			
			// 서버전송
			try {
				if (window.globalLoader) globalLoader.style.display = "flex";

				const url = isModify ? "/api/review/manage/modify" : "/api/review/manage/register";
								
				const res = await fetch(url, {
					method: "POST",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify(payload)
				});

				if (!res.ok) {
					throw new Error("HTTP " + res.status);
				}

				const result = await res.json?.() ?? null;
				showToast(isModify ? "인사평가가 수정되었습니다." : "인사평가가 등록되었습니다.", "success");

				// 목록 로딩 및 초기화
				loadTeamMemberList(currentReviewMasterCode);
				btnDetailReset?.click();
				const evalComment = document.querySelector('#evalComment');
				if (evalComment) {
					evalComment.value = "";
				}
				loadReviewMasterList();

			} catch (err) {
				console.error(err);
				showToast("인사평가 저장 중 오류가 발생했습니다.", "error");
			} finally {
				if (window.globalLoader) globalLoader.style.display = "none";
			}


		})
	}


	/* ------------------------------------------------------------------
	 * 저장된 평가결과를 화면에 반영
	 * ------------------------------------------------------------------ */
	function applyReviewResultToForm(reviewData) {
		if (!reviewData) return;

		// 1) 최종등급, 코멘트 세팅
		const finalScoreInput = document.querySelector("#finalScore");
		const commentInput = document.querySelector("#evalComment");

		if (finalScoreInput) {
			finalScoreInput.value = reviewData.finalScore ?? "";
		}
		if (commentInput) {
			commentInput.value = reviewData.reviewComment ?? "";
		}

		
		// 2) 결과를 evalSeq 기준 Map으로 만들기
		const resultList = reviewData.hrReviewResultList || [];
		const scoreBySeq = new Map();
		  resultList.forEach(r => {
		    if (r.evalSeq != null) scoreBySeq.set(String(r.evalSeq), r.evalItemScore);
		  });
		
		// 화면의 각 row(evalSeq)와 매칭해서 체크
		const rows = document.querySelectorAll("#reviewTbody tr:not(.empty-row)");
		rows.forEach(row => {
		    const seqInput = row.querySelector('input[name="evalSeq"]');
		    const seq = seqInput ? String(seqInput.value) : null;
		    if (!seq) return;

		    // 기존 체크 해제(안 하면 예전 값 남는 경우가 있음)
		    row.querySelectorAll('.score-radio input[type="radio"]').forEach(r => (r.checked = false));

		    const grade = scoreBySeq.get(seq);
		    if (grade) setScoreRadioValue(row, grade);
		  });
	}
	






});