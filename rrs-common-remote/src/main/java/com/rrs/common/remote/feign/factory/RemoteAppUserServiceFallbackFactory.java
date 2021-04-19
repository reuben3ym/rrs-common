package com.rrs.common.remote.feign.factory;

import com.rrs.common.remote.feign.RemoteAppUserService;
import com.rrs.common.remote.feign.fallback.RemoteAppUserServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 * @date
 */
@Component
public class RemoteAppUserServiceFallbackFactory implements FallbackFactory<RemoteAppUserService> {

    @Override
    public RemoteAppUserService create(Throwable throwable) {
        RemoteAppUserServiceFallbackImpl remoteUserServiceFallback = new RemoteAppUserServiceFallbackImpl();
        remoteUserServiceFallback.setCause(throwable);
        return remoteUserServiceFallback;
    }

}
