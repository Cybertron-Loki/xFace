CREATE
    DATABASE IF NOT EXISTS `xface`;
USE `xface`;
drop table if exists user;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`       INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255)        NOT NULL,
    `nickname` VARCHAR(255)        NOT NULL,
    `account`  VARCHAR(255)        NOT NULL,
    `password` VARCHAR(255)        NOT NULL,
    `avatar`   VARCHAR(255) DEFAULT 'http://hebei.sinaimg.cn/2013/0913/U7459P1275DT20130913150426.jpg',
    `role`     VARCHAR(255) DEFAULT 'user',
    `isDelete` TINYINT(1)   DEFAULT 0
);