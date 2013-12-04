create table users (
    id                      varchar(100) not null,
    pw                      varchar(100) not null,
    device               varchar(200) not null,
	createDt            date,
    constraint pk_parent primary key (device));
 
 create table friends (
 	id                      varchar(100) not null,
 	f_id                     varchar(100) not null,
 	createDt            date,
    constraint pk_parent primary key (id,f_id));
