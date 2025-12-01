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

-- 
SELECT   pr.payroll_period,
         cc.code_name AS payroll_type,
         pr.payroll_name,
         pr.payroll_date,
         pr.create_date,
         COUNT(*) AS peopleNumber
FROM     tb_payroll pr
         JOIN tb_cm_code cc
         ON pr.payroll_type = cc.code
GROUP BY pr.payroll_period, 
         cc.code_name,
         pr.payroll_name, 
         pr.payroll_date, 
         pr.create_date;
                 
SELECT *
FROM   tb_payroll;

--=========================================
--20251201
--=========================================
-- tb_payroll에 회사 코드 0000으로 수정
UPDATE tb_payroll
SET company_code = '0000';

-- 급여 계산 프로시저
CREATE OR REPLACE PROCEDURE sp_calculate_payroll(
    -- 어떤 대장을 급여계산할건지 선언
    p_payroll_period_code IN VARCHAR2 -- 계산할 대장 코드
)
IS
    -- 1) 커서 정의
    -- 위에서 선언한 대장에 해당하는 사원조회
    CURSOR user_cursor IS -- 커서 선언
        SELECT pr.user_id, -- 사용자 id
               um.salary, -- 기본급여
               pr.bonus_rate, -- 상여 지급율
               pr.bonus_amount, -- 상여 지급액
               ad.total_over_work_time, -- 총 연장근무시간
               ad.total_night_work_time, -- 총 야간근무시간
               ad.total_holiday_work_time, -- 총 휴일근무시간
               um.family_count, -- 가족수
               um.children_count, -- 자녀수
               al.expiry_date, -- 소멸예정일
               al.remaining_days -- 잔여연차
        FROM   tb_payroll pr
               JOIN tb_user_master um -- 사원관리 테이블
               ON pr.user_id = um.user_id
               JOIN ( SELECT ad.user_id,
                             SUM(ad.over_work_time) as total_over_work_time,
                             SUM(ad.night_work_time) as total_night_work_time,
                             SUM(ad.holiday_work_time) as total_holiday_work_time
                      FROM   tb_attendance ad -- 근태관리 테이블
                             JOIN tb_payroll pr -- 급여대장 테이블
                             ON ad.user_id = pr.user_id
                      WHERE  ad.work_date >= pr.payroll_start_date
                        AND  ad.work_date <= pr.payroll_end_date
                      GROUP BY ad.user_id
                ) ad 
                ON ad.user_id = pr.user_id
                JOIN tb_annual_leave al
                ON al.user_id = pr.user_id
         WHERE  pr.payroll_period_code = p_payroll_period_code;
        
    -- 변수선언
    v_uid tb_payroll.user_id%TYPE; -- 사용자id
    v_sal tb_user_master.salary%TYPE; -- 기본급여
    v_br tb_payroll.bonus_rate%TYPE; -- 지급율
    v_ba tb_paryoll.bonus_amount%TYPE; -- 지급액
    v_bonus NUMBER; -- 상여금
    v_ordinary_wage NUMBER; -- 통상임금
    v_overtime NUMBER; -- 연장근로수당
    v_night NUMBER; -- 야간근로수당
    v_holiday NUMBER; -- 휴일근로수당
    v_family NUMBER; -- 가족수당
    v_meal NUMBER; -- 식대
    v_annual_leave NUMBER; -- 연차휴가수당
    v_total_allowance NUMBER; -- 총수당총액
BEGIN
    -- 2) 커서 실행
    -- 3) 데이터 확인 및 인출
    FOR user_info IN user_cursor LOOP        
        -- 변수 설정
        v_uid := user_info.user_id; -- 사용자id
        v_sal := user_info.salary; -- 기본급여
        v_br := user_info.bonus_rate; -- 상여 지급율
        v_ba := user_info.bonus_amount; -- 상여 지급액
        -- 위에서 fetch into해서 실행한거 결과 출력
        DBMS_OUTPUT.PUT_LINE(v_uid); -- 사용자id
        DBMS_OUTPUT.PUT_LINE(','||v_sal); -- 급여
        
        -- 시간당 통상임금 := 기본급 / 209
        v_ordinary_wage := v_sal / 209;
        
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
        
        -- 연장근로수당
        IF user_info.total_over_work_time > 0 THEN   
            -- 연장근로수당 := 시간당 통상임금 * 총연장근무시간 * 1.5
            v_overtime := v_ordinary_wage * user_info.total_over_work_time * 1.5;
        ELSE
            v_overtime := 0;
        END IF;
                
        -- 야간근로수당
        IF user_info.total_night_work_time > 0 THEN
            -- 야간근로수당 := 시간당 통상임금 * 총야간근로수당 * 1.5
            v_night := v_ordinary_wage * user_info.total_night_work_time * 1.5;
        ELSE
            v_night := 0;
        END IF;
        
        -- 휴일근로수당
        IF user_info.total_holiday_work_time > 0 THEN
            -- 휴일근로수당 := 시간당 통상임금 * 총휴일근로수당 * 1.5
            v_holiday := v_ordinary_wage * user_info.total_holiday_work_time * 1.5;
        ELSE
            v_holiday := 0;
        END IF;
                
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
        
        -- 식대(월)
        v_meal := 200000;

        -- 연차휴가수당
        -- 미사용연차일수 * 1일 통상임금(통상시급 * 8)
        -- 25년에서 26년으로 넘어갔어 그럼 연차도 26년1월1일에 생기겠지 그럼 비교를 해야하네
        -- 지금 시스템날짜랑 소멸예정일이 같아지면 계산하고 그에 해당하는 사원의 연차의 잔여연차가 미사용연차일수네
        -- 그럼 필요한 데이터가 소멸예정일, 잔여연차
        IF user_info.expiry_date <= SYSDATE THEN
            v_annual_leave := (user_info.remaining_days) * (v_ordinary_wage * 8);
        ELSE
            v_annual_leave := 0;
        
        -- 총 수당총액
        v_total_allowance := v_overtime + v_night + v_holiday + v_family + v_meal + v_annual_leave;
        
        -- 소득세
        
        -- 국민연금
        -- 고용보험
        -- 건강보험
        -- 장기요양보험
        -- 지방소득세
        -- 총 공제총액
        -- 실 수령액
        
    END LOOP;
    -- 4) 커서 종료
END;
/

SET serveroutput ON; -- dbms_output하기전에 무조건 해야하는거
-- 프로시저 실행하는구문
EXEC sp_calculate_payroll('PRP25113000001');
SELECT *
FROM   tb_user_master;
    