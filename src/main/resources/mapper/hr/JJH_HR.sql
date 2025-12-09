-- 대장급여-상여등록-사원조회쿼리문
SELECT user_id,
       user_name,
       dept,
       hire_date,
       job_title
FROM   tb_user_master
WHERE  leave_date IS NULL;

SELECT *
FROM   tb_user_master;

-- =========
-- 20251126
-- =========

-- 사원조회
SELECT *
FROM   tb_user_master;

-- =========
-- 20251127
-- =========

-- ===============================
-- TB_PAYROLL 샘플 데이터 (총 10건)
-- ===============================
-- 조건: PAYROLL_PERIOD='202509', PAYROLL_TYPE='급여', PAYROLL_NAME='2025년 9월 정기 급여대장', PAYROLL_DATE='2025-10-10' (모두 동일)

-- 1. 김사원 (H2025001)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_01', 'ROOT', 'H2025001', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 2. 이대리 (S2024005)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_02', 'ROOT', 'S2024005', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 3. 박과장 (D2023010)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_03', 'ROOT', 'D2023010', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 4. 최부장 (P2019003)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_04', 'ROOT', 'P2019003', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 5. 정주임 (D2025002)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_05', 'ROOT', 'D2025002', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 6. 윤사원 (S2025008)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_06', 'ROOT', 'S2025008', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 7. 문과장 (H2022007)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_07', 'ROOT', 'H2022007', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 8. 한대리 (F2023012)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_08', 'ROOT', 'F2023012', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 9. 오주임 (D2024009)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_09', 'ROOT', 'D2024009', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);

-- 10. 신사원 (M2025015)
INSERT INTO TB_PAYROLL (PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_NAME, PAYROLL_START_DATE, PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_TYPE, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE)
VALUES ('P202509_10', 'ROOT', 'M2025015', '202509', '2025년 9월 정기 급여대장', DATE '2025-09-01', DATE '2025-09-30', DATE '2025-10-10', '급여', 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE);



-- ================================
-- TB_DEPT_MASTER 샘플 데이터 INSERT
-- ================================
-- CEO
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D000','ROOT','CEO',NULL,1,SYSDATE,'0','ADMIN',SYSDATE);

-- 재무총괄 / 회계부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D100','ROOT','재무총괄', 'D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D110','ROOT','회계부','D100',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D111','ROOT','재무회계팀','D110',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D112','ROOT','관리회계팀','D110',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D113','ROOT','세무팀','D110',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 운영총괄 / 물류부, 자재부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D200','ROOT','운영총괄','D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D210','ROOT','물류부','D200',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D211','ROOT','창고관리팀','D210',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D212','ROOT','배송팀','D210',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D213','ROOT','수송관리팀','D210',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D220','ROOT','자재부','D200',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D221','ROOT','구매팀','D220',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D222','ROOT','재고관리팀','D220',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D223','ROOT','자재기획팀','D220',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 기술총괄 / 개발부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D300','ROOT','기술총괄','D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D310','ROOT','개발부','D300',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D311','ROOT','시스템운영팀','D310',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D312','ROOT','ERP개발팀','D310',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D313','ROOT','인프라팀','D310',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 인사총괄 / 인사부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D400','ROOT','인사총괄','D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D410','ROOT','인사부','D400',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D411','ROOT','인재채용팀','D410',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D412','ROOT','급여/복리후생팀','D410',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D413','ROOT','교육/평가팀','D410',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 마케팅총괄 / 마케팅부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D500','ROOT','마케팅총괄','D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D510','ROOT','마케팅부','D500',3,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D511','ROOT','브랜드마케팅팀','D510',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D512','ROOT','디지털마케팅팀','D510',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D513','ROOT','CRM/고객분석팀','D510',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 품질부
INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D600','ROOT','품질부','D000',2,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D611','ROOT','품질검사팀','D600',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D612','ROOT','불량관리팀','D600',4,SYSDATE,'0','ADMIN',SYSDATE);

INSERT INTO TB_DEPT_MASTER(DEPT_CODE, COMPANY_CODE, DEPT_NAME, UPPER_DEPT_NO, DEPT_LEVEL, START_DATE, STATUS, CREATED_BY, CREATE_DATE)
VALUES('D613','ROOT','품질개선팀','D600',4,SYSDATE,'0','ADMIN',SYSDATE);

-- 공통코드 부서 데이터 삭제
SELECT *
FROM   tb_cm_code;
SELECT *
FROM   tb_cm_code_group;
DELETE FROM tb_cm_code
WHERE group_code = '0B';
DELETE FROM tb_cm_code_group
WHERE group_code = '0B';

-- 급여 생성 함수
create or replace FUNCTION FN_MAKE_PAYROLL_CODE
RETURN VARCHAR2
IS
    v_date          VARCHAR2(8);
    v_max           VARCHAR2(50);
    v_seq_num       NUMBER;
    v_new_seq       VARCHAR2(5);
    v_payroll_code  VARCHAR2(50);
BEGIN
    -- 오늘 날짜
    v_date := TO_CHAR(SYSDATE, 'YYMMDD');

    SELECT MAX(PAYROLL_CODE)
      INTO v_max
      FROM TB_PAYROLL
     WHERE payroll_code LIKE 'PRL' || v_date || '%';

    IF v_max IS NULL THEN
        v_new_seq := '00001';
    ELSE
        v_seq_num := TO_NUMBER(SUBSTR(v_max, 12, 5)) + 1;
        v_new_seq := LPAD(v_seq_num, 5, '0');
    END IF;

    v_payroll_code := 'CPY' || v_date || v_new_seq;
    RETURN v_payroll_code;
END;

-- 급여대장-상여등록
--INSERT INTO tb_payroll (
--            payroll_code,
--            company_code,
--            user_id,
--            payroll_period,
--            payroll_type,
--            bonus_method,
--            bonus_rate,
--            bonus_amount,
--            payroll_name,
--            payroll_start_date,
--            payroll_end_date,
--            payroll_date
--        )   
--        VALUES (
--              FN_MAKE_PAYROLL_CODE(), 
--              'ROOT',
--              #{userId},
--              #{payrollPeriod},
--              '상여',
--              #{bonusMethod},  #{bonusRate},
--              #{bonusAmount},
--              #{payrollName},
--              #{payrollStartDate},
--              #{payrollEndDate},
--              #{payrollDate}
--        );

--=====================
--20251130
--=====================
--급여귀속코드 생성하는 함수
CREATE OR REPLACE FUNCTION FN_MAKE_PERIOD_CODE
RETURN VARCHAR2
IS
    v_prefix VARCHAR2(3) := 'PRP';
    v_date   VARCHAR2(6) := TO_CHAR(SYSDATE, 'YYMMDD');
    v_seq    VARCHAR2(5);
BEGIN
    -- 'PRP251129'로 시작하는 코드 중 최대 시퀀스를 찾음
    SELECT LPAD(NVL(MAX(TO_NUMBER(SUBSTR(PAYROLL_PERIOD_CODE, 10))) , 0) + 1, 5, '0')
    INTO v_seq
    FROM TB_PAYROLL
    WHERE PAYROLL_PERIOD_CODE LIKE v_prefix || v_date || '%';
    
    RETURN v_prefix || v_date || v_seq;
END;
/

-- 상여로 되어있는 이름을 j2로 수정
UPDATE tb_payroll
SET payroll_type = 'j2';

SELECT *
FROM   tb_user_master;
SELECT *
FROM   tb_dept_master;
SELECT *
FROM   tb_cm_code;

-- 사원조회(공통코드 적용)
SELECT um.user_id,
       um.user_name,
       dm.dept_name AS dept
FROM   tb_user_master um
       JOIN tb_dept_master dm
       ON um.dept = dm.dept_code;

SELECT *
FROM   tb_dept_master;

-- 급여 대장 목록 조회
SELECT   pr.payroll_period,
         cc.code_name AS payroll_type,
         pr.payroll_name,
         pr.payroll_date,
         pr.create_date,
         pr.payroll_period_code,
         COUNT(*) AS peopleNumber,
         SUM(upm.total_allowance) as totalAmount, -- 지급총액
         pr.payroll_code
FROM     tb_payroll pr
         JOIN tb_cm_code cc
         ON pr.payroll_type = cc.code
         LEFT JOIN tb_user_pay_management upm
         ON upm.payroll_code = pr.payroll_code
GROUP BY pr.payroll_period, 
         cc.code_name,
         pr.payroll_name, 
         pr.payroll_date, 
         pr.create_date,
         pr.payroll_period_code,
         pr.payroll_code;      
                 
SELECT *
FROM   tb_payroll;

--=========================================
--20251201
--=========================================
-- tb_payroll에 회사 코드 0000으로 수정
UPDATE tb_payroll
SET company_code = '0000';

