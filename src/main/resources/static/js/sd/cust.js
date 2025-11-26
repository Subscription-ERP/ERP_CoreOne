document.addEventListener('DOMContentLoaded', () => {
  const grid = new tui.Grid({
    el: document.getElementById('grid'),
    rowHeaders: ['checkbox'],
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '거래처코드', name: 'custCode' },
      { header: '거래처명', name: 'custName' },
      { header: '대표자명', name: 'ceoName' },
      { header: '연락처', name: 'phone' },
      { header: '이메일', name: 'custEmail' },
      { header: '주소  ', name: 'address' },
      { header: '거래처유형  ', name: 'custType' },
      { header: '사용구분  ', name: 'useStatus' },
      { header: '담당자  ', name: 'userName' },
    ],
    data: []
  });

  loadData();

  function loadData() {
    fetch('/api/sd/cust')
      .then(res => res.json())
      .then(result => {
        console.log(result);

        grid.resetData(result);
        grid.refreshLayout();

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
});

