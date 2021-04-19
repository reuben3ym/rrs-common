package com.rrs.common.remote.feign.fallback;

import com.rrs.common.core.Result;
import com.rrs.common.core.base.LogInfo;
import com.rrs.common.remote.feign.RemoteLogService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 * @date
 */
@Slf4j
@Component
public class RemoteLogServiceFallbackImpl implements RemoteLogService {

    @Setter
    private Throwable cause;

    /**
     * 保存日志
     *
     * @param logInfo
     * @return Result
     */
    @Override
    public Result save(LogInfo logInfo, String inner) {
        log.error("feign 插入日志失败", cause);
        return null;
    }

}
