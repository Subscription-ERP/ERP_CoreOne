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
	
	const divId = { '0I': 'docType'  }
	getCmCodeOptions2(divId);

	
	
	/* ------------------------------------------------------------------
	 * 모달
	 * ------------------------------------------------------------------ */
	
	// 화면에 보이는 사원명 input
	const UserName     = document.getElementById("UserName");
	// 실제 선택된 사원 ID (hidden)
	const UserId = document.getElementById("UserId");  

	// 사원명 input 클릭 시 모달 열기
	if (UserName) {
	  UserName.addEventListener("click", (e) => {
	    if (typeof openUserSearchModal === 'function') {
	      openUserSearchModal(e);
	    } else {
	      console.error("에러가 발생했습니다.");
	    }
	  });
	}

	// 모달에서 row 선택 시 호출되는 콜백 (전역)
	window.handleSelectedEmp = function(row) {
	  if (UserId) {
	    UserId.value = row.userId;
	  }
	  if (UserName) {
	    UserName.value = row.userName;
	  }
	};


	/* ------------------------------------------------------------------
	 * 확인 버튼 클릭 이벤트 -> 증명서 등록(insert)
	 * ------------------------------------------------------------------ */
	
	const btnConfirm = document.querySelector("#btnConfirm");
	
	if(btnConfirm){
		btnConfirm.addEventListener("click", async () => {
			
			const fr = document.querySelector(".form-allwrapper");
			
			const payload = {
				userId : fr.querySelector("#UserId")?.value ?? "",
				docType : fr.querySelector("#docType")?.value ?? "",
				issueDate : fr.querySelector("#IssueDate")?.value ?? "",
				purpose : fr.querySelector("#purpose")?.value ?? "",
			}
			
			try{
				const res = await fetch('/api/hr/docs', {
					method: "POST",
					headers: {
						"Content-Type": "application/json;charset=UTF-8",
					},
					body: JSON.stringify(payload),
				});
				
				if(!res.ok){
					if(res.status === 401){
						throw new Error("로그인이 필요합니다.");
					}
					throw new Error("증명서 등록 중 서버 오류");
				}
				
				const vo = await res.json();   // controller에서 반환한 HrDocumentVO
				const docCode = vo.docCode;    // vo안에 있는 docCode 필드
				
				showToast("증명서가 생성되었습니다.", "success");
				
				// 미리보기 (Jasper PDF iframe)
				const previewContainer = document.querySelector("#certPreviewPage");
				if(previewContainer && docCode){
					const previewUrl = `/api/hr/docs/${docCode}/preview`;
					
					previewContainer.innerHTML = `
					          <iframe
					              src="${previewUrl}"
					              style="width:100%;height:100%;border:none;"
					          ></iframe>
					        `;
				}
								
			}catch(err){
				console.error(err);
				showToast("증명서 등록을 실패했습니다.", "error")
			}
			
			
		})
	}
	

	
	
});