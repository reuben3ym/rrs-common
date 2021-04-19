package com.rrs.common.remote.feign;

import com.rrs.common.core.Result;
import com.rrs.common.core.base.LogInfo;
import com.rrs.common.core.base.LogService;
import com.rrs.common.core.constant.SecurityConstants;
import com.rrs.common.core.constant.ServiceNameConstants;
import com.rrs.common.remote.feign.factory.RemoteLogServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author zjm
 * @date
 */
@FeignClient(path = "sys/log", contextId = "remoteLogService", value = ServiceNameConstants.SYS_SERVICE,
        fallbackFactory = RemoteLogServiceFallbackFactory.class)
public interface RemoteLogService extends LogService {

    /**
     * 保存日志
     *
     * @param logInfo
     * @return
     */
    @Override
    @PostMapping("/save")
    Result save(@RequestBody LogInfo logInfo, @RequestHeader(SecurityConstants.INNER) String inner);

}
