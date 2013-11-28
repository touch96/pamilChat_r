create table users (
    id                      varchar(100) not null,
    pw                      varchar(100) not null,
    m_id               varchar(200) not null,
	createDt            date,
    constraint pk_parent primary key (m_id));