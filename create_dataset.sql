create database copoint;
use copoint;
drop table t_user;
create table t_user(
	id int(11) not null auto_increment,
    name varchar(30),
    pwd varchar(100),
    primary key(id)
)default charset=utf8mb4;
insert into t_user(name,pwd) values('zhu','123456');
insert into t_user(name,pwd) values('yao','123456');
select * from t_user;

drop table t_group;
create table t_group(
	id int(11) not null auto_increment,
    authorName varchar(30),
    groupName varchar(60),
    memberName varchar(30),
    primary key(id)
)default charset=utf8mb4;
select * from t_group;
insert into t_group(authorName,groupName,memberName) values('zhu','group@zhe',null);
insert into t_group(authorName,groupName,memberName) values('yao','group@yao',null);
select * from t_group;