-- 컬렉션
-- 컬렉션 레코드 타입 생성(결과를 담을 한 행의 구조 정의)
-- T_PAYROLL_RESULT_REC는 급여 계산 결과의 '한 행(ROW)' 또는 '한 레코드(RECORD)'의 구조를 정의함
-- 데이터베이스의 테이블을 생성할 때 각 컬럼을 정의하는것과 같다.
CREATE OR REPLACE TYPE T_PAYROLL_RESULT_REC AS OBJECT (
    payroll_code VARCHAR2(20),
    user_id                     VARCHAR2(20),
    user_name                   VARCHAR2(100),
    dept_name                   VARCHAR2(100),
    payroll_date                DATE,
    salary                      NUMBER,
    bonus                       NUMBER,
    overtime                    NUMBER,
    night                       NUMBER,
    holiday                     NUMBER,
    family                      NUMBER,
    meal                        NUMBER,
    annual_leave                NUMBER,
    total_allowance             NUMBER,
    total_payment_amount        NUMBER,
    national_pension            NUMBER,
    employment_insurance        NUMBER,
    health_insurance            NUMBER,
    long_time_care_insurance    NUMBER,
    total_deduction_amount      NUMBER,
    net_pay                     NUMBER,
    absence NUMBER
);
/

-- 테이블 타입(위 레코드 타입을 여러 개 담을 수 있는 목록 구조 정의)
CREATE OR REPLACE TYPE T_PAYROLL_RESULTS_TAB IS TABLE OF T_PAYROLL_RESULT_REC;
/   

-- 정리해보자면 여러개의 T_PAYROLL_RESULT_REC 컬렉션 레코드가 하나의 테이블타입 T_PAYROLL_RESULTS_TAB 여기에 저장

-- 급여 계산 프로시저
CREATE OR REPLACE PROCEDURE sp_calculate_payroll(
    -- 어떤 대장을 급여계산할건지 선언
    p_payroll_period_code IN VARCHAR2, -- 계산할 대장 코드
    p_out_results OUT SYS_REFCURSOR -- Ref Cursor 반환
    -- 줄여서 간단하게 말하자면 결과 반환 통로다. 결과를 외부로 내보내는 통로
    -- 여기서 OUT이 프로시저가 실행된 후에 값을 반환한다~ 라는 의미임 > 말그대로 출력용
    -- SYS_REFCURSOR은 ORACLE에서 제공하는 특별한 데이터 타입임
    -- 일반적인 변수(VARCHAR2, NUMBER)는 하나의 값만 담는다면 REF CURSOR는 여러행과 열로 이루어진 데이터 셋 자체를 담아서 반환함
    -- JAVA에서 프로시저를 호출하면 p_our_results를 통해서 전달받은 커서를 SELECT문을 실행한 것처럼 순회하면서 데이터를 읽어 올 수 있음.
)
/* ================================
 * 프로시저명 : sp_calculate_payroll
 * 설명 : 급여 계산 프로시저
 * 작성자 : 장준현
 * 작성일 : 25.12.03
 * 수정이력 :
    * 수정일 :
 * ================================ */
IS
    -- 1) 커서 정의
    -- 위에서 선언한 대장에 해당하는 사원조회
    CURSOR user_cursor IS -- 커서 선언
        SELECT pr.payroll_code, -- 급여대장코드
               pr.user_id, -- 사용자 id
               um.user_name, -- 성명
               dm.dept_name, -- 부서명
               pr.payroll_date, -- 지급일               
               um.salary, -- 기본급여
               pr.bonus_rate, -- 상여 지급율
               pr.bonus_amount, -- 상여 지급액
               ad.total_over_work_time, -- 총 연장근무시간
               ad.total_night_work_time, -- 총 야간근무시간
               ad.total_holiday_work_time, -- 총 휴일근무시간
               um.family_count, -- 가족수
               um.children_count, -- 자녀수
               al.expiry_date, -- 소멸예정일
               al.remaining_days, -- 잔여연차
               pr.payroll_start_date, -- 대장시작시간
               absence.absenceCount -- 결근, 병가 얼마나 썼는지
        FROM   tb_payroll pr
               JOIN tb_user_master um -- 사원관리 테이블
               ON pr.user_id = um.user_id
               LEFT JOIN ( SELECT ad_sub.user_id,
                                  SUM(ad_sub.over_work_time) as total_over_work_time,
                                  SUM(ad_sub.night_work_time) as total_night_work_time,
                                  SUM(ad_sub.holiday_work_time) as total_holiday_work_time
                           FROM   tb_attendance ad_sub -- 근태관리 테이블
                                  JOIN tb_payroll pr_sub -- 급여대장 테이블
                                  ON ad_sub.user_id = pr_sub.user_id
                           WHERE  pr_sub.payroll_period_code = p_payroll_period_code -- 매개변수로 받은 신고귀속코드랑 급여대장에있는 신고귀속코드가 같은걸 찾고
                           AND    ad_sub.work_date >= pr_sub.payroll_start_date -- work_date일한시간이 대장시작시간보다 크거나 같은거
                           AND    ad_sub.work_date <= pr_sub.payroll_end_date -- work_date일한시간이 대장종료시간보다 작거나 같은거
                           GROUP BY ad_sub.user_id -- 이렇게 하면 몇월에 대한 급여인지 알게됨
                          ) ad 
                ON ad.user_id = pr.user_id
                LEFT JOIN tb_annual_leave al
                ON al.user_id = pr.user_id
                JOIN tb_dept_master dm
                ON dm.dept_code = um.dept
                LEFT JOIN ( SELECT att_sub.user_id,
                                   COUNT(*) as absenceCount
                            FROM   tb_attendance att_sub
                                   JOIN tb_payroll pr_sub
                                   ON att_sub.user_id = pr_sub.user_id
                            WHERE  pr_sub.payroll_period_code = p_payroll_period_code
                              AND  att_sub.work_date >= pr_sub.payroll_start_date -- work_date일한시간이 대장시작시간보다 크거나 같은거
                              AND  att_sub.work_date <= pr_sub.payroll_end_date
                              AND  (attend_type = 'h4' OR  attend_type = 'h7')
                           GROUP BY att_sub.user_id ) absence
                ON pr.user_id = absence.user_id
         WHERE  pr.payroll_period_code = p_payroll_period_code;
        
    -- 변수선언
    v_uid tb_payroll.user_id%TYPE; -- 사용자id
    v_sal tb_user_master.salary%TYPE; -- 기본급여
    v_br tb_payroll.bonus_rate%TYPE; -- 지급율
    v_ba tb_payroll.bonus_amount%TYPE; -- 지급액
    v_bonus NUMBER; -- 상여금
    v_ordinary_wage NUMBER; -- 통상임금
    v_overtime NUMBER; -- 연장근로수당
    v_night NUMBER; -- 야간근로수당
    v_holiday NUMBER; -- 휴일근로수당
    v_family NUMBER; -- 가족수당
    v_meal NUMBER; -- 식대
    v_annual_leave NUMBER; -- 연차휴가수당
    v_total_allowance NUMBER; -- 총수당총액
    v_earnings NUMBER; -- 소득
    v_national_pension NUMBER; -- 국민연금
    v_employment_insurance NUMBER; -- 고용보험
    v_health_insurance NUMBER; -- 건강보험
    v_long_time_care_insurance NUMBER; -- 장기요양보험
    v_net_pay  NUMBER; -- 실수령액
    v_total_payment_amount NUMBER; -- 총 지급액(상여포함)
    v_total_deduction_amount NUMBER; -- 총 4대보험
    v_absence NUMBER; -- 결근, 병가 공제액
    
    -- 결과 컬렉션 변수 선언
    v_results T_PAYROLL_RESULTS_TAB;
    -- 프로시저 내에서 계산된 수많은 사원의 급여 데이터를 임시로 저장하고 관리하기 위해서는 컬렉션(COLLECTION)이라는 PL/SQL 내부 데이터 구조를 사용
    -- v_results : 컬랙션 변수 이름
    -- T_PAYROLL_RESULTS_TAB : 미리 데이터베이스에 정의된 '중첩테이블타입'임
    -- 계산 결과를 담을 수 있는 구조화된 목록(리스트) 라고 생각하면 됨
    -- 이걸 사용할려면 반드시 레코드타입과, 테이블 타입이 미리 선언되어 있어야 함
