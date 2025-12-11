/**
 * certificationManage.js
 */

document.addEventListener("DOMContentLoaded", async () => {


	
	/* ------------------------------------------------------------------
	 * 공통코드
	 * ------------------------------------------------------------------ */
	function getCmCodeOptions2(divId) {

		// 자동생성 : code=0A&code=0C&code=0D&code=0E
		const keys = Object.keys(divId);  // ['0A','0C','0D','0E']
		const param = keys.map(k => `code=${k}`).join('&');

		fetch(`/api/com/commonCodes?${param}`)
			.then(res => res.json())
			.then(list => {
				for (item in divId) {
					const select = document.querySelector(`.card-ui--search #${divId[item]}`);

					list[item].forEach(d => {
						const opt = document.createElement('option');
						opt.value = d.code;
						opt.textContent = d.codeName;
						select.appendChild(opt);
					});
				}
			}).catch(err => console.error(err));

	}
	
	const divId = { '0I': 'doc-search'  }
	getCmCodeOptions2(divId);

	
	
	/* ------------------------------------------------------------------
	 * 모달
	 * ------------------------------------------------------------------ */

	// 화면에 보이는 사원명 input
	const empNameInput     = document.getElementById("empName");
	// 실제 선택된 사원 ID (hidden)
	const selectedUserIdInput = document.getElementById("selectedUserId");  // ← 위에서 id 바꾼 것 기준

	// 사원명 input 클릭 시 모달 열기
	if (empNameInput) {
	  empNameInput.addEventListener("click", (e) => {
	    if (typeof openHrEmpModal === 'function') {
	      openHrEmpModal(e);
	    } else {
	      console.error("openHrEmpModal 함수가 로드되지 않았습니다.");
	    }
	  });
	}

	// 모달에서 row 선택 시 호출되는 콜백 (전역)
	window.handleSelectedEmp = function(row) {
	  if (selectedUserIdInput) {
	    selectedUserIdInput.value = row.userId;
	  }
	  if (empNameInput) {
	    empNameInput.value = row.userName;
	  }
	};



	
	
});