package com.rrs.modules.sys.entity;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rrs.common.core.base.BaseEntity;
import com.rrs.common.core.validator.constraints.LengthForUtf8;

import lombok.Data;

/**
 * 【数据权限】实体类
 *
 * @author zjm
 */
@Data
@TableName("T_SYS_DATA_PERMISSION")
public class SysDataPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据权限ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @LengthForUtf8(max = 32)
    private String dataPermissionId;

    /**
     * 数据权限名称
     */
    @NotNull
    @LengthForUtf8(max = 100)
    private String dataPermissionName;

    /**
     * 方法Id
     */
    @LengthForUtf8(max = 255)
    private String methodId;

    /**
     * 实体类型
     */
    @LengthForUtf8(max = 1)
    private String entityType;

    /**
     * 实体ID
     */
    @LengthForUtf8(max = 32)
    private String entityId;

    /**
     * 业务表名
     */
    @LengthForUtf8(max = 100)
    private String tableName;

    /**
     * 类名
     */
    @LengthForUtf8(max = 255)
    private String className;

    /**
     * 字段名
     */
    @LengthForUtf8(max = 100)
    private String columnName;

    /**
     * 数据源策略
     */
    @LengthForUtf8(max = 1)
    private String sourceStrategy;

    /**
     * 查询表达式
     */
    @LengthForUtf8(max = 100)
    private String operate;

    /**
     * 查询条件
     */
    @LengthForUtf8(max = 255)
    private String value;

    /**
     * 系统数据源类
     */
    @LengthForUtf8(max = 255)
    private String sourceProvider;

    /**
     * 系统数据源参数
     */
    @LengthForUtf8(max = 255)
    private String sourceProviderParams;

}