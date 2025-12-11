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
	
	

	  // ================= 사원 선택 모달 연동 =================
	  const empNameInput = document.getElementById("empName");
	  const empIdInput   = document.getElementById("empId");

	  if (empNameInput) {
	    empNameInput.addEventListener("click", (e) => {
	      // hrEmpModal.js 안의 openHrEmpModal 호출
	      if (typeof openHrEmpModal === 'function') {
	        openHrEmpModal(e);
	      } else {
	        console.error("openHrEmpModal 함수가 로드되지 않았습니다.");
	      }
	    });
	  }

	  // 모달에서 row 선택 시 호출되는 콜백 (전역)
	  window.handleSelectedEmp = function(row) {
	    // row의 필드명은 hrEmpModalGrid에서 지정한 name과 동일
	    empIdInput.value   = row.userId;
	    empNameInput.value = row.userName;
	  };

	  // ================= 이하 기존 코드 (증명서 미리보기, 저장 등) =================


	
	
});