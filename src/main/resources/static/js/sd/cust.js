document.addEventListener('DOMContentLoaded', () => {
  const Grid = tui.Grid;
  const form = document.getElementById('custForm');
  const btnSearch = document.getElementById("btnSearch");
  const btnSave = document.getElementById('btnSave');

  // toast grid 테마 변경
  Grid.applyTheme('clean');

  // grid 정보
  const grid = new tui.Grid({
    el: document.getElementById('grid'),
    rowHeaders: ['rowNum'],
    bodyHeight: 'fitToParent',
    scrollX: false,
    scrollY: true,
    columns: [
      {
        header: '거래처코드',
        name: 'custCode',
        align: 'center',
        sortingType: 'asc',
        sortable: true
      },
      { header: '거래처명', name: 'custName' },
      { header: '대표자명', name: 'ceoName' },
      { header: '연락처', name: 'phone' },
      { header: '이메일', name: 'custEmail' },
      {
        header: '주소',
        name: 'fullAddress',
        width: 300,
        formatter: ({ row }) => {
          const addr = row.address || '';
          const detail = row.addressDetail || '';
          return `${addr} ${detail}`.trim();
        },

      },
      { name: 'custTypeCode', hidden: true },
      { header: '거래처유형  ', name: 'custType' },
      { header: '사용구분  ', name: 'useStatus', align: 'center' },
      { header: '담당자  ', name: 'userName' },
    ],
    data: []
  });

  // 바로 실행
  custListData();
  custTypeListData();

  // 검색 눌렀을 때 검색 함수 실행
  btnSearch.addEventListener('click', searchCust);

  // Enter 눌렀을 때 검색
  document.getElementById('searchCustCode').addEventListener('keydown', handleEnter);
  document.getElementById('searchCustName').addEventListener('keydown', handleEnter);

  // 사용중단 포함 체크 변경 시 바로 검색
  document.getElementById("includeStopped").addEventListener('change', searchCust);

  // 초기화 버튼 클릭시 초기화 함수 실행
  document.getElementById("btnReset").addEventListener('click', resetData);

  // 지도 검색을 눌렀을 때 Kakao Address API 실행
  document.getElementById("btnOfficeSearch").addEventListener('click', findAddress);

  // 행 클릭시 상세정보 보기
  grid.on('click', (e) => {
    const rowKey = e.rowKey;
    if (rowKey == null) return;

    const row = grid.getRow(rowKey);
    updateDetailPanel(row);
    editMode(row);
  });

  // 저장시 유효성 검사
  btnSave.addEventListener('click', (e) => {
    console.log("눌렀습니다");
    e.preventDefault(); // 필요 시 기본 submit 막기
    if (!alertInfo()) { // false면 에러
      return;
    }
    form.submit(); // 통과하면 submit
  });

  // 함수 영역 ==================================================================

  // 거래처 정보 불러오기
  function custListData() {
    fetch('/api/sd/custList')
      .then(res => res.json())
      .then(result => {
        grid.resetData(result);
        grid.refreshLayout();

        grid.getData().forEach(row => {
          if (row.useStatus === 'N') {
            grid.addRowClassName(row.rowKey, 'row-inactive');
          }
        });

      })
      .catch(err => console.error(err));
  }

  // 거래처유형 불러오기
  function custTypeListData() {
    fetch('/api/com/type?groupCode=CUST_TYPE')
        .then(res => res.json())
        .then(result => {
          const searchCustType = document.getElementById('searchCustType');
          const custType = document.getElementById('custType');

          // 필요하면 초기화
          searchCustType.innerHTML = '<option value="">전체</option>';
          custType.innerHTML = '<option value="">선택하세요</option>';

          result.forEach(item => {
            // 거래처유형 (검색)
            const opt1 = document.createElement('option');
            opt1.value = item.code;
            opt1.textContent = item.codeName;
            searchCustType.appendChild(opt1);

            // 거래처유형 (상세정보)
            const opt2 = document.createElement('option');
            opt2.value = item.code;
            opt2.textContent = item.codeName;
            custType.appendChild(opt2);
          });
        })
        .catch(err => console.error(err));
  }

  // 거래처 검색
  function searchCust() {
    const custCode = document.getElementById('searchCustCode').value.trim();
    const custName = document.getElementById('searchCustName').value.trim();
    const custTypeCode = document.getElementById('searchCustType').value;
    const includeStopped = document.getElementById('includeStopped').checked;

    const params = { custCode, custName, custTypeCode, includeStopped };

    fetch('/api/sd/searchCust', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(params)
    })
        .then(res => res.json())
        .then(result => {
          grid.resetData(result);
          grid.refreshLayout();

          grid.getData().forEach(row => {
            if (row.useStatus === 'N') {
              grid.addRowClassName(row.rowKey, 'row-inactive');
            }
          });
        })
        .catch(err => console.error(err));

    resetData();
  }

  // 초기화 함수
  function resetData() {
    document.querySelector('#custCode').value  = '';
    document.querySelector('#custName').value = '';
    document.querySelector('#ceoName').value = '';
    document.querySelector('#bno').value = '';
    document.querySelector('#bType').value = '';
    document.querySelector('#bItem').value = '';
    document.querySelector('#phone').value = '';
    document.querySelector('#faxNo').value = '';
    document.querySelector('#custEmail').value = '';
    document.querySelector('#zonecode').value = '';
    document.querySelector('#address').value = '';
    document.querySelector('#addressDetail').value = '';
    document.querySelector('#custType').value = '';
    document.querySelector('#dept').value = '';
    document.querySelector('#userName').value = '';
    document.querySelectorAll('input[name="useStatus"]').forEach(r => {
      r.checked = r.value === 'Y';
    });
    saveMode();
  }

  // 상세정보 불러오는 함수
  function updateDetailPanel(row) {
    if (!row) {
      resetData();
      return;
    }

    document.querySelector('#custCode').value  = row.custCode;
    document.querySelector('#custName').value  = row.custName;
    document.querySelector('#ceoName').value  = row.ceoName;
    document.querySelector('#bno').value = row.bno;
    document.querySelector('#bType').value = row.btype;
    document.querySelector('#bItem').value = row.bitem;
    document.querySelector('#phone').value = row.phone;
    document.querySelector('#faxNo').value = row.faxNo;
    document.querySelector('#custEmail').value = row.custEmail;
    document.querySelector('#zonecode').value = row.zonecode;
    document.querySelector('#address').value = row.address;
    document.querySelector('#addressDetail').value = row.addressDetail;
    document.querySelector('#custType').value = row.custTypeCode;
    document.querySelector('#dept').value = row.dept;
    document.querySelector('#userName').value = row.userName;
    document.querySelectorAll('input[name="useStatus"]').forEach(r => {
      r.checked = (r.value === row.useStatus);
    });
  }

  // 저장
  function saveMode() {
    form.action = '/sd/cust/save';  // 저장 URL
    btnSave.textContent = '저장';
  }

  // 수정
  function editMode() {
    form.action = '/sd/cust/modify';
    btnSave.textContent = '수정';
  }

  // 유효성 검사
  function alertInfo() {

    let custName = document.querySelector('#custName').value;
    let ceoName = document.querySelector('#ceoName').value;
    let bno = document.querySelector('#bno').value;
    let phone = document.querySelector('#phone').value;
    let zonecode = document.querySelector('#zonecode').value;
    let address = document.querySelector('#address').value;
    let custType = document.querySelector('#custType').value;
    let userName = document.querySelector('#userName').value;
    let useStatusChecked = document.querySelector('input[name="useStatus"]:checked');

    if (custName === "") {
      showToast('상호(이름)을 입력하시오', 'warning');
      return false;
    } else if (ceoName === "") {
      showToast('대표자명을 입력하시오', 'warning');
      return false;
    } else if (bno === "") {
      showToast('사업자번호를 입력하시오', 'warning');
      return false;
    } else if (phone === "") {
      showToast('전화번호를 입력하시오', 'warning');
      return false;
    } else if (zonecode === "" || address === "") {
      showToast('주소를 입력하시오', 'warning');
      return false;
    } else if (custType === "") {
      showToast('거래처유형을 선택하시오', 'warning');
      return false;
    } else if (userName === "") {
      showToast('담당자를 입력하시오', 'warning');
      return false;
    } else if (!useStatusChecked) {
      showToast('사용여부를 선택하시오', 'warning');
      return false;
    }

    return true;
  }

  // Enter 입력 시 검색
  function handleEnter(e) {
    if(e.key === 'Enter') {
      e.preventDefault();
      btnSearch.click();
    }
  }

  // Kakao Address API
  function findAddress() {
    new daum.Postcode({
      oncomplete: function (data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        let roadAddr = data.roadAddress; // 도로명 주소 변수
        let zonecode = data.zonecode; // 도로명 주소 변수

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById("address").value = roadAddr;
        document.getElementById("zonecode").value = zonecode;
      }
    }).open();
  }

});

