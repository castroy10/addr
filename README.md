# Телефонный справочник
#### Телефонный справочник, удобно реализующий, на мой взгляд, паттерны поведения пользователей телефонного справочника организации

Использованные технологии:
- Spring Boot
- Vaadin Framework
- Postgres SQL
- Spring Security

База данных состоит из следующих таблиц:

    create table department
    (
    id serial not null constraint department_pkey primary key,
    chief_employee_id integer not null,
    department_name varchar(255),
    parent_department_id integer not null
    );

    create table position
    (
    id serial not null constraint position_pkey primary key,
    chief_employee_id integer,
    department_id integer,
    parent_position_id integer,
    position_name varchar(255)
    );

    create table employee
    (
    id serial not null constraint employee_pkey primary key,
    first_name varchar(255),
    last_name varchar(255),
    middle_name varchar(255),
    mobile_phone varchar(255),
    phone varchar(255),
    email varchar(255),
    department_id integer constraint department_id references department,
    position_id integer constraint position_id references position
    );

    create table usr
    (
    id integer generated by default as identity constraint usr_pkey primary key,
    username varchar(100) not null,
    password varchar(100) not null,
    accountexperied boolean,
    accountlocked boolean,
    credentialsexperied boolean,
    employee_id integer constraint usr_employee_id_fkey references employee on delete set null
    );

Для защиты административной части /admin использован SpringSecurity, хранящий пользователей в базе Usr с кодировкой паролей BCrypt.

Посмотреть, как это работает можно здесь: https://addr2.castroy10.ru/

