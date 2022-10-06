CREATE TABLE `dfl_user`
(
    `id`          int                                                    NOT NULL AUTO_INCREMENT,
    `nickname`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
    `username`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
    `descs`       varchar(255)                                            DEFAULT NULL COMMENT '个人描术信息',
    `telephone`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '电话',
    `email`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '邮箱',
    `pwd`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '密码',
    `if_del`      tinyint                                                NOT NULL COMMENT '是否删除',
    `status`      tinyint                                                NOT NULL COMMENT '状态:是否有效0无效，1有效',
    `remark`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `create_time` datetime                                                DEFAULT NULL,
    `modify_time` datetime                                                DEFAULT NULL,
    `create_user` int                                                     DEFAULT NULL,
    `modify_user` int                                                     DEFAULT NULL,
    `register_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL,
    `sys_type`    char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_nickname` (`nickname`) USING BTREE,
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

CREATE TABLE `dfl_role`
(
    `id`          int     NOT NULL AUTO_INCREMENT,
    `code`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '编码',
    `name`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '名称',
    `remark`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `if_del`      tinyint NOT NULL COMMENT '是否删除',
    `status`      tinyint NOT NULL COMMENT '状态:是否有效0无效，1有效',
    `create_time` datetime                                                DEFAULT NULL COMMENT '创建时间',
    `modify_time` datetime                                                DEFAULT NULL COMMENT '修改时间',
    `create_user` int                                                     DEFAULT NULL COMMENT '创建人',
    `modify_user` int                                                     DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色表';

CREATE TABLE `dfl_resource`
(
    `id`          int                                                     NOT NULL AUTO_INCREMENT,
    `name`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `uri`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口uri',
    `uri_id`      int                                                    DEFAULT NULL,
    `method`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求类型(GET/POST/PUT)',
    `res_type`    tinyint                                                DEFAULT NULL COMMENT '资源类型，0接口,1功能',
    `status`      tinyint                                                DEFAULT NULL,
    `if_del`      tinyint                                                DEFAULT NULL COMMENT '是否删除',
    `create_time` datetime                                               DEFAULT NULL COMMENT '创建时间',
    `modify_time` datetime                                               DEFAULT NULL COMMENT '修改时间',
    `create_user` int                                                    DEFAULT NULL COMMENT '创建人',
    `modify_user` int                                                    DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_uri` (`uri`) USING BTREE,
    KEY           `idx_create` (`create_time`) USING BTREE,
    KEY           `idx_uri_id` (`uri_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='菜单资源管理';