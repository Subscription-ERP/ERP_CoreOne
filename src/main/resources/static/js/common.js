/**
 * common.js
 */

/*document.addEventListener('DOMContentLoaded', () => {
  getDeptOptions();
  getCmCodeOptions();
});*/


/* ------------------------------------------------------------------
 * 부서조회
 * ------------------------------------------------------------------ */
function getDeptOptions() {
	fetch('/api/hr/getDeptName')
		.then(res => res.json())
		.then(list => {
			const select = document.querySelector(".form-grid #dept");

			let line = "";

			list.forEach(d => {
				if (line != d.deptCode.charAt(1)) {
					const opt = document.createElement('option');
					opt.textContent = " ";
					select.appendChild(opt);
				}
				line = d.deptCode.charAt(1)
				const opt = document.createElement('option');
				opt.value = d.deptCode;
				opt.textContent = d.deptName;
				select.appendChild(opt);
			});
		}).catch(err => console.error(err));
}


/* ------------------------------------------------------------------
 * 부서조회 (여러 select 동시 세팅 가능 버전)
 * ------------------------------------------------------------------ */
function getDeptOptions2(selectors = ["#dept"]) {
	// 매개변수를 배열/문자열 모두 받게 처리
	const targetSelectors = Array.isArray(selectors) ? selectors : [selectors];

	fetch('/api/hr/getDeptName')
		.then(res => res.json())
		.then(list => {
			// 각 셀렉터마다 동일한 옵션 세팅
			targetSelectors.forEach(sel => {
				const select = document.querySelector(sel);
				if (!select) return;   // 해당 요소 없으면 패스

				// 기존 옵션 비우고 기본값 하나 넣고 시작 (원하면 수정)
				select.innerHTML = "";
				// 검색용이라면 "전체" 같은 옵션을 넣고 싶으면 여기서 조건 분기 가능
				const defaultOpt = document.createElement('option');
				defaultOpt.value = "";
				defaultOpt.textContent = "전체";
				select.appendChild(defaultOpt);

				let line = "";

				list.forEach(d => {
					if (line != d.deptCode.charAt(1)) {
						const opt = document.createElement('option');
						opt.textContent = " ";
						select.appendChild(opt);
					}
					line = d.deptCode.charAt(1);

					const opt = document.createElement('option');
					opt.value = d.deptCode;
					opt.textContent = d.deptName;
					select.appendChild(opt);
				});
			});
		})
		.catch(err => console.error(err));
}




/* ------------------------------------------------------------------
 * 공통코드
 * ------------------------------------------------------------------ */

function getCmCodeOptions(divId) {

	// 자동생성 : code=0A&code=0C&code=0D&code=0E
	const keys = Object.keys(divId);  // ['0A','0C','0D','0E']
	const param = keys.map(k => `code=${k}`).join('&');

	fetch(`/api/com/commonCodes?${param}`)
		.then(res => res.json())
		.then(list => {
			for (item in divId) {
				const select = document.querySelector(`.form-grid #${divId[item]}`);

				list[item].forEach(d => {
					const opt = document.createElement('option');
					opt.value = d.code;
					opt.textContent = d.codeName;
					select.appendChild(opt);
				});
			}
		}).catch(err => console.error(err));

}



/* ------------------------------------------------------------------
 * 토스트 알람
 * ------------------------------------------------------------------ */
let rcToastTimer = null;

function showToast(message, type = 'info') {
	const container = document.getElementById('toastContainer');
	if (!container) return;

	container.className = 'rc-toast';
	container.innerHTML = '';

	container.classList.add(`rc-toast--${type}`);

	const icons = {
		success: '✔',
		error: '✖',
		warning: '⚠',
		info: 'ℹ'
	};

	container.innerHTML = `
    <div class="rc-toast__icon">${icons[type]}</div>
    <div class="rc-toast__message">${message}</div>
  `;

	container.classList.add('rc-toast--show');

	if (rcToastTimer) clearTimeout(rcToastTimer);
	rcToastTimer = setTimeout(() => {
		container.classList.remove('rc-toast--show');
	}, 2500);
}


/* ------------------------------------------------------------------
 * input 클릭시 달력 선택창 뜨게 하기
   사용방법 : setupNativeDatePicker('input을 감싸는 부모 id값', 'input id값')
   예시 : setupNativeDatePicker('hireDateWrapper', 'hireDateInput');
 * ------------------------------------------------------------------ */

function setupNativeDatePicker(wrapperId, inputId) {
	const wrapper = document.getElementById(wrapperId);
	const input = document.getElementById(inputId);
	if (!wrapper || !input) return;

	// wrapper 아무 곳이나 클릭해도 달력 뜨게
	wrapper.addEventListener('click', () => {
		
	    if (input.showPicker) {
	      try {
	        input.showPicker();      // 크롬/엣지에서 달력 팝업
	      } catch (e) {
	        // 혹시나 또 막히면 조용히 무시하고 포커스만 줌
	        input.focus();
	      }
	    } else {
	      input.focus();             // 지원 안 하는 브라우저용
	    }
	  });

	// input이 포커스를 얻었을 때도 자동으로 달력 띄우기 (원하면)
/*	input.addEventListener('focus', () => {
		if (input.showPicker) {
			input.showPicker();
		}
	});*/
}



/* ------------------------------------------------------------------
 * 테이블 (추가 동적처리)
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



/* ------------------------------------------------------------------
 * 시작일 > 종료일 선택 못하게 막기 
 * ------------------------------------------------------------------ */

// 날짜 범위 검증 함수
function validateDateRangeByIds(startId, endId, opts = {}){
	const {
		message = "종료일이 시작일보다 빠를 수 없습니다.",
		clearOnInvalid = true,
	} = opts;
	
	const startEl = document.querySelector(`#${startId}`);
	const endEl = document.querySelector(`#${endId}`);
	if(!startEl || !endEl) return true;
	
	const startVal = startEl.value;
	const endVal = endEl.value;
	
	// 둘다 값이 있어야할 때 비교
	if(!startVal || !endVal) return true;
	
	const startDate = new Date(startVal);
	const endDate = new Date(endVal);
	
	if(endDate < startDate){
		showToast(message, "warning");
		if(clearOnInvalid){
			startEl.value = "";
			endEl.value = "";
		}
		return false;
	}
	return true;
	
}

// 이벤트 연결
function bindDateRangeValidation(startId, endId, opts){
	const startEl = document.querySelector(`#${startId}`);
	const endEl = document.querySelector(`#${endId}`);
	if(!startEl || !endEl) return;
	
	const handler = () => validateDateRangeByIds(startId, endId, opts);
	
	startEl.addEventListener("change", handler);
	endEl.addEventListener("change", handler);
}



