const sampleData = [
  { 발행일자:'2025-11-01', 일련번호:'T251101-01', 거래처:'A업체', 공급가액:'9,000,000', 세액:'900,000', 합계금액:'9,900,000' },
  { 발행일자:'2025-11-01', 일련번호:'T251101-02', 거래처:'C업체', 공급가액:'1,750,000', 세액:'0', 합계금액:'1,750,000' },
  { 발행일자:'2025-11-01', 일련번호:'T251101-03', 거래처:'B업체', 공급가액:'10,000,000', 세액:'1,000,000', 합계금액:'11,000,000' },
  { 발행일자:'2025-11-01', 일련번호:'T251105-01', 거래처:'D업체', 공급가액:'9,800,000', 세액:'980,000', 합계금액:'10,780,000' },
  { 발행일자:'2025-11-01', 일련번호:'T251110-01', 거래처:'C업체', 공급가액:'100,000', 세액:'0', 합계금액:'100,000' }
];

const grid = new tui.Grid({
  el: document.getElementById('taxInvoiceGrid'),
  data: sampleData,
  scrollX: false,
  scrollY: true,
  bodyHeight: 'fitToParent',
  rowHeaders: ['rowNum'],

  columns: [
    { header:'발행일자', name:'발행일자', width:120 },
    { header:'일련번호', name:'일련번호', width:150 },
    { header:'거래처', name:'거래처', width:120 },
    { header:'공급가액', name:'공급가액', align:'right', width:130 },
    { header:'세액', name:'세액', align:'right', width:130 },
    { header:'합계금액', name:'합계금액', align:'right', width:130 }
  ]
});