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
 	code                      varchar(100) not null,
 	f_code                     varchar(100) not null,
 	isNew  boolean,
 	status                     varchar(2) not null,
 	createDt            date,
    constraint pk_parent primary key (s_code,f_code));
ALTER TABLE friendrequest CHANGE COLUMN s_code code varchar(100) not null;
    
 create table msghistory (
 	msghistoryseq INT NOT NULL AUTO_INCREMENT,
 	send_code                     varchar(100) not null,
 	target                      varchar(100) not null,
 	img                          varchar(1000) not null,
 	sec    INT,
 	type varchar(1) not null,
 	createDt            date,
    constraint pk_parent primary key (msghistoryseq));


ALTER TABLE msghistory CHANGE COLUMN isNew type varchar(1) not null;