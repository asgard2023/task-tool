CREATE TABLE `ta_data_method`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `code`              varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '方法名',
    `name`              varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
    `category`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '分类：仅分类，没基他作用',
    `show_processing`   tinyint                                                 DEFAULT NULL COMMENT '是否显示正在进行中的任务',
    `data_id_arg`       varchar(64)                                             DEFAULT NULL COMMENT 'dataId参数序号或参数名',
    `if_del`            tinyint                                                 DEFAULT NULL COMMENT '是否删除',
    `status`            tinyint                                                 DEFAULT NULL COMMENT '状态',
    `remark`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `create_time`       datetime                                                DEFAULT NULL COMMENT '创建时间',
    `modify_time`       datetime                                                DEFAULT NULL COMMENT '修改时间',
    `create_user`       int                                                     DEFAULT NULL COMMENT '创建人',
    `modify_user`       int                                                     DEFAULT NULL COMMENT '修改人',
    `if_log_detail`     tinyint                                                 DEFAULT NULL COMMENT ' 是否日志详情',
    `if_remind`         tinyint                                                 DEFAULT NULL COMMENT '是否进行告警提示',
    `pkg`               varchar(128)                                            DEFAULT NULL COMMENT '包名',
    `type`              varchar(32)                                             DEFAULT NULL COMMENT '类别',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_code` (`code`) USING BTREE,
    KEY                 `idx_create` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;


CREATE TABLE `ta_method_count`
(
    `id`                   int NOT NULL AUTO_INCREMENT,
    `data_method_id`       int                                                     DEFAULT NULL COMMENT '数据方法id',
    `time_type`            varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '时间类型',
    `time_seconds`         mediumint                                               DEFAULT NULL,
    `time_value`           int                                                     DEFAULT NULL COMMENT '时间值',
    `run_count`            bigint                                                  DEFAULT NULL COMMENT '运行次数',
    `run_time`             int                                                     DEFAULT NULL COMMENT '最新运行时间(ms)',
    `run_time_date`        datetime                                                DEFAULT NULL COMMENT '最近运行时间',
    `run_uid`              varchar(64)                                             DEFAULT NULL,
    `first_time`           datetime                                                DEFAULT NULL COMMENT '首次运行时间',
    `first_uid`            varchar(64)                                             DEFAULT NULL,
    `run_server`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL,
    `error_count`          int                                                     DEFAULT NULL COMMENT '错误次数',
    `error_newly_info`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最新错误信息',
    `error_newly_data_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '最新错误dataId',
    `error_newly_time`     datetime                                                DEFAULT NULL COMMENT '最新错误时间',
    `error_newly_server`   varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '最新错误对应的服务器名',
    `error_newly_uid`      varchar(64)                                             DEFAULT NULL,
    `max_run_time`         int                                                     DEFAULT NULL COMMENT '最大执行时间(ms)',
    `max_run_time_date`    datetime                                                DEFAULT NULL COMMENT '最大执行时间发生时间',
    `max_run_time_data_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '最大执行时间对应dataId',
    `max_run_server`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '最大执行时间对应的服务器名',
    `max_run_time_uid`     varchar(64)                                             DEFAULT NULL,
    `if_del`               tinyint                                                 DEFAULT NULL COMMENT '是否删除',
    `status`               tinyint                                                 DEFAULT NULL COMMENT '是否有效',
    `create_time`          datetime                                                DEFAULT NULL COMMENT '创建时间',
    `modify_time`          datetime                                                DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                    `idx_create` (`create_time`) USING BTREE,
    KEY                    `idx_data_method_id` (`data_method_id`) USING BTREE,
    KEY                    `idx_time_value` (`time_value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='任务运行次数统计记录';


CREATE TABLE `ta_method_count_source`
(
    `id`              int                                                     NOT NULL AUTO_INCREMENT,
    `method_count_id` int                                                     NOT NULL,
    `source`          varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `run_count`       int                                                     NOT NULL,
    `if_del`          tinyint  DEFAULT NULL,
    `create_time`     datetime                                                NOT NULL,
    `modify_time`     datetime DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `idx_method_count` (`method_count_id`) USING BTREE,
    KEY               `create_date` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;