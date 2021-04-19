package com.rrs.modules.sys.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rrs.common.core.base.BaseEntity;
import com.rrs.common.core.validator.constraints.LengthForUtf8;

import lombok.Data;

/**
 * 【系统参数】实体类
 *
 * @author zjm
 */
@Data
@TableName("T_SYS_CONFIG")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 系统参数ID
     */
    @TableId
    @NotNull
    @LengthForUtf8(max = 32)
    private String configId;

    /**
     * 系统参数名称
     */
    @NotNull
    @LengthForUtf8(max = 100)
    private String configName;

    /**
     * 系统参数值
     */
    @NotNull
    @LengthForUtf8(max = 255)
    private String configValue;

    /**
     * 排序号
     */
    @NotNull
    @Max(9999)
    private Integer sortNo;

    /**
     * 备注
     */
    @LengthForUtf8(max = 255)
    private String remark;

}