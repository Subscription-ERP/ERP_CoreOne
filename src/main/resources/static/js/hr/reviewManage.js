/**
 * reviewManage.js (인사평가관리)
 */

/*document.addEventListener("DOMContentLoaded", async () => {
	
		
	 ------------------------------------------------------------------
	 * 인사평가 기준관리 - Toast UI Grid 생성
	 * ------------------------------------------------------------------ 
	let ReviewGrid;     // 인사평가 목록
	let teamGrid;       // 팀원 목록

	function initReviewMasterGrid() {
		ReviewGrid = new tui.Grid({
			el: document.getElementById('reviewList'),
			bodyHeight: 'fitToParent',
			scrollX: false,
			scrollY: true,
			rowHeaders: ['rowNum'],
			columns: [
				{ header: "코드", name: "reviewMasterCode", hidden: true },
				{ header: "인사평가명", name: "reviewMasterName" },
				{ header: "시작일", name: "reviewStartDate" },
				{ header: "종료일", name: "reviewEndDate" },
				{ header: "상태", name: "reivewStatus" },
				{
				  header: '평가하기',
				  name: 'evalBtn',
				  width: 100,
				  align: 'center',
				  formatter: () => {
				    // 셀 안에 버튼 HTML을 그대로 렌더링
				    return '<button type="button" class="btn btn-white btn-sm grid-eval-btn">평가</button>';
				  }
				}
			]
		});
	}

	
	 ------------------------------------------------------------------
	 * 인사평가 기준관리 다건조회
	 * ------------------------------------------------------------------ 
	async function loadReviewMasterList(){
		//globalLoader.style.display = "flex";
		const response = await fetch('/api/review/manage/status');
		const data = await response.json();
	    ReviewGrid.resetData(data);
		ReviewGrid.refreshLayout();
		globalLoader.style.display = "none";
	}
		
	initReviewMasterGrid();
	loadReviewMasterList();
	
	
	 ------------------------------------------------------------------
	 * 인사평가관리 - Toast UI Grid 생성
	 * ------------------------------------------------------------------ 
	
	
	
	
	
	
	
	
	
	 ------------------------------------------------------------------
	 * 인사평가관리 팀원목록
	 * ------------------------------------------------------------------ 
	
	
	
});*/

/**
 * reviewManage.js (인사평가관리)
 */

document.addEventListener("DOMContentLoaded", () => {

  /* ------------------------------------------------------------------
   * 0. 그리드 인스턴스 변수
   * ------------------------------------------------------------------ */
  let reviewGrid; // 인사평가 목록 (위)
  let teamGrid;   // 팀원 목록 (아래)

  /* ------------------------------------------------------------------
   * 1. 인사평가 목록 Grid (위)
   * ------------------------------------------------------------------ */
  function initReviewMasterGrid() {
    reviewGrid = new tui.Grid({
      el: document.getElementById('reviewList'),
      bodyHeight: 'fitToParent',
      scrollX: false,
      scrollY: true,
      rowHeaders: ['rowNum'],
      columns: [
        { header: "코드",       name: "reviewMasterCode", hidden: true },
        { header: "인사평가명", name: "reviewMasterName" },
        { header: "시작일",     name: "reviewStartDate" },
        { header: "종료일",     name: "reviewEndDate" },
        { header: "상태",       name: "reivewStatus" },
        {
          header: "평가하기",
          name: "evalBtn",
          width: 100,
          align: "center",
          formatter: () =>
            '<button type="button" class="btn btn-white btn-sm grid-eval-btn">평가</button>'
        }
      ]
    });

    // 👉 셀 클릭 이벤트: "평가" 버튼만 동작
    reviewGrid.on("click", (ev) => {
      const target = ev.nativeEvent.target;

      // 버튼(.grid-eval-btn)이 아니면 무시
      if (!target.classList.contains("grid-eval-btn")) {
        return;
      }

      const rowData = reviewGrid.getRow(ev.rowKey);
      if (!rowData || !rowData.reviewMasterCode) return;

      const reviewMasterCode = rowData.reviewMasterCode;

      // 🔥 선택한 템플릿 코드로 팀원 목록 조회
      loadTeamMemberList(reviewMasterCode);

      // (선택) 상세 영역 제목 바꾸기
      const titleEl = document.querySelector(
        ".card-ui--detail .card-ui__header h3"
      );
      if (titleEl && rowData.reviewMasterName) {
        titleEl.textContent = rowData.reviewMasterName;
      }
    });
  }

  /* ------------------------------------------------------------------
   * 2. 팀원 목록 Grid (아래)
   * ------------------------------------------------------------------ */
  function initTeamGrid() {
    teamGrid = new tui.Grid({
      el: document.getElementById("evalListGrid"),
      bodyHeight: 260,
      scrollX: false,
      scrollY: true,
      rowHeaders: ["rowNum"],
      columns: [
        { header: "사원번호",   name: "targetUserId" },
        { header: "사원명",     name: "targetUserName" },
        { header: "부서",       name: "targetDept" },
        { header: "직위/직급", name: "targetJobTitle" },
        // { header: "직책",       name: "targetPosition" },
        // { header: "평가상태",   name: "reviewStatus" },
        { header: "총점",       name: "finalScore" }
      ]
    });
  }

  /* ------------------------------------------------------------------
   * 3. 인사평가 목록 + 상태 조회
   *    GET /api/review/manage/status
   * ------------------------------------------------------------------ */
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

  /* ------------------------------------------------------------------
   * 4. 선택한 템플릿의 팀원 목록 + 평가정보 조회
   *    GET /api/review/manage/team-members?reviewMasterCode=xxx
   * ------------------------------------------------------------------ */
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
   * 5. 초기 실행
   * ------------------------------------------------------------------ */
  initReviewMasterGrid(); // 상단 그리드
  initTeamGrid();         // 하단 그리드
  loadReviewMasterList(); // 인사평가 목록 조회

});