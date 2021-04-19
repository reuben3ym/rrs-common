package com.rrs.common.core.base;

import com.rrs.common.core.Result;

public interface UserInfoService {
    Result<UserInfo> info(String userId, String inner);
}
