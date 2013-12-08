create table users (
    code                varchar(100) not null,
    password            varchar(100) not null,
    token               varchar(200) not null,
	createDt            date,
    constraint pk_parent primary key (code,token));
 
 create table friends (
 	code                      varchar(100) not null,
 	f_code                     varchar(100) not null,
 	createDt            date,
    constraint pk_parent primary key (code,f_code));
    
 create table friendrequest (
 	s_code                      varchar(100) not null,
 	f_code                     varchar(100) not null,
 	isNew  boolean,
 	status                     varchar(2) not null,
 	createDt            date,
    constraint pk_parent primary key (s_code,f_code));

 create table msghistory (
 	msghistoryseq INT NOT NULL AUTO_INCREMENT,
 	send_code                     varchar(100) not null,
 	recieve_code                      varchar(100) not null,
 	img                          varchar(1000) not null,
 	sec    INT,
 	isNew  boolean,
 	createDt            date,
    constraint pk_parent primary key (msghistoryseq));
