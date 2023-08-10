/*DROP DATABASE tienda*/
create database tienda;
use tienda;

							/* USUARIO_VENTA */
/*DROP TABLE usuario_venta*/
create table usuario_venta(
ci_user varchar(10) not null primary key,
nombres_user varchar(60) not null
);

							/* LOGIN USUARIO_VENTA */
/*DROP TABLE login_venta*/
create table login_venta(
FK_ci_user varchar(10) not null,
usuario_user varchar(10) not null,
contrasenia_user varchar(10) not null,
constraint FK_ci_user foreign key (FK_ci_user) references usuario_venta(ci_user)
);

							/* ADMIN */
/*DROP TABLE usuario_venta*/
create table admin (
ci_ad varchar(10) not null primary key,
nombres_ad varchar(60) not null
);

							/* LOGIN_ADMIN */
/*DROP TABLE login_admin*/
create table login_admin(
FK_ci_ad varchar(10) not null,
usuario_ad varchar(10) not null,
contrasenia_ad varchar(10) not null,
constraint FK_ci_ad foreign key (FK_ci_ad) references admin(ci_ad)
);

							/* SUPER ADMIN */
/*DROP TABLE usuario_venta*/
create table super_admin (
ci_sa varchar(10) not null primary key,
nombres_sa varchar(60) not null
);

							/* LOGIN SUPER ADMIN */
/*DROP TABLE login_admin*/
create table login_sa(
FK_ci_sa varchar(10) not null,
usuario_sa varchar(10) not null,
contrasenia_sa varchar(10) not null,
constraint FK_ci_sa foreign key (FK_ci_sa) references super_admin(ci_sa)
);

SELECT * FROM admin;
SELECT * FROM login_admin;

							/* DATOS DE ADMINISTRADOS */
insert into super_admin values ("1727936070", "Dilan Alexander Flores Quimbia");
insert into login_sa values ("1727936070", "DA_17", "1727");

							/*CLIENTE*/
/*DROP TABLE cliente*/
create table cliente(
ci_cl varchar(10) not null primary key,
nombres_cl varchar(60) not null,
estado_cuenta numeric(3,2) not null
);

							/* PRODUCTO */
/*DROP TABLE producto*/
create table producto (
id_producto int not null primary key,
producto varchar(25) not null,
inversion numeric(3,2) not null,
ganancia numeric(3,2) not null,
precio_venta_unitario numeric(3,2) not null
);
							/* INVENTARIO */
/*DROP TABLE inventario*/
create table inventario (
FK_id_producto int not null primary key,
fecha_registro date null,
fecha_actual date null,
cantidad numeric(3) not null,
precio_total numeric(5,2) not null,
/*precio_venta_total numeric(5,2) not null,*/
constraint  foreign key (FK_id_producto) references producto(id_producto)
);
							/*CABECERA DE FACTURA*/
/*DROP TABLE cab_trans*/
create table cab_trans (
num_f varchar(5) not null primary key,
fecha_f date null,
/*FKci_user varchar(10) not null,*/
total_f numeric(5,2)  null,
FKci_cl varchar(10) null,
/*constraint FKci_user foreign key (FKci_user) references usuario_venta(ci_user),*/
constraint FKci_cl foreign key (FKci_cl) references cliente(ci_cl)
);

							/* DETALLE DE FACTURA */
/*DROP TABLE det_trans*/
create table det_trans (
FKnum_f varchar(5) not null,
FKid_producto int not null,
cantidad_dt numeric(3) not null,
precio_dt numeric(5,2) not null,
constraint FKnum_f foreign key (FKnum_f) references cab_trans(num_f),
constraint FKid_producto foreign key (FKid_producto) references producto(id_producto)
);
       
							/* VISUALIZACIÓN DE TABLAS */
SELECT * FROM usuario_venta;
SELECT * FROM admin;
SELECT * FROM super_admin;
SELECT * FROM login_venta;
SELECT * FROM login_admin;
SELECT * FROM login_sa;
SELECT * FROM cliente;
SELECT * FROM producto;
SELECT * FROM inventario;
SELECT * FROM cab_trans;
SELECT * FROM det_trans;

set SQL_SAFE_UPDATES=0;


