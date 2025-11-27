
-- 시퀀스 생성
CREATE SEQUENCE certi_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999 NOCYCLE NOCACHE NOORDER;
CREATE SEQUENCE wex_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999 NOCYCLE NOCACHE NOORDER;
CREATE SEQUENCE ehist_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999 NOCYCLE NOCACHE NOORDER;

-- 데이터 : 공통코드그룹(tb_cm_code_group) 테이블
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0A', '입사구분', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0B', '부서', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0C', '직위/직급', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0D', '직책', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0E', '재직상태', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0F', '이력타입', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0G', '급여지급유형', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0H', '근태상태', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0I', '증명서 종류', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0J', '급여구분', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0K', '상여지급방법', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0L', '출고여부', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0M', '입고여부', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0N', '인사평가상태', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code_group ( group_code, group_name, remark, created_by, create_date, updated_by, update_date ) VALUES ( '0O', '입사구분', NULL, 'ADMIN', sysdate, NULL, NULL );

-- 데이터 : 공통코드(tb_cm_code) 테이블
INSERT INTO tb_cm_code ( code, group_code, code_name, remark, created_by, create_date, updtaed_by, update_date ) VALUES ( 'a1', '0A', '신입', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'a2', '0A', '경력', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'a3', '0A', '계약직', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b1', '0B', '인사', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b2', '0B', '회계', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b3', '0B', '영업', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b4', '0B', '물류', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b5', '0B', '자재', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b6', '0B', '개발', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b7', '0B', '품질', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'b8', '0B', '마케팅', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c1', '0C', '사원', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c2', '0C', '주임', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c3', '0C', '대리', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c4', '0C', '과장', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c5', '0C', '차장', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'c6', '0C', '부장', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'd1', '0D', '팀원', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'd2', '0D', '팀장', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( '0', '0E', '재직', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( '1', '0E', '휴직', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( '2', '0E', '퇴사', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'f1', '0F', '부서이력', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'f2', '0F', '근속이력', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'g1', '0G', '급여', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'g2', '0G', '상여', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'g3', '0G', '명절수당', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'g4', '0G', '야근수당', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'g5', '0G', '휴일수당', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h1', '0H', '정상', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h2', '0H', '지각', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h3', '0H', '조퇴', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h4', '0H', '결근', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h5', '0H', '반차', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h6', '0H', '연차', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h7', '0H', '병가', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h8', '0H', '외근', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h9', '0H', '출장', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'h10', '0H', '휴무', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'j1', '0I', '재직', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'j2', '0I', '경력', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'k1', '0J', '급여', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'k2', '0J', '상여', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'l1', '0K', '지급율', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'l2', '0K', '지급액', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( '0', '0N', '미평가', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( '1', '0N', '평가완료', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'o1', '0O', '신입', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'o2', '0O', '경력', NULL, 'ADMIN', sysdate, NULL, NULL );
INSERT INTO tb_cm_code VALUES ( 'o3', '0O', '계약직', NULL, 'ADMIN', sysdate, NULL, NULL );

-- 데이터 : 사원(tb_user_master) 테이블
-- 1. 인사팀 김사원 (HR, 사원) - 신입(o1)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP25010200001', '김사원', '010-1111-2222', 'kim.hr@root.com', DATE '2025-01-02', 'o1', NULL, NULL, '03724', '서울 서대문구 증가로 24', 'D410', 'c1', 'd1', 1, 0, NULL, '국민은행', '1234567890', '김사원', 35000000, '0', NULL, NULL, '신규 입사자', 'ADMIN', sysdate, NULL, NULL );
-- 2. 영업팀 이대리 (SALES, 대리) - 경력(o2)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP24051500002', '이대리', '010-3333-4444', 'lee.sales@root.com', DATE '2024-05-15', 'o2', NULL, NULL, '06233', '서울 강남구 테헤란로 123', 'D213', 'c3', 'd1', 2, 0, NULL, '신한은행', '1109876543', '이대리', 48000000, '0', NULL, NULL, '핵심 영업 인력', 'ADMIN', sysdate, NULL, NULL );
-- 3. 개발팀 박과장 (DEV, 과장) - 경력(o2)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP23030100003', '박과장', '010-5555-6666', 'park.dev@root.com', DATE '2023-03-01', 'o2', NULL, NULL, '08826', '서울 관악구 남부순환로 150', 'D312', 'c4', 'd1', 4, 2, NULL, '우리은행', '1000123456', '박과장', 65000000, '0', NULL, NULL, '팀 내 최고 개발자', 'ADMIN', sysdate, NULL, NULL );
-- 4. 기획팀 최부장 (PLAN, 부장) - 경력(o2)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP19100100004', '최부장', '010-7777-8888', 'choi.plan@root.com', DATE '2019-10-01', 'o2', NULL, NULL, '21990', '인천 연수구 송도과학로 10', 'D513', 'c6', 'd1', 3, 1, 'Y', '농협은행', '3024567890', '최부장', 80000000, '0', NULL, NULL, '사업 기획 총괄', 'ADMIN', sysdate, NULL, NULL );
-- 5. 개발팀 정주임 (DEV, 주임) - 계약직(o3)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP25041000005', '정주임', '010-9999-0000', 'jung.dev@root.com', DATE '2025-04-10', 'o3', NULL, NULL, '41131', '경기 고양시 일산동구 중앙로 50', 'D312', 'c2', 'd1', 1, 0, NULL, '신한은행', '1101234567', '정주임', 38000000, '0', NULL, NULL, '6개월 계약직 (재직)', 'ADMIN', sysdate, NULL, NULL );
-- 6. 영업팀 윤사원 (SALES, 사원) - 신입(o1)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP25070100006', '윤사원', '010-1234-5678', 'yoon.sales@root.com', DATE '2025-07-01', 'o1', NULL, NULL, '34054', '대전 유성구 대덕대로 100', 'D213', 'c1', 'd1', 1, 0, NULL, '국민은행', '3333098765', '윤사원', 34000000, '0', NULL, NULL, '신입 사원', 'ADMIN', sysdate, NULL, NULL );
-- 7. 인사팀 문과장 (HR, 과장) - 경력(o2)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP22090100007', '문과장', '010-2468-1357', 'moon.hr@root.com', DATE '2022-09-01', 'o2', NULL, NULL, '47246', '부산 부산진구 중앙대로 800', 'D410', 'c4', 'd1', 3, 1, NULL, '하나은행', '4567890123', '문과장', 59000000, '0', NULL, NULL, '급여 담당자', 'ADMIN', sysdate, NULL, NULL );
-- 8. 재무팀 한대리 (FINANCE, 대리) - 경력(o2)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP23112000008', '한대리', '010-9876-5432', 'han.finance@root.com', DATE '2023-11-20', 'o2', NULL, NULL, '04520', '서울 중구 을지로 50', 'D111', 'c3', 'd1', 2, 0, 'Y', '우리은행', '1000654321', '한대리', 45000000, '0', NULL, NULL, '회계 및 정산 담당', 'ADMIN', sysdate, NULL, NULL );
-- 9. 개발팀 오주임 (DEV, 주임) - 경력(o2), 퇴사자
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP24010100009', '오주임', '010-4321-8765', 'oh.dev@root.com', DATE '2024-01-01', 'o2', DATE '2025-01-31', '개인사정', '13594', '경기 성남시 분당구 판교역로 200', 'D312', 'c2', 'd1', 1, 0, NULL, '국민은행', '7777112233', '오주임', 40000000, '2', NULL, NULL, '2025년 1월 31일 퇴사', 'ADMIN', sysdate, NULL, NULL );
-- 10. 마케팅팀 신사원 (MARKET, 사원) - 신입(o1)
INSERT INTO tb_user_master ( company_code, user_id, user_name, tel, email, hire_date, hire_type, leave_date, leave_reason, zip_code, address, dept, job_title, position, family_count, children_count, householder, bank_name, account_no, account_holder, salary, user_status, user_photo, user_file, remark, created_by, create_date, updated_by, update_date )
VALUES ( 'ROOT', 'EMP25060100010', '신사원', '010-8642-7531', 'shin.market@root.com', DATE '2025-06-01', 'o1', NULL, NULL, '07333', '서울 영등포구 국제금융로 10', 'D512', 'c1', 'd1', 1, 0, 'Y', '카카오뱅크', '3333224466', '신사원', 32000000, '0', NULL, NULL, '신규 채용', 'ADMIN', sysdate, NULL, NULL );

-- 데이터 : 경력사항(tb_work_experience) 테이블
-- 1. 김사원 
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 1, 'ROOT', 'EMP25010200001', 'ABC유통', '인사팀', '인사보조', DATE '2023-01-02', DATE '2023-12-31', '입퇴사 서류 보조, 4대보험 처리 지원, 인사 DB 정리', 28000000, '1년 계약직 근무', 'ADMIN', SYSDATE, NULL, NULL );
-- 2. 이대리
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 2, 'ROOT', 'EMP24051500002', '글로벌무역상사', '영업1팀', '영업사원', DATE '2017-03-01', DATE '2024-04-30', '국내·해외 거래처 관리, 매출 분석, 견적/계약 업무, 클레임 처리', 42000000, '매출 우수사원 2회 수상', 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 4, 'ROOT', 'EMP24051500002', '코리아상사', '영업지원팀', '영업지원', DATE '2015-01-01', DATE '2017-02-28', '견적서 작성, 매출 자료 정리, 고객 응대, 내부 보고서 작성', 31000000, '영업 관리 기본 역량 확보', 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 5, 'ROOT', 'EMP24051500002', '한빛유통', '물류영업팀', '주문/출고관리', DATE '2013-03-01', DATE '2014-12-31', '납품 일정 관리, 출고 품질 점검, 거래처 출고 요청 대응', 28000000, '물류-영업 협업 경험', 'ADMIN', SYSDATE, NULL, NULL );
-- 3. 박과장 
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 3, 'ROOT', 'EMP23030100003', '테크솔루션', '개발본부', '백엔드 개발자', DATE '2016-01-01', DATE '2023-02-28', 'Java/Spring 기반 백엔드 개발, DB 모델링, 시스템 운영 및 성능 개선', 60000000, '프로젝트 리드 경험 보유', 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 6, 'ROOT', 'EMP23030100003', '아이티넷', '개발팀', '백엔드 개발자', DATE '2013-07-01', DATE '2015-12-31', 'Spring Framework 기반 API 개발, 사내 인트라넷 기능 유지보수', 45000000, '내부 시스템 개선 프로젝트 참여', 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_work_experience ( wex_seq, company_code, user_id, wex_company_name, wex_dept, wex_job_title, wex_hire_date, wex_leave_date, wex_main_duty, wex_salary, remark, created_by, create_date, updated_by, update_date )
VALUES ( 7, 'ROOT', 'EMP23030100003', '에이스정보기술', 'SI개발팀', '신입 개발자', DATE '2011-01-03', DATE '2013-06-30', '공공기관 SI 프로젝트 지원, JSP/Servlet 페이지 개발 및 테스트', 32000000, '첫 개발 직무 경험', 'ADMIN', SYSDATE, NULL, NULL );

--  데이터 : 이력(tb_user_history) 테이블
-- 1. 김사원
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 1, 'ROOT', 'EMP25010200001', 'f1', DATE '2025-09-01', 'D411', 'c1', 'D410', 'c1', '조직개편에 따른 부서 이동', NULL, NULL, NULL, NULL, 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 2, 'ROOT', 'EMP25010200001', 'f2', DATE '2026-01-01', NULL, NULL, NULL, NULL, NULL, 32000000, 35000000, 'j1', '수습 종료에 따른 연봉 인상', 'ADMIN', SYSDATE, NULL, NULL );
-- 3. 이대리
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 3, 'ROOT', 'EMP24051500002', 'f1', DATE '2024-10-01', 'D211', 'c2', 'D212', 'c2', '물류 업무 로테이션에 따른 부서 이동', NULL, NULL, NULL, NULL, 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 4, 'ROOT', 'EMP24051500002', 'f1', DATE '2025-06-01', 'D212', 'c2', 'D213', 'c3', '수송관리팀 배치 및 승진(주임→대리)', NULL, NULL, NULL, NULL, 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 5, 'ROOT', 'EMP24051500002', 'f2', DATE '2025-01-01', NULL, NULL, NULL, NULL, NULL, 42000000, 48000000, 'j1', '연간 실적 우수에 따른 연봉 인상', 'ADMIN', SYSDATE, NULL, NULL );
-- 6. 박과장 
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 6, 'ROOT', 'EMP23030100003', 'f1', DATE '2024-04-01', 'D311', 'c3', 'D312', 'c4', 'ERP 프로젝트 전담 조직 이동 및 승진(대리→과장)', NULL, NULL, NULL, NULL, 'ADMIN', SYSDATE, NULL, NULL );
INSERT INTO tb_user_history ( ehist_seq, company_code, user_id, hist_type, apply_date, prev_dept, prev_job_title, new_dept, new_job_title, dept_change_reason, base_salary, total_salary, pay_type, salary_change_reason, created_by, create_date, updated_by, update_date )
VALUES ( 7, 'ROOT', 'EMP23030100003', 'f2', DATE '2024-04-01', NULL, NULL, NULL, NULL, NULL, 58000000, 65000000, 'j1', '과장 승진에 따른 연봉 인상', 'ADMIN', SYSDATE, NULL, NULL );



