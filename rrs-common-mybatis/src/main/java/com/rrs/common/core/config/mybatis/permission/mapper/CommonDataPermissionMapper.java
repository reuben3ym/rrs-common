package com.rrs.common.core.config.mybatis.permission.mapper;

import com.rrs.common.core.config.mybatis.permission.CommonDataPermissionVO;

import java.util.List;

/**
 * @author zjm
 */
public interface CommonDataPermissionMapper {
    /**
     * 查询数据权限信息
     *
     * @param commonDataPermissionVO
     * @return
     */
    List<CommonDataPermissionVO> selectDataPermissions(CommonDataPermissionVO commonDataPermissionVO);
}
