package com.rrs.modules.sys.entity.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zjm
 * @date 2020年3月24日
 */
@Data
public class Meta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String icon;
    private Boolean isCache = true;
    private Boolean affix;
}
