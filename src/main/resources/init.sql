DROP DATABASE IF EXISTS `xface`;
CREATE DATABASE IF NOT EXISTS `xface`;

USE `xface`;

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User`
(
    `id`         bigint(64)   NOT NULL COMMENT '用户唯一标识',
    `username`   varchar(255) NOT NULL COMMENT '用户名',
    `password`   varchar(255) COMMENT '登录密码',
    `phoneNum`   varchar(255) NOT NULL COMMENT '手机号码',
    `role`       varchar(128) NOT NULL DEFAULT 'user' COMMENT '用户角色 user-普通用户 admin-管理员等',
    `status`     int(11)      NOT NULL DEFAULT 0 COMMENT '用户状态 0-正常 1-禁用等',
    `avatar`     varchar(255) COMMENT '用户头像',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `avatar`     varchar(128) NOT NULL DEFAULT 'u',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '用户表';

DROP TABLE IF EXISTS `Friendship`;

CREATE TABLE `Friendship`
(
    `userId`         bigint(64)   NOT NULL COMMENT '用户id',
    `friendId`       bigint(64)   NOT NULL COMMENT '好友id',
    `friendName`     VARCHAR(255) NOT NULL COMMENT '好友名称',
    `friendNickName` VARCHAR(255) NULL COMMENT '好友昵称',
    `friendStatus`   int(11)      NOT NULL DEFAULT 0 COMMENT '好友状态 0-正常 1-禁用等',
    `status`         int(11)      NOT NULL DEFAULT 0 COMMENT '好友关系状态 0-正常 1-禁用等',
    `createTime`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`userId`, `friendId`),
    FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friendId`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '友情状态存疑表';

DROP TABLE IF EXISTS `Group`;

