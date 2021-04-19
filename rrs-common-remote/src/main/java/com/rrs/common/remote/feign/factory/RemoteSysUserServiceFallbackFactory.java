package com.rrs.common.remote.feign.factory;

import com.rrs.common.remote.feign.RemoteSysUserService;
import com.rrs.common.remote.feign.fallback.RemoteSysUserServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 * @date
 */
@Component
public class RemoteSysUserServiceFallbackFactory implements FallbackFactory<RemoteSysUserService> {

    @Override
    public RemoteSysUserService create(Throwable throwable) {
        RemoteSysUserServiceFallbackImpl remoteUserServiceFallback = new RemoteSysUserServiceFallbackImpl();
        remoteUserServiceFallback.setCause(throwable);
        return remoteUserServiceFallback;
    }

}
