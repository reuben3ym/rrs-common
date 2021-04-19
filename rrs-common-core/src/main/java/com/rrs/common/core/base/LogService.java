package com.rrs.common.core.base;

import com.rrs.common.core.Result;

public interface LogService {
    Result save(LogInfo logInfo, String inner);
}