CREATE TABLE `Group`
(
    `id`           bigint(64)   NOT NULL COMMENT '群组id',
    `name`         VARCHAR(255) NOT NULL COMMENT '群组名称',
    `creatorId`    bigint(64)   NOT NULL COMMENT '创建者id',
    `status`       int(11)      NOT NULL DEFAULT 0 COMMENT '群组状态 0-正常 1-禁用等',
    `createTime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `type`         INT(11)      NOT NULL COMMENT '族群类别，默认0-朋友群，1-家庭群，2-同事群 ...',
    `memberNumber` INT(11)      NOT NULL DEFAULT 0 COMMENT '群人数',
    `headerId`     bigint(64)   NOT NULL COMMENT '群主id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`creatorId`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '群聊信息表';

DROP TABLE IF EXISTS `GroupMember`;

CREATE TABLE `GroupMember`
(
    `groupId`    bigint(64)   NOT NULL COMMENT '群组id',
    `userId`     bigint(64)   NOT NULL COMMENT '用户id',
    `status`     int(11)      NOT NULL DEFAULT 0 COMMENT '群组状态 0-正常 1-禁用等',
    `createTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `nickName`   VARCHAR(255) COMMENT '群内昵称',
    `role`       VARCHAR(255) NOT NULL DEFAULT 'user' comment '普通用户',
    PRIMARY KEY (`groupId`, `userId`),
    FOREIGN KEY (`groupId`) REFERENCES `Group` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '群成员信息表';

DROP TABLE IF EXISTS `Message`;

-- 待定 不确定
CREATE TABLE `Message`
(
    `id`         bigint(64)   NOT NULL COMMENT '消息id',
#     `conversationId` VARCHAR(255) NOT NULL COMMENT '会话id 私聊/群聊',
    `senderId`   bigint(64)   NOT NULL COMMENT '发送者id',
    `receiverId` bigint(64)   NULL COMMENT '接收者id 空为群聊信息，非空为私聊信息',
    `content`    TEXT         NOT NULL COMMENT '消息内容',
    `type`       VARCHAR(255) NOT NULL COMMENT '消息类型 text/image/file/video/audio/etc',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`senderId`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiverId`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '消息表';

DROP TABLE IF EXISTS `Diary`;
-- TODO 未设置隐私范围
CREATE TABLE `Diary`
(
    `id`         bigint(64)   NOT NULL COMMENT '日记id',
    `userId`     bigint(64)   NOT NULL COMMENT '用户id',
    -- 是否需要创建类别表 进行外键相连   我觉得不用，这个就行
    `typeId`     bigint(64) COMMENT '日记类型',
    `title`      VARCHAR(255) NOT NULL COMMENT '日记标题',
    `content`    TEXT         NOT NULL COMMENT '日记内容',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `visible`    VARCHAR(255) NOT NULL COMMENT '隐私：先定为公开可见或者仅群内可见或者仅自己可见',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '日记表';

DROP TABLE IF EXISTS `Comment`;

CREATE TABLE `Comment`
(
    `id`         bigint(64) NOT NULL COMMENT '评论id',
    `userId`     bigint(64) NOT NULL COMMENT '用户id',
    `diaryId`    bigint(64) COMMENT '日记id',
#     `parentId`   INT(11)  DEFAULT -1 COMMENT '父评论id',
    `content`    TEXT       NOT NULL COMMENT '评论内容',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `blogId`     INT(11) COMMENT 'blog id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '评论表';


DROP TABLE IF EXISTS `Brand`;
CREATE TABLE `Brand`
(
    `id`         bigint(64)   NOT NULL COMMENT '商家id',
    `name`       VARCHAR(255) NOT NULL COMMENT '商家名/品牌名',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `location`   VARCHAR(255) NOT NULL COMMENT '商家名/品牌位置',
#     `score`      INT(5)       NOT NULL COMMENT '商家评分',
    `type_id`    int(11) comment '所属范畴',
    `images`     varchar(255) comment '照骗',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '商家/品牌表';

DROP TABLE IF EXISTS `Blog`;

CREATE TABLE `Blog`
(
    `id`           bigint(64)   NOT NULL COMMENT '个人发表的避雷/种草/旅游经验帖子',
    `userId`       bigint(64)   NOT NULL COMMENT '用户id',
    `commentCount` bigint(64) COMMENT '评论数量',
    `createTime`   DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `type`         VARCHAR(255) NOT NULL COMMENT 'blog类型',
    `visible`      VARCHAR(255) NOT NULL DEFAULT 'public' COMMENT '隐私：personal 个人可见;public 任何人都可以看',
    `content`      TEXT         NOT NULL COMMENT 'blog内容',
    `brand_id`     bigint(64) COMMENT '关联品牌id（可不填）',
    `shop_id`      bigint(64) COMMENT '关联商家id（可不填）',
    `products_id`  bigint(64) COMMENT '关联产品id（可不填）',
    `location`     VARCHAR(255) COMMENT '所在位置',
    `group_id`     bigint(64) COMMENT '群组id(可见范围，可以让哪个群看到）',
    `like`         INT(11)               DEFAULT 0 COMMENT '点赞数',
    `title`        VARCHAR(255) NOT NULL COMMENT '标题',
    `images`       VARCHAR(255) NOT NULL COMMENT '图片',
    `avatar`       VARCHAR(255) NOT NULL COMMENT '用户头像',
    `userName`     VARCHAR(255) NOT NULL COMMENT '用户名字',
    PRIMARY KEY (`id`),
#     FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`group_id`) REFERENCES `Group` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '帖子表';

DROP TABLE IF EXISTS `Log_History`;
CREATE TABLE `Log_History`
(
    `id`             bigint(64)   NOT NULL AUTO_INCREMENT COMMENT 'log的id',
    `userName`       VARCHAR(255) NOT NULL COMMENT 'SEI',
    `operation_type` VARCHAR(255) NOT NULL comment '嘎哈了',
    `operation_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    primary key (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '操作记录表';

DROP TABLE IF EXISTS `Type_Blog`;
CREATE TABLE `Type_Blog`
(
    `id`     int(11)      NOT NULL AUTO_INCREMENT COMMENT 'type的id',
    `name`   VARCHAR(255) NOT NULL comment '种类名称',
    `images` VARCHAR(255) NOT NULL comment '图片',
    primary key (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = 'blog类型表';


DROP TABLE IF EXISTS `Follow`;
CREATE TABLE `Follow`
(
    `id`           bigint(64) NOT NULL AUTO_INCREMENT COMMENT 'type的id',
    `userId`       bigint(64) NOT NULL comment '被关注的用户id',
    `userFollowId` bigint(64) NOT NULL comment '被关注的用户id',
    `createTime`   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    primary key (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '关注用户表';


DROP TABLE IF EXISTS `UserInfo`;
CREATE TABLE `UserInfo`
(
    `userId`     bigint(64)   NOT NULL COMMENT '用户id',
    `city`       VARCHAR(255) COMMENT '所在城市',
    `birthDay`   DATETIME comment '生日',
    `hobby`      VARCHAR(255) comment '爱好',
    `userName`   VARCHAR(255) NOT NULL comment '用户名',
    `fans`       INT(11)      NOT NULL default 0 COMMENT '粉丝数',
    `introduce`  VARCHAR(255) comment '个人描述',
    `followee`   INT(11)      NOT NULL default 0 COMMENT '关注数',
    `gender`     CHAR(5)      not null default 'none' comment '性别，默认none，女为’女‘男为‘男',
    `age`        INT(11) COMMENT '年龄',
    `mail`       VARCHAR(255) comment '邮箱',
    `avatar`     varchar(255) NULL COMMENT '用户头像',
    `createTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '个人展示页表';


DROP TABLE IF EXISTS `DiaryType`;
CREATE TABLE `DiaryType`
(
    `userId`     bigint(64)   NOT NULL COMMENT '用户id',
    `id`         bigint(64)   NOT NULL COMMENT 'id',
    `type`       VARCHAR(255) NOT NULL DEFAULT '默认',
    `createTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `visible`    VARCHAR(255) NOT NULL DEFAULT 'public',
#     `group_id`   bigint(64)   COMMENT '群id',
    primary key (`id`)
#     foreign key (`group_id`) references `Group`(`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '个人展示页表';

DROP TABLE IF EXISTS `Shop`;
CREATE TABLE `Shop`
(
    `id`         bigint(64)   NOT NULL COMMENT '商店id',
    `location`   varchar(255) not null comment '地点',
    `shopName`   varchar(255) not null comment '名字',
    `type_id`    int(11) comment '所属范畴',
    `images`     varchar(255) comment '照骗',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '商店表';

DROP TABLE IF EXISTS `Products`;
CREATE TABLE `Products`
(
    `id`         bigint(64) NOT NULL COMMENT '产品id',
    `shopId`     bigint(64) comment '关联商家',
    `brandId`    bigint(64) comment '关联品牌',
    `images`     varchar(255) comment '照骗',
    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`id`),
    foreign key (`brandId`) REFERENCES `Brand` (`id`),
    foreign key (`shopId`) references `Shop` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT = '商店表';


DROP TABLE IF EXISTS `Voucher`;
CREATE TABLE `Voucher`
(
    `id`           bigint(64)  UNSIGNED NOT NULL COMMENT '主键',
    `shop_id`      bigint(64)  UNSIGNED NOT NULL COMMENT '商铺id',
    `title`        varchar(255)        NOT NULL COMMENT '代金券标题',
    `sub_title`    varchar(255)        NULL     DEFAULT NULL COMMENT '副标题',
    `rules`        varchar(1024)       NULL     DEFAULT NULL COMMENT '使用规则',
    `pay_value`    bigint(10) UNSIGNED NOT NULL COMMENT '支付金额，单位是分。例如200代表2元',
    `actual_value` bigint(10)          NOT NULL COMMENT '抵扣金额，单位是分。例如200代表2元',
    `type`         tinyint(1)          NOT NULL DEFAULT 0 COMMENT '0,普通券；1,秒杀券;2,个人发起的券',
    `status`       tinyint(1)          NOT NULL DEFAULT 1 COMMENT '1,上架; 2,下架; 3,过期',
    `createTime`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='优惠券';



DROP TABLE IF EXISTS `Voucher_Order`;
CREATE TABLE `Voucher_Order`
(
    `id`         bigint(64)           NOT NULL COMMENT '主键',
    `user_id`     bigint(64)   NOT NULL COMMENT '下单的用户id',
    `voucher_id`  bigint(64)   NOT NULL COMMENT '购买的代金券id',
    `pay_type`    tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '支付方式 1：余额支付；2：支付宝；3：微信',
    `status`      tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款',
    `create_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    `pay_time`    DATETIME            NULL     DEFAULT NULL COMMENT '支付时间',
    `use_time`    DATETIME            NULL     DEFAULT NULL COMMENT '核销时间',
    `refund_time` DATETIME            NULL     DEFAULT NULL COMMENT '退款时间',
    `update_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='优惠券订单';


DROP TABLE IF EXISTS `Group_Voucher`;
CREATE TABLE `Group_Voucher`
(
    `group_id`   bigint(20)          NOT NULL COMMENT '所属群',
    `creator_id` bigint(20)          NOT NULL COMMENT '创建人',
    `voucher_id` bigint(20) UNSIGNED NOT NULL COMMENT '关联的优惠券的id',
    primary key (`voucher_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='群内优惠券表，与优惠券是一对一关系';

DROP TABLE IF EXISTS `Seckill_Voucher`;
CREATE TABLE `Seckill_Voucher`
(
    `voucher_id`  bigint(20) UNSIGNED NOT NULL COMMENT '关联的优惠券的id',
    `stock`       int(8)              NOT NULL COMMENT '库存',
    `create_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `begin_time`  DATETIME            NOT NULL COMMENT '生效时间',
    `end_time`    DATETIME            NOT NULL COMMENT '失效时间',
    `update_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`voucher_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='秒杀优惠券表，与优惠券是一对一关系';


DROP TABLE IF EXISTS `Collection`;
CREATE TABLE `Collection`
(
    `id`          bigint(64)  NOT NULL COMMENT 'id',
    `name`        varchar(255)        NOT NULL COMMENT 'name',
    `userId`      bigint(64)          NOT NULL,
    `create_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='收藏夹';

DROP TABLE IF EXISTS `Collection_Reflection`;
CREATE TABLE `Collection_Reflection`
(
    `id`           bigint(64)  NOT NULL COMMENT 'id',
    `collectionId` bigint(64)  NOT NULL,
    `blogId`       bigint(64)          NOT NULL,
    `userId`       bigint(64)          NOT NULL,
    `blog_name`    varchar(255)        NOT NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY (`collectionId`) REFERENCES `Collection` (`id`),
     FOREIGN KEY (`blogId`)   REFERENCES `Blog` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='收藏夹---';



