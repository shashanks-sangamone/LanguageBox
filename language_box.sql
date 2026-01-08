create database language_box;
-- drop database language_box;
use language_box;

create table companies(
	id integer primary key auto_increment,
    company_name varchar(100) unique not null,
    country_id varchar(3) not null
);

insert into companies(company_name,country_id) values("SMVITM","IND");

create table users(
	id integer primary key auto_increment,
    company_id integer not null,
    user_name varchar(40) not null,
    user_email varchar(40) not null unique,
    date1 date not null,
    role enum("USER","ADMIN") default "USER" not null,
    foreign key (company_id) references companies(id) on delete cascade
);

insert into users(company_id,user_name,user_email,date1) values(1,"shashank.s","ssvision069@gmail.com",'2025-09-05');

create table devices(
	device_id integer primary key unique,
    device_name varchar(20) not null,
    company_id integer not null,
    date1 date not null,
    location integer not null,
    language_id varchar(3) not null
);

insert into devices(device_id,device_name,company_id,date1,location,language_id) values(1,"ENG1",1,'2025-09-05',577228,"ENG");

create table user_lists(
	id integer auto_increment primary key,
    user_id integer not null,
    list_name varchar(20) not null,
    filename varchar(40) not null,
    foreign key (user_id) references users(id) on delete cascade
);

insert into user_lists(user_id,list_name,filename) values(1,"list1","list1-1");

create table plans(
	id integer auto_increment primary key,
    plan_name varchar(20) not null unique,
    price int not null unique,
    description varchar(100) not null
);

insert into plans(plan_name,price,description) values("Free Plan","0.0","Free for first 1 year");

create table subscription(
	id integer auto_increment primary key,
    subscription_start datetime not null,
    subscription_ends datetime not null,
    transaction_id varchar(40) unique default null,
    device_id integer not null,
    status tinyint not null,
    plan_id integer not null,
    foreign key (device_id) references devices(device_id) on delete cascade,
    foreign key (plan_id) references plans(id) on delete cascade
);

insert into subscription(subscription_start,subscription_ends,transaction_id,device_id,status,plan_id) values
('2025-07-17','2025-07-17',"111111",1,0,1);



-- drop table subscription;

select * from companies;
select * from users;
select * from user_lists;
select * from devices;
select * from plans;
select * from subscription;

-- update companies set company_name="sa" where id=1;
-- update companies set country_id="" where id=1;
-- update user_lists set list_name="" where id=1;

