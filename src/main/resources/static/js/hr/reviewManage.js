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
				{ header: "인사평가명", name: "reviewMasterName", width: 700 },
				{ header: "시작일", name: "reviewStartDate", align: 'center' },
				{ header: "종료일", name: "reviewEndDate", align: 'center' },
				{ header: "상태", name: "reivewStatus", align: 'center', width: 150 },
				{
					header: "평가하기",
					name: "evalBtn",
					width: 150,
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
						// 값이 null/undefined/빈문자면 "미평가"로 표시
						if (!value || String(value).trim() === "") {
							return "미평가";
						}
						return value;
					}
				},
				{ header: "총점", name: "finalScore", align: 'right' }
			]
		});

		// 팀원 클릭시 사원정보+평가항목 조회
		teamGrid.on("click", (ev) => {
			const rowData = teamGrid.getRow(ev.rowKey);
			if (!rowData) return;

			fillDetailBasicInfo(rowData); // 사원명/부서/총점 채우기

			if (currentReviewMasterCode) {
				loadEvalItemList(currentReviewMasterCode);
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
			if (window.globalLoader) globalLoader.style.display = "flex";

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

		} finally {
			if (window.globalLoader) globalLoader.style.display = "none";
		}
	}


	// 선택한 템플릿의 팀원 목록 + 평가정보 조회
	async function loadTeamMemberList(reviewMasterCode) {
		try {
			if (window.globalLoader) globalLoader.style.display = "flex";

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
		} finally {
			if (window.globalLoader) globalLoader.style.display = "none";
		}
	}


	/* ------------------------------------------------------------------
	 * 화면 렌더링 (평가항목 테이블, 상세정보)
	 * ------------------------------------------------------------------ */
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
			const scoreSelect = rowEl.querySelector('select[name="evalScore"]');

			if (seqInput) seqInput.value = item.evalSeq ?? (index + 1);
			if (nameInput) nameInput.value = item.evalName ?? "";
			if (detailInput) detailInput.value = item.evalDetail ?? "";
			if (weightInput) weightInput.value = item.evalWeight ?? "";

			// 이미 저장된 점수 값이 있다면 여기서 채우고,
			// 아직 없다면 기본값("")으로 둬도 됩니다.
			if (scoreSelect && item.evalScore) {
				scoreSelect.value = item.evalScore;
			}

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
		const totalScoreInput = document.getElementById("totalScore");

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
			const grade = row.querySelector('select[name="evalScore"]').value;
			const weightInput = row.querySelector('input[name="evalWeight"]');
			const weight = weightInput ? parseFloat(weightInput.value || "0") : 0;

			if (grade && scoreMap[grade]) {
				const baseScore = scoreMap[grade]; // A~E -> 5~1 변환
				const weighted = baseScore * (weight / 100); // 가중치 계산
				total += weighted;

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
		const selects = document.querySelectorAll('#reviewTbody select[name="evalScore"]');

		selects.forEach(sel => {
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
				const scoreSelect = row.querySelector('select[name="evalScore"]');

				const grade = scoreSelect ? scoreSelect.value : '';

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
	if (btnDetailReset) {
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
	}

	// 검색
	if (btnResetSearch) {
		btnResetSearch.addEventListener("click", () => {

			document.querySelectorAll(".card-ui--search input")
				.forEach((el) => {
					if (el.type === "checkbox") el.checked = false;
					else el.value = "";
				})
			initReviewMasterGrid();
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





});