BEGIN
    -- BEGIN 블록 시작 시 컬렉션을 명시적으로 초기화
    v_results := T_PAYROLL_RESULTS_TAB();
    -- 단순 변수 선언만으로는 메모리에 공간이 할당되지 않음
    -- 예시) 자바에서 arraylist객체를 선언하고 new arraylist()를 안한것과 비슷한거임
    -- T_PAYROLL_RESULTS_TAB() : 컬렉션의 생성자 함수를 호출해서 메모리에 컬렉션 객체를 생성
    -- 이 객체를 v_results 변수에 할당(초기화)
    -- 만약에 이 명시적 초기화를 안하면 나중에 .EXTEND나 .LAST를 사용할때 오류가 발생함
    
    -- 2) 커서 실행
    -- 3) 데이터 확인 및 인출
    FOR user_info IN user_cursor LOOP        
        -- 변수 설정
        v_uid := user_info.user_id; -- 사용자id
        v_sal := user_info.salary; -- 기본급여
        v_br := user_info.bonus_rate; -- 상여 지급율
        v_ba := user_info.bonus_amount; -- 상여 지급액
        -- 위에서 fetch into해서 실행한거 결과 출력
        DBMS_OUTPUT.PUT_LINE('v_uid:'||v_uid); -- 사용자id
        DBMS_OUTPUT.PUT_LINE(',v_sal:'||v_sal); -- 급여
        
        
        -- =================================================================================================================
        -- 209시간
        -- 주 40시간 근무하는 월급제 근로자의 월평균 소정근로시간을 의미
        -- 1주 소정근로시간 40시간에 유급주휴 8시간(40시간 / 8시간 × 1일 8시간)을 더한 48시간을 월평균(4.345주)으로 환산하여 반올림한 값
        -- (주40시간 + 주휴 8시간) X 4.345 = 약209시간
        -- =================================================================================================================
        -- 시간당 통상임금(통상시급) := 기본급 / 209
        v_ordinary_wage := ROUND(v_sal / 209);
        DBMS_OUTPUT.PUT_LINE(',v_ordinary_wage:'||v_ordinary_wage);
        
        -- ===============================================================
        -- payroll_type가 j1(상여)일 경우 지급액중에서는 상여만 나타나며
        -- 공제에서는 소득세, 지방소득세, 건강보험, 고용보험, 장기요양 만 빠짐
        -- 상여는 한마디로 국민연금은 계산안함(국민연금은 정기적 급여만)
        -- ===============================================================
        IF v_br > 0 OR v_ba > 0 THEN
            -- 상여금
            IF v_br > 0 THEN
                -- 지급율(%)방식 : 기본급 * (상여 지급율 / 100)
                v_bonus := v_sal * (v_br / 100);
            ELSIF v_ba > 0 THEN
                -- 지급액
                v_bonus := v_ba;
            ELSE
                v_bonus := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE(',v_bonus:'||v_bonus);
            
            v_sal := 0;
            v_earnings := v_bonus;
            v_total_payment_amount := v_bonus;
            v_national_pension := 0;
            v_total_allowance := v_bonus; -- 수당총액
        ELSE
            -- 연장근로수당
            IF user_info.total_over_work_time > 0 THEN   
                -- 연장근로수당 := 시간당 통상임금 * 총연장근무시간 * 1.5
                v_overtime := v_ordinary_wage * user_info.total_over_work_time * 1.5;
            ELSE
                v_overtime := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE(',v_overtime:'||v_overtime);
                    
            -- 야간근로수당
            IF user_info.total_night_work_time > 0 THEN
                -- 야간근로수당 := 시간당 통상임금 * 총야간근로수당 * 1.5
                v_night := v_ordinary_wage * user_info.total_night_work_time * 1.5;
            ELSE
                v_night := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE(',v_night:'||v_night);
            
            -- 휴일근로수당
            IF user_info.total_holiday_work_time > 0 THEN
                -- 휴일근로수당 := 시간당 통상임금 * 총휴일근로수당 * 1.5
                v_holiday := v_ordinary_wage * user_info.total_holiday_work_time * 1.5;
            ELSE
                v_holiday := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE(',v_holiday:'||v_holiday);
                    
            -- 가족수당
            IF user_info.family_count > 0 THEN
                IF (user_info.family_count - user_info.children_count) > 1 THEN
                    -- 가족수당 := 40000 * 1명(배우자) + 50000 * 2명(자녀수)
                    v_family := 40000 * 1 + 50000 * user_info.children_count;
                ELSE
                    v_family := 50000 * user_info.children_count;
                END IF;
            ELSE
                v_family := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE(',v_family:'||v_family);
            
            -- 식대(월)
            v_meal := 200000;
            DBMS_OUTPUT.PUT_LINE(',v_meal:'||v_meal);
            
            -- 연차휴가수당
            -- 미사용연차일수 * 1일 통상임금(통상시급 * 8)
            -- 25년에서 26년으로 넘어갔어 그럼 연차도 26년1월1일에 생기겠지 그럼 비교를 해야하네
            -- 지금 시스템날짜랑 소멸예정일이 같아지면 계산하고 그에 해당하는 사원의 연차의 잔여연차가 미사용연차일수네
            -- 그럼 필요한 데이터가 소멸예정일, 잔여연차
            IF user_info.expiry_date <= SYSDATE THEN
                v_annual_leave := (user_info.remaining_days) * (v_ordinary_wage * 8);
            ELSE
                v_annual_leave := 0;
            END IF;
            DBMS_OUTPUT.PUT_LINE('v_annual_leave:'||v_annual_leave);
            
            -- 총 수당총액(기본급 미포함)
            v_total_allowance := v_overtime + v_night + v_holiday + v_family + v_meal + v_annual_leave;
            DBMS_OUTPUT.PUT_LINE('v_total_allowance:'||v_total_allowance);
            
            -- 총 지급액(기본급)
            v_total_payment_amount := v_sal + v_overtime + v_night + v_holiday + v_family + v_meal + v_annual_leave;
            DBMS_OUTPUT.PUT_LINE('v_total_payment_amount:'||v_total_payment_amount);
            
            -- 소득 := 근로소득 - 비과세근로소득
            v_earnings := v_total_payment_amount - v_meal;
            DBMS_OUTPUT.PUT_LINE('v_earnings:'||v_earnings);
            
            -- 국민연금
            -- 계산법 : 소득 * 0.045(4.5%)
            -- 1000원 미만은 제외(절사)하고 계산
            -- 예시) 15,123원 -> 15,000원
            v_national_pension := TRUNC(v_earnings, -3) * 0.045;
            DBMS_OUTPUT.PUT_LINE('v_national_pension:'||v_national_pension);
                        
            -- =================================================================
            -- 통상임금은 기본적으로 상여, 수당, 4대보험, 소득세, 지방소득에만 영향줌
            -- 실 수령액 - (결근 + 지각 + 병가)
            -- ① (기본금 / 해당 월의 전체 일수 or 30일고정 or 월의 유급일수) × 근로일수(주휴일 포함)
            -- ② (기본금 / 209시간) × 8시간 × 근로일수(주휴일 포함)
            -- 보통 사업자는 1번을 선호
            -- 이유는 돈 적게 나가서
            -- h1	0H	정상 x
            -- h2	0H	지각 x
            -- h3	0H	조퇴 x
            -- h4	0H	결근 o
            -- h5	0H	반차 x
            -- h6	0H	연차 x
            -- h7	0H	병가 o
            -- h8	0H	외근 x
            -- h9	0H	출장 x
            -- h10	0H	휴무 x
            --
            -- 결근에 대해서 계산
            -- =================================================================
            IF user_info.absenceCount > 0 THEN
                v_absence := ( v_sal / 30 ) * user_info.absenceCount;
            ELSE
                v_absence := 0;
            END IF;
        END IF;
        
        -- 고용보험
        -- 계산법 : 소득 * 0.009(0.9%)
        -- 1의자리수, 소수점은 제외
        v_employment_insurance := TRUNC((v_earnings * 0.009) , -1);
        DBMS_OUTPUT.PUT_LINE('v_employment_insurance:'||v_employment_insurance);
        
        -- 건강보험
        -- 계산법 : 소득 * 0.03545(3.545%)
        -- 1의자리수, 소수점은 제외
        v_health_insurance := TRUNC((v_earnings * 0.03545) , -1);
        DBMS_OUTPUT.PUT_LINE('v_health_insurance:'||v_health_insurance);
        
        -- 장기요양보험
        -- 계산법 : 소득 * 0.004591(0.4591%)
        -- 1의자리수, 소수점은 제외
        v_long_time_care_insurance := TRUNC((v_earnings * 0.004591) , -1);
        DBMS_OUTPUT.PUT_LINE('v_long_time_care_insurance:'||v_long_time_care_insurance);
        
        --=======
        -- 소득세
        --=======
        -- ???
                
        -- 지방소득세
        -- 계산법 : 소득세 * 0.1
        
        -- 총 공제총액
        -- 총 4대보험 := 국민연금 + 고용보험 + 건강보험 + 장기요양보험 + 결근병가공제
        v_total_deduction_amount := v_national_pension + v_employment_insurance + v_health_insurance + v_long_time_care_insurance + v_absence;
        DBMS_OUTPUT.PUT_LINE('v_total_deduction_amount:'||v_total_deduction_amount);
        
        -- 실 수령액
        v_net_pay := v_total_payment_amount - v_total_deduction_amount;
        DBMS_OUTPUT.PUT_LINE('v_net_pay:'||v_net_pay);
        
        -- 3) 계산 완료 후 결과를 컬렉션에 추가
        v_results.EXTEND; 
        -- 컬렉션의 크기를 1만큼 늘려서 새로운 빈 공간을 만듬
        -- 새로운 배열 칸을 추가한다고 생각하면 됨
        
        v_results(v_results.LAST) := T_PAYROLL_RESULT_REC(
        -- v_results(v_results.LAST) 방금 .EXTEND로 추가된 마지막 위치를 가리킴
        -- T_PAYROLL_RESULT_REC 타입의 레코드를 생성
            user_info.payroll_code,
            v_uid, 
            user_info.user_name,
            user_info.dept_name,
            user_info.payroll_date,
            v_sal, 
            v_bonus, 
            v_overtime, 
            v_night, 
            v_holiday, 
            v_family, 
            v_meal, 
            v_annual_leave, 
            v_total_allowance, 
            v_total_payment_amount, 
            v_national_pension, 
            v_employment_insurance, 
            v_health_insurance, 
            v_long_time_care_insurance, 
            v_total_deduction_amount, 
            v_net_pay,
            v_absence
        );
        
    END LOOP;
    -- 4) 커서 종료
    
    -- 계산된 컬렉션 데이터를 Ref Cursor에 담아 외부에 반환
    -- 이 부분은 프로시저 내부의 PL/SQL 데이터(v_results 컬렉션)을 외부SQL환경으로 전달하기 위한 형식 변환 및 반환 과정
    -- 외부시스템은 SQL결과 집합(SELECT 쿼리의 결과) 형태로 데이터를 받아야 함
    OPEN p_out_results FOR
        SELECT payroll_code,
               user_id,
               user_name,
               dept_name,
               payroll_date,
               salary,
               bonus,
               overtime,
               night,
               holiday,
               family,
               meal,
               annual_leave,
               total_allowance,
               total_payment_amount,
               national_pension,
               employment_insurance,
               health_insurance,
               long_time_care_insurance,
               total_deduction_amount,
               net_pay,
               absence
        FROM TABLE(v_results);
        -- TABLE()함수는 타입 컬렉션(v_results)을 마치 데이터베이스의 일반적인 테이블처럼 SQL문에서 조회 할 수 있도록 변환해 주는 특수 함수
        -- 정확히는 컬렉션을 관계형 테이블로 변환하는 기능
    -- 원레는 CLOSE CURSOR를 해줘야하는데 하면은 외부로 전달될 데이터 통로를 닫아버리는 것임
    -- CLOSE CURSOR를 JAVA에서 닫아줘야함
           
