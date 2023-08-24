use tbl_koreait;

create table kor_employees(
kor_emp_id int not null auto_increment,
kor_emp_email varchar(50) not null unique, -- null 가능, 중복된 값은 x
kor_emp_passwd varchar(20) not null,
kor_emp_name varchar(10) not null,
kor_emp_gender char(1), -- = char(1)
kor_emp_dept char(3) not null,
kor_emp_pos char(3) not null,
kor_emp_level int default 1, -- level > 3
kor_emp_auth char(1),
kor_emp_created datetime, -- 사원 가입일
kor_emp_modified datetime, -- 사원 정보 수정일
kor_emp_image_name varchar(255),
kor_emp_image_size bigint, -- bigint = long
kor_emp_trans_name varchar(255),
primary key(kor_emp_id)
);

-- ADMIN 계정은 미리 생성
INSERT INTO kor_employees VALUES(NULL, 'mail@mail.com', '1111', '관리자', 'W', '100', '101', 7, 'Y', now(), now(), '', 0, '');

-- 테이블 2개 조인하면서 해당 id 값만 조회
SELECT D.kor_dept_name FROM kor_dept D INNER JOIN kor_employees E ON D.kor_dept_code = E.kor_emp_dept WHERE E.kor_emp_id = 8;

-- 테이블 3개 조인하면서 해당 id 값만 조회
-- D.*, P.*, E.*로 적을 수 있음(이름이 충돌하지만 않으면)
SELECT * FROM kor_dept D INNER JOIN kor_employees E ON D.kor_dept_code = E.kor_emp_dept INNER JOIN kor_pos P ON E.kor_emp_pos = P.kor_pos_code
WHERE E.kor_emp_id = 8;

