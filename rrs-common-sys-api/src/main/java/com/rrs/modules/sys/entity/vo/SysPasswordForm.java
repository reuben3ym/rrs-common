package com.rrs.modules.sys.entity.vo;

import lombok.Data;

/**
 * @author zjm
 * @date 2020年3月24日
 */
@Data
public class SysPasswordForm {
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

}