END;
/

SET serveroutput ON; -- dbms_output하기전에 무조건 해야하는거
-- 프로시저 실행하는구문
EXEC sp_calculate_payroll('PRP25113000001');
SELECT *
FROM   tb_user_master;
    
SELECT *
FROM   tb_attendance;
SELECT *
FROM   tb_annual_leave;

-- ========================================
-- TB_ATTENDANCE 샘플 데이터
-- ========================================
-- TB_ATTENDANCE 테이블에 삽입할 10개의 샘플 데이터입니다.

-- 1. 김사원 (EMP25010200001): 2025-12-02, 정상 근무 (9시간 근무, 1시간 휴게 제외 -> 8시간 총 근무)
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251201', 'EMP25010200001', TO_DATE('20251202', 'YYYYMMDD'), '정상근무',
    TO_DATE('20251202 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251202 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 2. 김사원 (EMP25010200001): 2025-12-03, 연장 근무 (18시 퇴근 후 2시간 연장) -> 총 연장 2.00
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251202', 'EMP25010200001', TO_DATE('20251203', 'YYYYMMDD'), '연장근무',
    TO_DATE('20251203 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251203 20:00', 'YYYYMMDD HH24:MI'), 
    2.00, 0.00, 0.00, 
    10.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 3. 박과장 (EMP23030100003): 2025-12-05, 정상 근무
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251203', 'EMP23030100003', TO_DATE('20251205', 'YYYYMMDD'), '정상근무',
    TO_DATE('20251205 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251205 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 4. 박과장 (EMP23030100003): 2025-12-07 (일요일), 야간 근무 (22:00 ~ 익일 06:00, 야간 8시간 가정) -> 총 야간 8.00
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251204', 'EMP23030100003', TO_DATE('20251207', 'YYYYMMDD'), '야간근무',
    TO_DATE('20251207 22:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251208 06:00', 'YYYYMMDD HH24:MI'), 
    0.00, 8.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 5. 최부장 (EMP19100100004): 2025-12-13 (토요일), 휴일 근무 (8시간) -> 총 휴일 8.00
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251205', 'EMP19100100004', TO_DATE('20251213', 'YYYYMMDD'), '휴일근무',
    TO_DATE('20251213 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251213 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 8.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 6. 정주임 (EMP25041000005): 2025-12-15, 정상 근무
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251206', 'EMP25041000005', TO_DATE('20251215', 'YYYYMMDD'), '정상근무',
    TO_DATE('20251215 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251215 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 7. 정주임 (EMP25041000005): 2025-12-17, 연장+야간 근무 (18시 퇴근, 22시까지 연장(4h), 22시~24시 야간(2h)) -> 연장 4.00, 야간 2.00
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251207', 'EMP25041000005', TO_DATE('20251217', 'YYYYMMDD'), '연장/야간근무',
    TO_DATE('20251217 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251218 00:00', 'YYYYMMDD HH24:MI'), 
    4.00, 2.00, 0.00, 
    14.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 8. 문과장 (EMP22090100007): 2025-12-19, 정상 근무
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251208', 'EMP22090100007', TO_DATE('20251219', 'YYYYMMDD'), '정상근무',
    TO_DATE('20251219 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251219 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 9. 한대리 (EMP23112000008): 2025-12-23, 정상 근무
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251209', 'EMP23112000008', TO_DATE('20251223', 'YYYYMMDD'), '정상근무',
    TO_DATE('20251223 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251223 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 0.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 10. 윤사원 (EMP25070100006): 2025-12-25 (크리스마스), 휴일 근무 (8시간) -> 총 휴일 8.00
INSERT INTO TB_ATTENDANCE (
    COMPANY_CODE, ATTEN_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, 
    IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, 
    TOTAL_WORK_TIME, CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE
) VALUES (
    'ROOT', 'ATN251210', 'EMP25070100006', TO_DATE('20251225', 'YYYYMMDD'), '휴일근무',
    TO_DATE('20251225 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251225 18:00', 'YYYYMMDD HH24:MI'), 
    0.00, 0.00, 8.00, 
    8.00, 'ADMIN', SYSDATE, 'ADMIN', SYSDATE
);

-- 커밋 (선택 사항이지만 테스트 시 반영을 위해 권장)
-- COMMIT;

-- TB_PAYROLL 테이블에 삽입할 샘플 데이터 (2025년 10월 급여 대장)
-- p_payroll_period_code: 'PAY2510_MONTHLY'

-- 급여 대장 기간 설정
-- 시작일: 2025-10-01, 종료일: 2025-10-31, 지급일: 2025-11-10

-- 1. 김사원 (EMP25010200001)
INSERT INTO TB_PAYROLL (
    PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_TYPE, 
    BONUS_METHOD, BONUS_RATE, BONUS_AMOUNT, PAYROLL_NAME, PAYROLL_START_DATE, 
    PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_PERIOD_CODE
) VALUES (
    'PRL2510001', 'ROOT', 'EMP25010200001', '202510', '월급',
    '없음', 0, 0.00, '2025년 10월 월급 - 김사원', TO_DATE('20251001', 'YYYYMMDD'), 
    TO_DATE('20251031', 'YYYYMMDD'), TO_DATE('20251110', 'YYYYMMDD'), 'PAY2510_MONTHLY'
);

-- 2. 박과장 (EMP23030100003)
INSERT INTO TB_PAYROLL (
    PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_TYPE, 
    BONUS_METHOD, BONUS_RATE, BONUS_AMOUNT, PAYROLL_NAME, PAYROLL_START_DATE, 
    PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_PERIOD_CODE
) VALUES (
    'PRL2510003', 'ROOT', 'EMP23030100003', '202510', '월급',
    '없음', 0, 0.00, '2025년 10월 월급 - 박과장', TO_DATE('20251001', 'YYYYMMDD'), 
    TO_DATE('20251031', 'YYYYMMDD'), TO_DATE('20251110', 'YYYYMMDD'), 'PAY2510_MONTHLY'
);

-- 3. 최부장 (EMP19100100004)
INSERT INTO TB_PAYROLL (
    PAYROLL_CODE, COMPANY_CODE, USER_ID, PAYROLL_PERIOD, PAYROLL_TYPE, 
    BONUS_METHOD, BONUS_RATE, BONUS_AMOUNT, PAYROLL_NAME, PAYROLL_START_DATE, 
    PAYROLL_END_DATE, PAYROLL_DATE, PAYROLL_PERIOD_CODE
) VALUES (
    'PRL2510004', 'ROOT', 'EMP19100100004', '202510', '월급',
    '없음', 0, 0.00, '2025년 10월 월급 - 최부장', TO_DATE('20251001', 'YYYYMMDD'), 
    TO_DATE('20251031', 'YYYYMMDD'), TO_DATE('20251110', 'YYYYMMDD'), 'PAY2510_MONTHLY'
);

-- 커밋 (테스트 시 데이터 반영을 위해 권장)
-- COMMIT;

SELECT *
FROM   tb_payroll;
SELECT *
FROM   tb_attendance;

-- TB_ATTENDANCE 테이블에 삽입할 샘플 데이터 (2025년 10월)
-- 5명의 사원에 대해 정상, 연장, 야간, 휴일 근무를 포함

-- 1. 김사원 (EMP25010200001) - 정상 근무
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100001', 'ROOT', 'EMP25010200001', TO_DATE('20251001', 'YYYYMMDD'), '정상', 
    TO_DATE('20251001 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251001 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100002', 'ROOT', 'EMP25010200001', TO_DATE('20251002', 'YYYYMMDD'), '정상', 
    TO_DATE('20251002 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251002 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);

-- 2. 이대리 (EMP24051500002) - 연장 근무 포함
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100003', 'ROOT', 'EMP24051500002', TO_DATE('20251006', 'YYYYMMDD'), '정상', 
    TO_DATE('20251006 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251006 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100004', 'ROOT', 'EMP24051500002', TO_DATE('20251007', 'YYYYMMDD'), '정상/연장', 
    TO_DATE('20251007 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251007 21:00', 'YYYYMMDD HH24:MI'), 3.00, 0.00, 0.00, 11.00); -- 18시 이후 3시간 연장

-- 3. 박과장 (EMP23030100003) - 야간 근무 포함
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100005', 'ROOT', 'EMP23030100003', TO_DATE('20251008', 'YYYYMMDD'), '정상/연장/야간', 
    TO_DATE('20251008 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251008 23:00', 'YYYYMMDD HH24:MI'), 5.00, 1.00, 0.00, 13.00); -- 18시 이후 5시간 연장, 22시 이후 1시간 야간
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100006', 'ROOT', 'EMP23030100003', TO_DATE('20251010', 'YYYYMMDD'), '정상', 
    TO_DATE('20251010 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251010 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);

-- 4. 최부장 (EMP19100100004) - 휴일 근무 포함 (10/3 개천절)
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100007', 'ROOT', 'EMP19100100004', TO_DATE('20251001', 'YYYYMMDD'), '정상', 
    TO_DATE('20251001 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251001 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100008', 'ROOT', 'EMP19100100004', TO_DATE('20251003', 'YYYYMMDD'), '휴일근무', 
    TO_DATE('20251003 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251003 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 8.00, 8.00); -- 10/3 (금) 개천절, 8시간 휴일 근무

-- 5. 정주임 (EMP25041000005) - 연차/휴무 포함
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100009', 'ROOT', 'EMP25041000005', TO_DATE('20251013', 'YYYYMMDD'), '정상', 
    TO_DATE('20251013 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251013 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100010', 'ROOT', 'EMP25041000005', TO_DATE('20251014', 'YYYYMMDD'), '연차', 
    NULL, NULL, 0.00, 0.00, 0.00, 0.00); -- 연차 사용
INSERT INTO TB_ATTENDANCE (ATTEN_CODE, COMPANY_CODE, USER_ID, WORK_DATE, ATTEND_TYPE, IN_TIME, OUT_TIME, OVER_WORK_TIME, NIGHT_WORK_TIME, HOLIDAY_WORK_TIME, TOTAL_WORK_TIME) 
VALUES ('ATT25100011', 'ROOT', 'EMP25041000005', TO_DATE('20251015', 'YYYYMMDD'), '정상', 
    TO_DATE('20251015 09:00', 'YYYYMMDD HH24:MI'), TO_DATE('20251015 18:00', 'YYYYMMDD HH24:MI'), 0.00, 0.00, 0.00, 8.00);
    
-- COMMIT;

-- ==================================
-- 2025-12-03
-- ==================================
-- 근태관리 어떻게 이루어지는지 확인해봐야 할듯, 결근관련해서 어떻게 관리되는지
-- 매월 1일에 급여대장테이블에 전에 달의 급여대장이 저장되도록(5월1일은 4월급여대장이 만들어지도록)

-- 매월 1일 급여 대장 생성 프로시저
create or replace PROCEDURE sp_insert_payroll_pay 
/* ==========================================
 * 프로시저명 : sp_insert_payroll_pay
 * 설명 : 매월 1일 급여 대장 생성 프로시저
 * 작성자 : 장준현
 * 작성일 : 25.12.04
 * 수정이력 : 프로시저안에 있는 코드값 생성 함수로 변경
    * 수정일 : 25.12.05 11:55
 * ========================================== */
IS
    -- 회사 조회 커서
    CURSOR payroll_cursor IS -- 커서선언
        SELECT company_code
        FROM   tb_company_master;
        
    -- 회사에 해당하는 사원 조회 커서
    CURSOR user_cursor (p_company_code VARCHAR2) IS
        SELECT user_id
        FROM   tb_user_master
        WHERE  leave_date IS NULL
          AND  company_code = p_company_code;

    v_payroll_start_date DATE;
    v_payroll_period VARCHAR2(20);
    v_payroll_type VARCHAR2(20);
    v_payroll_name VARCHAR2(100);
    v_payroll_end_date DATE;
    v_payroll_date DATE;
    v_payroll_period_code VARCHAR2(20);
BEGIN
    -- payroll_start_date 대장기간 시작일
    v_payroll_start_date := TRUNC(ADD_MONTHS(SYSDATE, -1),'MM'); -- 월의 첫날로 잘라내기
    -- payroll_period 귀속연월
    v_payroll_period := TO_CHAR(v_payroll_start_date, 'YYYY-MM');
    -- payroll_type 급여구분
    v_payroll_type := 'j1';
    -- payroll_name 대장명칭
    v_payroll_name := TO_CHAR(v_payroll_start_date, 'yyyy"년" MM"월"') || '정기급여';
    -- payroll_end_date 대장기간종료일
    v_payroll_end_date := LAST_DAY(ADD_MONTHS(SYSDATE, -1));
    -- payroll_date 지급일
    v_payroll_date := TRUNC(SYSDATE, 'MM') + 9;
    
    -- 회사 조회 커서 루프시작
    FOR payroll_info IN payroll_cursor LOOP
        
        -- 귀속코드생성
        v_payroll_period_code := fn_make_date_code('PAYROLL_PERIOD');
        
        -- 사원 조회 커서 루프시작
        FOR user_info IN user_cursor(payroll_info.company_code) LOOP
            
            -- 급여대장에 INSERT
            INSERT INTO tb_payroll (
                payroll_code, -- 급여대장코드 함수
                company_code, -- 회사코드
                user_id, -- 사원번호
                payroll_period, -- 귀속연월
                payroll_type, -- j1 급여
                payroll_name, -- 급여대장명칭
                payroll_start_date, -- 대장기간시작일
                payroll_end_date, -- 대장기간종료일
                payroll_date, -- 지급일                            
                payroll_period_code -- 신고귀속코드 함수
            ) VALUES (fn_make_date_code('PAYROLL'),
                      payroll_info.company_code,
                      user_info.user_id,
                      v_payroll_period,
                      v_payroll_type,
                      v_payroll_name,
                      v_payroll_start_date,
                      v_payroll_end_date,
                      v_payroll_date,
                      v_payroll_period_code
            );
        END LOOP;
    END LOOP;

     -- 5. 트랜잭션 처리 및 결과 출력
    COMMIT;
    
END;
/

-- TB_COMPANY_MASTER 테이블에 샘플 데이터 1건을 삽입하는 쿼리입니다.
INSERT INTO tb_company_master (
    COMPANY_CODE,
    COMPANY_NAME,
    CEO_NAME,
    CEO_PHONE,
    COMPANY_EMAIL,
    INDUSTRY_TYPE,
    BUSINESS_TYPE,
    MANAGER_NAME,
    EMPLOYEE_COUNT,
    BNO,
    CHECK_NO,
    COMPANY_ADDRESS,
    COMPANY_PHONE,
    MANAGER_PHONE
    -- CREATED_BY, CREATE_DATE, UPDATED_BY, UPDATE_DATE는 기본값(DEFAULT)을 사용합니다.
) VALUES (
    '0000', -- 회사코드
    '테스트 솔루션즈', -- 회사명
    '김대표', -- 대표자명
    '010-1234-5678', -- 대표자 휴대폰번호
    'test@solution.com', -- 회사이메일
    '소프트웨어 개발 및 공급', -- 업종
    '서비스', -- 업태
    '박담당', -- 담당자 명
    50, -- 직원 수
    '123-45-67890', -- 사업자등록번호
    '1234567890', -- 인증번호 (가정)
    '서울특별시 강남구 테헤란로 123', -- 회사 주소
    '02-9876-5432', -- 회사 전화번호
    '010-2468-1357' -- 담당자 연락처
);

COMMIT; -- 변경사항을 확정합니다.

CREATE SEQUENCE QUIZ.TB_PAYROLL_SEQ START WITH 1
INCREMENT BY 1       
NOCACHE; 

-- 급여 생성 함수
create or replace FUNCTION FN_MAKE_USER_PAY_MANAGEMENT
RETURN VARCHAR2
IS
    v_date          VARCHAR2(8);
    v_max           VARCHAR2(50);
    v_seq_num       NUMBER;
    v_new_seq       VARCHAR2(5);
    v_user_pay_management_code  VARCHAR2(50);
BEGIN
    -- 오늘 날짜
    v_date := TO_CHAR(SYSDATE, 'YYMMDD');

    SELECT MAX(user_pay_management_code)
      INTO v_max
      FROM tb_user_pay_management
     WHERE user_pay_management_code LIKE 'PMC' || v_date || '%';

    IF v_max IS NULL THEN
        v_new_seq := '00001';
    ELSE
        v_seq_num := TO_NUMBER(SUBSTR(v_max, 12, 5)) + 1;
        v_new_seq := LPAD(v_seq_num, 5, '0');
    END IF;

    v_user_pay_management_code := 'PMC' || v_date || v_new_seq;
    RETURN v_user_pay_management_code;
END;
/

-- 급여대장-계산하기모달창-확정버튼 
INSERT INTO tb_user_pay_management (
    user_pay_management_code,
    payroll_code,
    company_code,
    user_id,
    pay_period,
    pay_date,
    salary,
    bonus,
    overtime_allowance,
    night_allowance,
    holiday_allowance,
    family_allowance,
    meal_allowance,
    annual_leave_allowance,
    total_allowance,
    total_payment,
    income_tax,
    national_pension,
    employment_insurance,
    health_insurance,
    long_time_care_insurance,
    local_income_tax,
    total_deduction,
    net_pay
) VALUES (
    FN_MAKE_USER_PAY_MANAGEMENT,
    #{payroll_code},
    (SELECT company_code
     FROM   tb_user_master
     WHERE  user_id = #{user_id}) as company_code,
    #{user_id},
    (SELECT payroll_period
     FROM   tb_payroll
     WHERE  payroll_code = #{payroll_code}) as pay_period,
    #{pay_date},
    #{salary},
    #{bonus},
    #{overtime_allowance},
    #{night_allowance},
    #{holiday_allowance},
    #{family_allowance},
    #{meal_allowance},
    #{annual_leave_allowance},
    #{total_allowance},
    #{total_payment},
    #{income_tax},
    #{national_pension},
    #{employment_insurance},
    #{health_insurance},
    #{long_time_care_insurance},
    #{local_income_tax},
    #{total_deduction},
    #{net_pay}
);

-- 급여 대장 목록 수정중
-- 지급총액 추가중...
-- tb_user_pay_management테이블에서 total_allowance를 다 합해야하는데 조건이 있음!
-- 조건은 
SELECT   pr.payroll_period,
         cc.code_name AS payroll_type,
         pr.payroll_name,
         pr.payroll_date,
         pr.create_date,
         pr.payroll_period_code,
         COUNT(*) AS peopleNumber,
         SUM(upm.total_payment) AS totalPayment -- 지급총액
FROM     tb_payroll pr
         JOIN tb_cm_code cc
         ON pr.payroll_type = cc.code
         LEFT JOIN tb_user_pay_management upm
         ON upm.payroll_code = pr.payroll_code
GROUP BY pr.payroll_period, 
         cc.code_name,
         pr.payroll_name, 
         pr.payroll_date, 
         pr.create_date,
         pr.payroll_period_code; 
         
-- ==================
-- 20251205
-- ==================

-- 급여관리-사원급여조회
SELECT upm.user_pay_management_code,
			   upm.payroll_code,
			   upm.company_code,
			   upm.user_id,
			   um.user_name,
			   dm.dept_name,
			   upm.pay_date, 
			   upm.salary,
			   upm.bonus,
			   upm.overtime_allowance AS overtime,
			   upm.night_allowance AS night,
			   upm.holiday_allowance AS holiday,
			   upm.family_allowance AS family,
			   upm.meal_allowance AS meal,
			   upm.annual_leave_allowance AS annual_leave,
 			   upm.total_allowance ,
 			   upm.total_payment AS total_payment_amount,
			   upm.income_tax,
			   upm.national_pension,
			   upm.employment_insurance,
			   upm.health_insurance,
			   upm.long_time_care_insurance,
			   upm.local_income_tax,
			   upm.total_deduction AS total_deduction_amount,
			   upm.net_pay
		FROM   tb_user_pay_management upm
			   JOIN tb_user_master um 
			   ON upm.user_id = um.user_id
			   JOIN tb_dept_master dm 
			   ON um.dept = dm.dept_code;
               
SELECT user_pay_management_code
		FROM   tb_user_pay_management;
			   JOIN tb_user_master um 
			   ON upm.user_id = um.user_id
			   JOIN tb_dept_master dm 
			   ON um.dept = dm.dept_code;
               
-- 급여대장-상여등록-신고귀속코드-생성
INSERT INTO TB_CM_CODE
(CODE, GROUP_CODE, CODE_NAME, ATTRIBUTE01, ATTRIBUTE02, REMARK)
VALUES
('PAYROLL_PERIOD','MAKE_CODE','신고귀속코드','TB_PAYROLL','PRP', 'PAYROLL_PERIOD_CODE');

-- =============================================
-- 요게 코드 실행하는 것들
-- 위에꺼가 오늘날짜를 포함 한거고
-- 아래꺼는 오늘날자를 포함 안한거고
-- =============================================
select fn_make_date_code('PAYROLL') from dual;
select fn_make_date_code('PAYROLL_PERIOD') from dual;
select fn_make_code('PAYROLL_PERIOD') from dual;

-- 사원급여관리-급여관리코드-생성
INSERT INTO TB_CM_CODE
(CODE, GROUP_CODE, CODE_NAME, ATTRIBUTE01, ATTRIBUTE02, REMARK)
VALUES
('USER_PAY_MANAGEMENT','MAKE_CODE','급여관리코드','TB_USER_PAY_MANAGEMENT','UPM', 'USER_PAY_MANAGEMENT_CODE');
-- 생성 결과
select fn_make_date_code('USER_PAY_MANAGEMENT') from dual;

-- 사원급여관리-단건조회-pdf출력-급여명세서에 필요한 데이터들 select
SELECT upm.user_pay_management_code,
       um.user_name,
       upm.user_id,
       dm.dept_name,
       cmc.code_name,
       upm.salary,
       upm.bonus,
       upm.overtime_allowance AS overtime,
       upm.night_allowance AS night,
       upm.holiday_allowance AS holiday,
       upm.family_allowance AS family,
       upm.meal_allowance AS meal,
       upm.annual_leave_allowance AS annualLeave,
       upm.total_payment AS totalPaymentAmount,
       upm.national_pension,
       upm.employment_insurance,
       upm.health_insurance,
       upm.long_time_care_insurance,
       upm.total_deduction AS totalDeductionAmount,
       upm.net_pay,
       upm.payroll_code,
       upm.company_code,
       upm.pay_period
FROM   tb_user_pay_management upm
       JOIN tb_user_master um 
       ON upm.user_id = um.user_id
       JOIN tb_dept_master dm 
       ON um.dept = dm.dept_code
       JOIN tb_cm_code cmc
       ON um.job_title = cmc.code;
       
-- h2 지각에 대한 결근일         
SELECT SUM(8 - (att_sub.total_work_time - (att_sub.over_work_time + att_sub.night_work_time + att_sub.holiday_work_time))),
       att_sub.user_id
FROM   tb_attendance att_sub
       JOIN tb_payroll pr_sub
       ON att_sub.user_id = pr_sub.user_id
WHERE  pr_sub.payroll_period_code = 'PRP25120500002'
  AND  att_sub.attend_type = 'h2'
GROUP BY att_sub.user_id;

-- h4 결근에 대한 몇일인지
SELECT att_sub.user_id,
       COUNT(*) as absenceCount
FROM   tb_attendance att_sub
       JOIN tb_payroll pr_sub
       ON att_sub.user_id = pr_sub.user_id
WHERE  pr_sub.payroll_period_code = 'PRP25120500002'
  AND  att_sub.work_date >= pr_sub.payroll_start_date -- work_date일한시간이 대장시작시간보다 크거나 같은거
  AND  att_sub.work_date <= pr_sub.payroll_end_date
  AND (attend_type = 'h4' OR  attend_type = 'h7')
GROUP BY att_sub.user_id ;

SELECT pr.payroll_code, -- 급여대장코드
               pr.user_id, -- 사용자 id
               um.user_name, -- 성명
               dm.dept_name, -- 부서명
               pr.payroll_date, -- 지급일             
               um.salary, -- 기본급여
               pr.bonus_rate, -- 상여 지급율
               pr.bonus_amount, -- 상여 지급액
               ad.total_over_work_time, -- 총 연장근무시간
               ad.total_night_work_time, -- 총 야간근무시간
               ad.total_holiday_work_time, -- 총 휴일근무시간
               um.family_count, -- 가족수
               um.children_count, -- 자녀수
               al.expiry_date, -- 소멸예정일
               al.remaining_days, -- 잔여연차
               pr.payroll_start_date, -- 대장시작시간               
               absence.absenceCount
        FROM   tb_payroll pr
               JOIN tb_user_master um -- 사원관리 테이블
               ON pr.user_id = um.user_id
               LEFT JOIN ( SELECT ad_sub.user_id,
                                  SUM(ad_sub.over_work_time) as total_over_work_time,
                                  SUM(ad_sub.night_work_time) as total_night_work_time,
                                  SUM(ad_sub.holiday_work_time) as total_holiday_work_time
                           FROM   tb_attendance ad_sub -- 근태관리 테이블
                                  JOIN tb_payroll pr_sub -- 급여대장 테이블
                                  ON ad_sub.user_id = pr_sub.user_id
                           WHERE  pr_sub.payroll_period_code = 'PRP25120500002' -- 매개변수로 받은 신고귀속코드랑 급여대장에있는 신고귀속코드가 같은걸 찾고
                           AND    ad_sub.work_date >= pr_sub.payroll_start_date -- work_date일한시간이 대장시작시간보다 크거나 같은거
                           AND    ad_sub.work_date <= pr_sub.payroll_end_date -- work_date일한시간이 대장종료시간보다 작거나 같은거
                           GROUP BY ad_sub.user_id -- 이렇게 하면 몇월에 대한 급여인지 알게됨
                          ) ad 
                ON ad.user_id = pr.user_id
                LEFT JOIN tb_annual_leave al
                ON al.user_id = pr.user_id
                JOIN tb_dept_master dm
                ON dm.dept_code = um.dept
                LEFT JOIN ( SELECT att_sub.user_id,
                              COUNT(*) as absenceCount
                       FROM   tb_attendance att_sub
                              JOIN tb_payroll pr_sub
                              ON att_sub.user_id = pr_sub.user_id
                       WHERE  pr_sub.payroll_period_code = 'PRP25120500002'
                         AND  att_sub.work_date >= pr_sub.payroll_start_date -- work_date일한시간이 대장시작시간보다 크거나 같은거
                         AND  att_sub.work_date <= pr_sub.payroll_end_date
                         AND  (attend_type = 'h4' OR  attend_type = 'h7')
                       GROUP BY att_sub.user_id ) absence
                ON pr.user_id = absence.user_id
         WHERE  pr.payroll_period_code = 'PRP25120500002';
         
-- ==========
-- 2025-12-08
-- ==========
SELECT *
FROM   tb_annual_leave;
SELECT *
FROM   tb_annual_leave_detail;
SELECT *
FROM   tb_user_master;
SELECT *
FROM   tb_cm_code;
SELECT *
FROM   tb_cm_code_group;
SELECT *
FROM   tb_attendance;
INSERT INTO tb_cm_code_group(group_code, group_name)
VALUES ('0B', '연차신청구분');
INSERT INTO tb_cm_code (code, group_code, code_name)
VALUES ('b1', '0B', '연차');
INSERT INTO tb_cm_code (code, group_code, code_name)
VALUES ('b2', '0B', '반차');
UPDATE tb_cm_code
SET code_name = '연차'
WHERE code_name = '연차신청구분';
UPDATE tb_annual_leave_detail
SET leave_type = 'b1'
WHERE leave_type = '연차';
UPDATE tb_annual_leave_detail
SET leave_type = 'b2'
WHERE leave_type = '반차';

-- 사원연차조회
SELECT ald.annual_leave_code, -- 연차관리코드
       ald.annual_leave_detail_seq, -- 연차상세관리번호
       al.user_id, -- 사번
       um.user_name, -- 성명
       dm.dept_name, -- 부서명
       cmcj.code_name as job_title, -- 직위직급
       cmcl.code_name as leave_type, -- 신청구분
       ald.used_days, -- 사용일수
       ald.leave_start_date, -- 연차시작일
       ald.leave_end_date, -- 연차종료일
       ald.leave_apply_date, -- 연차신청일
       ald.rm -- 사유
FROM   tb_annual_leave_detail ald
       JOIN tb_annual_leave al
       ON al.annual_leave_code = ald.annual_leave_code
       JOIN tb_user_master um
       ON um.user_id = al.user_id
       JOIN tb_dept_master dm
       ON dm.dept_code = um.dept
       JOIN tb_cm_code cmcj
       ON cmcj.code = um.job_title
       JOIN tb_cm_code cmcl
       ON cmcl.code = ald.leave_type
WHERE  al.user_id = 'EMP23030100003';
       
-- tb_annual_leave 샘플데이터
INSERT INTO TB_ANNUAL_LEAVE (ANNUAL_LEAVE_CODE, COMPANY_CODE, USER_ID, GRANT_YEAR, TOTAL_GRANT_DAYS, TOTAL_USED_DAYS, REMAINING_DAYS, EXPIRY_DATE)
VALUES ('AL24_001', '0000', 'EMP25001', '2024', 15, 15, 0, TO_DATE('2024-12-31', 'YYYY-MM-DD'));

INSERT INTO TB_ANNUAL_LEAVE (ANNUAL_LEAVE_CODE, COMPANY_CODE, USER_ID, GRANT_YEAR, TOTAL_GRANT_DAYS, TOTAL_USED_DAYS, REMAINING_DAYS, EXPIRY_DATE)
VALUES ('AL25_001', '0000', 'EMP25001', '2025', 16, 4.5, 11.5, TO_DATE('2025-12-31', 'YYYY-MM-DD'));

-- tb_annual_leave_detail 샘플데이터
INSERT ALL
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (1, 'AL24_001', '0000', '연차', 1.0, TO_DATE('2024-03-05', 'YYYY-MM-DD'), TO_DATE('2024-03-05', 'YYYY-MM-DD'), TO_DATE('2024-03-01', 'YYYY-MM-DD'), '개인사정', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (2, 'AL24_001', '0000', '연차', 3.0, TO_DATE('2024-07-20', 'YYYY-MM-DD'), TO_DATE('2024-07-22', 'YYYY-MM-DD'), TO_DATE('2024-07-10', 'YYYY-MM-DD'), '여름휴가', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (3, 'AL24_001', '0000', '오전반차', 0.5, TO_DATE('2024-09-10', 'YYYY-MM-DD'), TO_DATE('2024-09-10', 'YYYY-MM-DD'), TO_DATE('2024-09-01', 'YYYY-MM-DD'), '병원진료', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (4, 'AL24_001', '0000', '연차', 10.5, TO_DATE('2024-12-15', 'YYYY-MM-DD'), TO_DATE('2024-12-25', 'YYYY-MM-DD'), TO_DATE('2024-12-01', 'YYYY-MM-DD'), '연말휴가', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (5, 'AL25_001', '0000', '연차', 1.0, TO_DATE('2025-01-02', 'YYYY-MM-DD'), TO_DATE('2025-01-02', 'YYYY-MM-DD'), TO_DATE('2024-12-20', 'YYYY-MM-DD'), '신년휴식', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (6, 'AL25_001', '0000', '오후반차', 0.5, TO_DATE('2025-02-14', 'YYYY-MM-DD'), TO_DATE('2025-02-14', 'YYYY-MM-DD'), TO_DATE('2025-02-01', 'YYYY-MM-DD'), '가사정리', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (7, 'AL25_001', '0000', '연차', 2.0, TO_DATE('2025-05-02', 'YYYY-MM-DD'), TO_DATE('2025-05-03', 'YYYY-MM-DD'), TO_DATE('2025-04-20', 'YYYY-MM-DD'), '샌드위치데이', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
INTO TB_ANNUAL_LEAVE_DETAIL VALUES (8, 'AL25_001', '0000', '연차', 1.0, TO_DATE('2025-08-01', 'YYYY-MM-DD'), TO_DATE('2025-08-01', 'YYYY-MM-DD'), TO_DATE('2025-07-20', 'YYYY-MM-DD'), '개인휴무', 'ADMIN', SYSDATE, 'ADMIN', SYSDATE)
SELECT * FROM DUAL;

-- 연차현황조회
SELECT al.annual_leave_code,
       al.company_code,
       al.user_id,
       al.grant_year,
       al.total_grant_days,
       al.total_used_days,
       al.remaining_days,
       al.expiry_date,       
       um.user_name,
       dm.dept_name,
       ccj.code_name as job_title
FROM   tb_annual_leave al
       JOIN tb_user_master um
       ON um.user_id = al.user_id
       JOIN tb_dept_master dm
       ON dm.dept_code = um.dept
       JOIN tb_cm_code ccj
       ON ccj.code = um.job_title
WHERE  grant_year = TO_CHAR(sysdate, 'yyyy');

-- insert연차신청
INSERT INTO tb_annual_leave_detail(annual_leave_code,
                                   company_code,
                                   leave_type,
                                   used_days,
                                   leave_start_date,
                                   leave_end_date,
                                   leave_apply_date,
                                   rm)
VALUES ((SELECT annual_leave_code
         FROM   tb_annual_leave
         WHERE  user_id = #{userId}),
         '0000',
         #{leaveType},
         #{usedDays},
         #{annualStartDate},
         #{annualEndDate},
         #{rm});
  
-- 공통코드 테이블 조회
SELECT *
FROM   tb_cm_code;

-- TB_ATTENDANCE 근태코드
INSERT INTO tb_cm_code(code, group_code, code_name, ATTRIBUTE01, ATTRIBUTE02, REMARK)
VALUES ('ATTEN','MAKE_CODE','근태코드','TB_ATTENDANCE','ATN', 'ATTEN_CODE');

-- 근태코드 실행
SELECT fn_make_date_code('ATTEN') FROM DUAL;
                                 
/*
 * 연차등록하면서 근태도 등록
 * COMPANY_CODE	VARCHAR2(20 BYTE)	No		1	회사코드
 * ATTEN_CODE	VARCHAR2(20 BYTE)	No		2	근태코드
 * USER_ID	VARCHAR2(20 BYTE)	No		3	사원번호_FK
 * WORK_DATE	DATE	Yes		4	근무일자
 * ATTEND_TYPE	VARCHAR2(20 BYTE)	Yes		5	근태상태
 * IN_TIME	DATE	Yes		6	출근시간
 * OUT_TIME	DATE	Yes		7	퇴근시간
 * OVER_WORK_TIME	NUMBER(5,2)	Yes		8	연장근무시간
 * NIGHT_WORK_TIME	NUMBER(5,2)	Yes		9	야간근무시간
 * HOLIDAY_WORK_TIME	NUMBER(5,2)	Yes		10	휴일근무시간
 * TOTAL_WORK_TIME	NUMBER(5,2)	Yes		11	일일총근무시간
 * REMARK	VARCHAR2(2000 BYTE)	Yes		12	비고
 * CREATED_BY	VARCHAR2(20 BYTE)	Yes	"'ADMIN'	"	13	생성자
 * CREATE_DATE	DATE	Yes		14	생성날짜
 * UPDATED_BY	VARCHAR2(20 BYTE)	Yes	"'ADMIN'	"	15	수정자
 * UPDATE_DATE	DATE	Yes		16	수정날짜
 */
 
INSERT INTO tb_attendance(company_code,
                          atten_code,
                          user_id,
                          work_date,
                          attend_type,
                          in_time,
                          out_time)
VALUES (#{companyCode},
        fn_make_date_code('ATTEN'),
        #{userId},
        TO_DATE(#{leaveStartDate},'yyyy/mm/dd'}),
        #{leaveType},
        #{;
        
        select * from tb_user_master;
SELECT annual_leave_code
FROM   tb_annual_leave
WHERE  user_id = 'EMP23030100003'
  AND  grant_year = TO_CHAR(sysdate, 'yyyy');
                        
SELECT *
FROM   tb_cm_code;

SELECT a.code
FROM   tb_cm_code a
JOIN   tb_cm_code b ON a.code_name = SUBSTR(b.code_name, 3)
WHERE  a.group_code = '0H'
  AND  b.code = 'b2';
  
SELECT TO_CHAR(TO_DATE('2025-12-09' || ' 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') 
FROM dual;

SELECT TO_CHAR(leave_start_date, 'YYYY-MM-DD HH24:MI:SS'), TO_CHAR(leave_end_date, 'YYYY-MM-DD HH24:MI:SS') FROM tb_annual_leave_detail;

/* ===========
 * 2025-12-09
 * =========== */
SELECT *
FROM   tb_company_master;
SELECT *
FROM   tb_user_master;

-- tb_annual_leave 연차관리코드
INSERT INTO tb_cm_code(code, group_code, code_name, ATTRIBUTE01, ATTRIBUTE02, REMARK)
VALUES ('ANNUAL_LEAVE','MAKE_CODE','연차관리코드','TB_ANNUAL_LEAVE','ANL','ANNUAL_LEAVE_CODE');

-- 연차관리코드 실행
SELECT fn_make_date_code('ANNUAL_LEAVE') FROM DUAL;
/* =======================================
 * 매년 1월1일마다 경력별 연차가 생기는 프로시저
 * ======================================= */
CREATE OR REPLACE PROCEDURE sp_calculate_annual
/* =============================================
 * 프로시저명 : sp_calculate_annual
 * 설명 : 매년 1월1일마다 경력별 연차가 생기는 프로시저
 * 작성자 : 장준현
 * 작성일 : 25.12.09
 * 수정이력 :
    * 수정일 :
 * ============================================= */
IS
    -- 1) 커서정의
    /* ============================
     * 사원 조회
     * 퇴사한 사람은 연차가 생기면 안됨
     * ============================ */
    CURSOR annual_user_cursor(p_company_code VARCHAR2) IS
        SELECT user_id,
               hire_date
        FROM   tb_user_master
        WHERE  leave_date IS NULL
          AND  company_code = p_company_code;
    
    /* =======
     * 회사조회
     * ======= */
    CURSOR annual_company_cursor IS
        SELECT company_code
        FROM   tb_company_master;
    
    v_annual_year NUMBER; -- 근속연수
    v_annual_date NUMBER; -- 총연차일수
BEGIN   
    -- 2) 커서 실행
    -- 3) 데이터 확인 및 인출
    -- 회사, 사원들 연차 입력
    FOR annual_company_info IN annual_company_cursor LOOP
        FOR annual_user_info IN annual_user_cursor(annual_company_info.company_code) LOOP
            -- 근속연수 생성
            v_annual_year := TRUNC(MONTHS_BETWEEN(SYSDATE, annual_user_info.hire_date) / 12);
            
            -- 근속연수에 따라 연차 생성                  
            IF v_annual_year >= 1 THEN
                IF v_annual_year < 3 THEN
                    -- 1년, 2년 근속 시 (2, 3년차)
                    v_annual_date := 15;
                ELSE
                    -- 3년 이상 근속 시 (4년차부터)
                    -- (근속연수 - 1) / 2 의 몫을 구함 (3년차부터 가산 시작)
                    v_annual_date := 15 + TRUNC((v_annual_year - 1) / 2);
                    
                    -- 최대 연차 일수 25일 제한 (선택 사항이나 보통 포함)
                    IF v_annual_date > 25 THEN
                        v_annual_date := 25;
                    END IF;
                END IF;
            ELSE
                -- 1년 미만 (입사 1년차)는 정기 부여 대상이 아닙니다.
                v_annual_date := 0; 
            END IF;
             
            INSERT INTO tb_annual_leave 
                (annual_leave_code,
                 company_code,
                 user_id,
                 grant_year,
                 total_grant_days,
                 total_used_days,
                 remaining_days,
                 expiry_date)
            VALUES 
                (fn_make_date_code('ANNUAL_LEAVE'),
                 annual_company_info.company_code,
                 annual_user_info.user_id,
                 TO_CHAR(SYSDATE, 'yyyy'),
                 v_annual_date,
                 0,
                 v_annual_date,
                 TRUNC(ADD_MONTHS(SYSDATE, 12), 'yyyy'));
                 
        END LOOP;
    END LOOP;
    -- 4) 커서 종료
    COMMIT;
END;
/

/* =======
 * 부서조회
 * ======= */
    SELECT dept_code,
           company_code,
           dept_name,
           upper_dept_no,
           dept_level,
           start_date,
           end_date,
           status,
           dept_mng,
           rm
    FROM   tb_dept_master;
    
/* ================================
 * 부서코드 공통코드에 등록 및 함수 생성
 * ================================ */
INSERT INTO tb_cm_code(code, group_code, code_name, ATTRIBUTE01, ATTRIBUTE02, remark)
VALUES ('DEPT','MAKE_CODE','부서코드','TB_DEPT_MASTER','DPT','DEPT_CODE');
SELECT fn_make_code('DEPT') FROM DUAL;

UPDATE tb_dept_master
SET dept_code = fn_make_code('DEPT')
WHERE dept_code = 'D000';