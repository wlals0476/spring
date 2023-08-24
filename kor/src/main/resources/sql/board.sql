use kordb;

-- 게시판 환경 설정 테이블
create table config(
code varchar(10), -- 게시판 테이블 이름
title varchar(100),
color varchar(10),
primary key(code)
);

-- 게시판 테이블 : kor_이름
create table kortb_${code}(
id int not null auto_increment,
subject varchar(100),
writer varchar(20),
content text,
grp int,
depth int,
primary key(id)
);

-- 일치하는 검색 =
SELECT * FROM 테이블이름 WHERE 검색컬럼 = 검색어;
ex) SELECT * FROM kortb_hrd WHERE subject = '검색어'; -- 제목 검색
SELECT * FROM kortb_hrd WHERE writer = '관리자';

-- 유사어 검색 LIKE : 검색어가 포함된 내용을 검색 - 속도가 느림
ex) SELECT * FROM 테이블이름 WHERE 검색컬럼 LIKE '%검색어%';
SELECT * FROM kortb_hrd WHERE content LIKE '%코리아아이티%';


