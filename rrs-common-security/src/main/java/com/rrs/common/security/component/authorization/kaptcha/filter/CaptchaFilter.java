package com.rrs.common.security.component.authorization.kaptcha.filter;

import cn.hutool.core.util.ObjectUtil;
import com.rrs.common.core.Result;
import com.rrs.common.core.base.UserInfo;
import com.rrs.common.core.base.UserInfoService;
import com.rrs.common.core.constant.CacheConstants;
import com.rrs.common.core.constant.SecurityConstants;
import com.rrs.common.core.exception.BaseException;
import com.rrs.common.core.redis.util.RedisUtil;
import com.rrs.common.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zjm
 */
public class CaptchaFilter extends OncePerRequestFilter {

    @Value("${rrs.captcha.clients}")
    private List<String> clients;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 只处理登录请求和需要验证码验证的client_id
        if (StringUtils.containsIgnoreCase(request.getServletPath(), SecurityConstants.OAUTH_TOKEN_URL)) {
            Authentication authentication = SecurityUtils.getAuthentication();
            String clientId = authentication != null ? authentication.getName() : null;
            if (clientId != null && clients.contains(clientId)) {
                // 2023-12-11 wzg提出方案：参数isCaptcha = false 不校验验证码
                String isCaptcha = request.getParameter("isCaptcha");
                if (!"false".equals(isCaptcha)) {
                    validateCaptcha(request);
                }
            }

            String username = request.getParameter("username");
            UserInfoService userInfoService = SpringContextUtils.getBean("sysUserServiceImpl");
            Result<UserInfo> result = userInfoService.info(username, SecurityConstants.INNER_TRUE);
            String status = result.getData().getStatus();
            if (ObjectUtil.equal("2", status)) {
                throw new BaseException("当前用户被锁定，请联系系统管理员！");
            }

            String password = request.getParameter("password");
            String passwordStr = result.getData().getPassword();
            if (!PasswordUtil.matchesPassword(password, passwordStr)) {
                String key = CacheConstants.OAUTH_NUMBS + username;
                int max = 5;
                boolean hasKey = redisUtil.hasKey(key);
                if (!hasKey) {
                    int tokenNumbs = 1;
                    redisUtil.set(key, tokenNumbs, 60 * 60 * 60);
                    throw new BaseException("您还有" + (max - tokenNumbs) + "次机会，输错" + max + "次账号将被锁定！");
                } else {
                    int tokenNumbs = (int) redisUtil.get(CacheConstants.OAUTH_NUMBS + username);
                    if (tokenNumbs < (max - 1)) {
                        tokenNumbs += 1;
                        redisUtil.set(key, tokenNumbs, 60 * 60 * 60);
                        throw new BaseException("您还有" + (max - tokenNumbs) + "次机会，输错" + max + "次账号将被锁定！");
                    } else {
                        userInfoService.disable(username);
                        throw new BaseException("您的账号已被锁定，请联系系统管理员！");
                    }

                }
            }

        }

        filterChain.doFilter(request, response);
    }

    public void disable(String userId) {
        String key = CacheConstants.OAUTH_NUMBS + userId;
        if (redisUtil.hasKey(key)) {
            redisUtil.del(key);
        }
    }

    /**
     * 验证验证码合法性
     *
     * @param request
     * @return
     */
    private void validateCaptcha(HttpServletRequest request) {
        String captcha = request.getParameter("captcha");
        String uuid = request.getParameter("uuid");
        CommonUtil.isEmptyStr(uuid, "验证码uuid不能为空");
        CommonUtil.isEmptyStr(captcha, "验证码不能为空");

        String cacheCaptcha = (String) redisUtil.get(CacheConstants.CAPTCHA + uuid);
        if (!captcha.equals(cacheCaptcha)) {
            throw new BaseException("验证码错误或已失效");
        }
        // 验证码验证通过后，应立即删除缓存，防止恶意暴力破解密码
        redisUtil.del(CacheConstants.CAPTCHA + uuid);
    }
}