create table users (
    code                      varchar(100) not null,
    password                      varchar(100) not null,
    token               varchar(200) not null,
	createDt            date,
    constraint pk_parent primary key (code,token));
 
 create table friends (
 	code                      varchar(100) not null,
 	f_code                     varchar(100) not null,
 	createDt            date,
    constraint pk_parent primary key (code,f_code));
