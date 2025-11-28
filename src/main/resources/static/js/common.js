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






