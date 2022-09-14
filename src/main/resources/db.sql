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

# 查询表中全部数据
SELECT *
FROM user;
# 删除表
drop table `user`;

# mysql预编译
prepare statement from 'select * from user where uid = ? and name = ?';
set @uid = 4,@name = '小星';
execute statement using @uid,@name;

#查看general_log是否开启
show variables like 'general_log%';
#开启general log:
set global general_log = 1;

#查询日志时区
show variables like 'log_timestamps';
#修改日志时区为系统默认的时区，如果想永久修改时区，则在my.ini配置文件中的[mysqld]下增加log_timestamps=SYSTEM
set global log_timestamps = SYSTEM;

# 查看mysql binlog日志是否开启
show variables like 'log_bin';
#查看现在正在记录哪个文件
show master status;
#查看bin log日志
show binlog events in 'XIAORANG-bin.000004';

# 查看mysql数据存储目录
show global variables like '%datadir%';

# 查看时区，如果需要永久修改时区，则在my.ini配置文件中的[mysqld]下增加default-time_zone = '+8:00'
show variables like '%time_zone';
#查看当前时间
select now();

# 查看数据包消息缓存区初始大小
show variables like 'net_buffer_length';
# 查看数据包消息缓存区最大大小
show variables like 'max_allowed_packet';
# 重新打开数据库连接参数生效，数据库服务重启后参数恢复为默认，想永久修改的话，则在my.ini配置文件中的[mysqld]下增加max_allowed_packet=32*1024*1024
set global max_allowed_packet = 32 * 1024 * 1024;