-- 사원전체테이블조회
SELECT
    um.company_code,
    um.user_id,
    um.user_name,
    um.tel,
    um.email,
    um.hire_date,
    um.hire_type,
    um.leave_date,
    um.leave_reason,
    um.zip_code,
    um.address,
    um.dept,
    dm.dept_name,
    um.job_title,
    um.position,
    um.family_count,
    um.children_count,
    um.householder,
    um.bank_name,
    um.account_no,
    um.account_holder,
    um.salary,
    um.user_status,
    um.user_photo,
    um.user_file,
    um.remark,
    c.certi_name,
    c.issue_org_name,
    c.get_date,
    c.license_no,
    c.expire_date,
    c.certi_file,
    c.remark,
    wex.wex_company_name,
    wex.wex_dept,
    wex.wex_job_title,
    wex.wex_hire_date,
    wex.wex_leave_date,
    wex.wex_main_duty,
    wex.wex_salary,
    wex.remark,
    uh.hist_type,
    uh.apply_date,
    uh.prev_dept,
    uh.prev_job_title,
    uh.new_dept,
    uh.new_job_title,
    uh.dept_change_reason,
    uh.base_salary,
    uh.total_salary,
    uh.pay_type,
    uh.salary_change_reason
FROM            tb_user_master um
LEFT JOIN    tb_dept_master dm
             ON    um.dept = dm.dept_code
LEFT JOIN    tb_certification c
             ON    um.user_id = c.user_id                         -- 자격증
LEFT JOIN    tb_work_experience wex
             ON    um.user_id = wex.user_id                    -- 경력사항
LEFT JOIN    tb_user_history uh
              ON   um.user_id = uh.user_id                       -- 이력
ORDER BY
    hire_date DESC;
