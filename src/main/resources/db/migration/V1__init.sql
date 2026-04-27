create table if not exists product (
    id bigserial primary key,
    external_id varchar(255) not null,
    name varchar(255) not null
);
