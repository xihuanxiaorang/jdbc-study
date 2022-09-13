CREATE DATABASE IF NOT EXISTS atguigudb;
use atguigudb;
CREATE TABLE IF NOT EXISTS `user`
(
    `uid`      BIGINT(32) AUTO_INCREMENT COMMENT '主键列(自动增长)',
    `name`     VARCHAR(32) NOT NULL COMMENT '用户名',
    `age`      INT(3)      NOT NULL COMMENT '用户年龄',
    `birthday` DATE        NOT NULL COMMENT '用户生日',
    `salary`   FLOAT DEFAULT 15000.0 COMMENT '用户月薪',
    `note`     TEXT COMMENT '用户说明',
    CONSTRAINT pk_uid PRIMARY KEY (`uid`)
) COMMENT '用户表';

INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`)
VALUES ('小让', 18, '1995-07-13', 16000.0, '程序员');
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`)
VALUES ('小星', 18, '1995-03-20', 20000.0, '幼教');
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`)
VALUES ('三十', 25, '1995-08-08', 22000.0, '硬件工程师');

DELETE
FROM `user`
WHERE `uid` = 1;

SELECT *
FROM user;

