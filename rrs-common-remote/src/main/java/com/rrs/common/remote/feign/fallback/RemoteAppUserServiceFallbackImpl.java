package com.rrs.common.remote.feign.fallback;

import com.rrs.common.core.Result;
import com.rrs.common.core.base.UserInfo;
import com.rrs.common.remote.feign.RemoteAppUserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 * @date
 */
@Slf4j
@Component
public class RemoteAppUserServiceFallbackImpl implements RemoteAppUserService {
    @Setter
    private Throwable cause;

    /**
     * 通过用户名查询用户信息
     *
     * @param userId 用户名
     * @return Result
     */
    @Override
    public Result<UserInfo> info(String userId, String inner) {
        log.error("feign 查询用户信息失败:{}", userId, cause);
        return null;
    }

    @Override
    public void disable(String userId) {
        log.error("feign 查询用户信息失败:{}", userId, cause);
    }
}
