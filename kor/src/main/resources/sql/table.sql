use kordb;

create table member(
id int not null auto_increment,
userid varchar(20),
username varchar(20),
passwd varchar(20),
level int,
primary key(id)
);

create table board(
id int not null auto_increment,
subject varchar(50),
writer varchar(20),
grp int, -- 댓글 달 원본 글 값
depth int, -- 댓글 순서
primary key(id)
);