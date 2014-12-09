# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tas.public.advertisement (
  advertisement_id          integer not null,
  name                      varchar(255),
  type                      varchar(255),
  constraint pk_advertisement primary key (advertisement_id))
;

create table tas.public.area (
  area_id                   integer not null,
  name                      varchar(255),
  coord                     varchar(255),
  constraint pk_area primary key (area_id))
;

create table bar (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_bar primary key (id))
;

create table tas.public.cost (
  cost_id                   integer not null,
  price                     integer,
  currency                  varchar(255),
  constraint pk_cost primary key (cost_id))
;

create table tas.public.discount (
  discount_id               integer not null,
  name                      varchar(255),
  lower_cost                varchar(255),
  constraint pk_discount primary key (discount_id))
;

create table tas.public.guide (
  guide_id                  integer not null,
  availibility              boolean,
  first_name                varchar(255),
  last_name                 varchar(255),
  constraint pk_guide primary key (guide_id))
;

create table tas.public.poi (
  poi_id                    integer not null,
  suitable_for_movement_disabled boolean,
  coord                     varchar(255),
  minimal_age               integer,
  name                      varchar(255),
  rating                    integer,
  required_time             time,
  type                      varchar(255),
  cost                      integer,
  constraint pk_poi primary key (poi_id))
;

create table tas.public.purchase (
  purchase_id               integer not null,
  cost                      integer,
  status                    varchar(255),
  constraint pk_purchase primary key (purchase_id))
;

create table tas.public.ticket (
  ticket_id                 integer not null,
  cost                      varchar(255),
  validity                  varchar(255),
  constraint pk_ticket primary key (ticket_id))
;

create table tas.public.trip (
  trip_id                   integer not null,
  cost                      varchar(255),
  required_time             time,
  constraint pk_trip primary key (trip_id))
;

create table user (
  id                        integer not null,
  username                  varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

create table tas.public.user (
  user_id                   integer not null,
  designation               varchar(255),
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  username                  varchar(255),
  constraint pk_user primary key (user_id))
;

create sequence tas.public.advertisement_seq;

create sequence tas.public.area_seq;

create sequence bar_seq;

create sequence tas.public.cost_seq;

create sequence tas.public.discount_seq;

create sequence tas.public.guide_seq;

create sequence tas.public.poi_seq;

create sequence tas.public.purchase_seq;

create sequence tas.public.ticket_seq;

create sequence tas.public.trip_seq;

create sequence user_seq;

create sequence tas.public.user_seq;




# --- !Downs

drop table if exists tas.public.advertisement cascade;

drop table if exists tas.public.area cascade;

drop table if exists bar cascade;

drop table if exists tas.public.cost cascade;

drop table if exists tas.public.discount cascade;

drop table if exists tas.public.guide cascade;

drop table if exists tas.public.poi cascade;

drop table if exists tas.public.purchase cascade;

drop table if exists tas.public.ticket cascade;

drop table if exists tas.public.trip cascade;

drop table if exists user cascade;

drop table if exists tas.public.user cascade;

drop sequence if exists tas.public.advertisement_seq;

drop sequence if exists tas.public.area_seq;

drop sequence if exists bar_seq;

drop sequence if exists tas.public.cost_seq;

drop sequence if exists tas.public.discount_seq;

drop sequence if exists tas.public.guide_seq;

drop sequence if exists tas.public.poi_seq;

drop sequence if exists tas.public.purchase_seq;

drop sequence if exists tas.public.ticket_seq;

drop sequence if exists tas.public.trip_seq;

drop sequence if exists user_seq;

drop sequence if exists tas.public.user_seq;

