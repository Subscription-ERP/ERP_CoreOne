document.addEventListener('DOMContentLoaded', () => {
  const Grid = tui.Grid;

  // toast grid 색상 변경
  Grid.applyTheme('default', {
    cell: {
      header: {
        background: '#fafafa'
      },
      normal: {
        background: '#ffffff',
      },
      rowHead: {
        background: '#ffffff',
      }
    },
  });

  // grid 정보
  const grid = new tui.Grid({
    el: document.getElementById('grid'),
    rowHeaders: ['rowNum'],
    bodyHeight: 300,
    scrollX: false,
    scrollY: false,
    columns: [
      { header: '거래처코드', name: 'custCode', align: 'center' },
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
      { header: '거래처유형  ', name: 'custType' },
      { header: '사용구분  ', name: 'useStatus', align: 'center' },
      { header: '담당자  ', name: 'userName' },
    ],
    data: []
  });

  custListData();

  // 거래처 정보 불러오기
  function custListData() {
    fetch('/api/sd/cust')
      .then(res => res.json())
      .then(result => {
        console.log(result);

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

  // 지도 검색을 눌렀을 때 Kakao Address API 켜지도록 설정
  document.getElementById("btnZipSearch").addEventListener('click', findAddress);

  // 초기화 버튼 클릭시 초기화 함수 실행
  document.getElementById("btnReset").addEventListener('click', resetData);
  
  // 행 클릭시 상세정보 보기
  grid.on('click', (ev) => {
    const rowKey = ev.rowKey;

    if (rowKey == null) return;

    const row = grid.getRow(rowKey);
    console.log(row);
    updateDetailPanel(row);
  });
  
  // 상세정보 불러오는 함수
  function updateDetailPanel(row) {
    if (!row) {
      // 선택 없을 때 초기화
      resetData();
      return;
    }
    
    console.log(row);
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
    document.querySelector('#custType').value = row.custType;
    document.querySelector('#dept').value = row.dept;
    document.querySelector('#userName').value = row.userName;
    document.querySelectorAll('input[name="useStatus"]').forEach(r => {
      r.checked = (r.value === row.useStatus);
    });
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
  }

});

