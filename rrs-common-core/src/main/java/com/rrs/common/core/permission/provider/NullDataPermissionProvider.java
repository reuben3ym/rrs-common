package com.rrs.common.core.permission.provider;

import com.rrs.common.core.permission.wrapper.PermissionWrapper;

/**
 * 空的DataPermissionProvider，可以用于测试
 *
 * @author zjm
 */
public class NullDataPermissionProvider extends AbstractDataPermissionProvider {
    @Override
    public PermissionWrapper wrap(PermissionWrapper permissionWrapper) {
        return permissionWrapper;
    }
}
