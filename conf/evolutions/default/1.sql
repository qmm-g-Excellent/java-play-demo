# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table order_record (
  id                            varchar(255) not null,
  order_number                  varchar(255),
  order_amount                  float,
  is_invoice                    integer,
  create_time                   timestamptz not null,
  update_time                   timestamptz not null,
  constraint ck_order_record_status check ( status in ('TO_BE_PAID','PAID','CANCELLED','REFUNDING','REFUNDED','SYSTEM_CANCELLED')),
  constraint pk_order_record primary key (id)
);
create sequence order_record_seq;

create table payment (
  id                            varchar(255) not null,
  amount                        decimal(38),
  notify_url                    varchar(255),
  pay_time                      timestamptz,
  actual_paid                   decimal(38),
  create_time                   timestamptz not null,
  update_time                   timestamptz not null,
  constraint pk_payment primary key (id)
);
create sequence payment_seq;

create table product (
  id                            varchar(255) not null,
  provider                      varchar(255),
  product_num                   integer,
  product_price                 float,
  status                        varchar(16),
  actual_refund_amount          float,
  create_time                   timestamptz not null,
  update_time                   timestamptz not null,
  constraint ck_product_status check ( status in ('TO_BE_PAID','PAID','CANCELLED','REFUNDING','REFUNDED','SYSTEM_CANCELLED')),
  constraint pk_product primary key (id)
);
create sequence product_seq;



# --- !Downs

alter table if exists product drop constraint if exists fk_product_order_record_id;
drop index if exists ix_product_order_record_id;

drop table if exists order_record cascade;
drop sequence if exists order_record_seq;

drop table if exists payment cascade;
drop sequence if exists payment_seq;

drop table if exists product cascade;
drop sequence if exists product_seq;


