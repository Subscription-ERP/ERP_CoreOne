/**
 * common.js
 */

/*document.addEventListener('DOMContentLoaded', () => {
  getDeptOptions();
  getCmCodeOptions();
});*/


/* ------------------------------------------------------------------
 * 1) 부서조회
 * ------------------------------------------------------------------ */
function getDeptOptions(){
	fetch('/api/hr/getDeptName')
		.then(res => res.json())
		.then(list => {
			const select = document.querySelector(".form-grid #dept");
			
			let line = "";
			
			list.forEach(d => {
				if(line != d.deptCode.charAt(1)){
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
 * 1) 부서조회 (여러 select 동시 세팅 가능 버전)
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
 * 2) 공통코드
 * ------------------------------------------------------------------ */

function getCmCodeOptions(divId){
	
	// 자동생성 : code=0A&code=0C&code=0D&code=0E
	const keys = Object.keys(divId);  // ['0A','0C','0D','0E']
	const param = keys.map(k => `code=${k}`).join('&');
		
	fetch(`/api/com/commonCodes?${param}`)
		.then(res => res.json())
		.then(list => {
			for(item in divId){
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
 * 3) 토스트 알람
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

