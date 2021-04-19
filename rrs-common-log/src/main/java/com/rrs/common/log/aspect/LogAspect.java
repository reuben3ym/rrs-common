package com.rrs.common.log.aspect;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rrs.common.core.Result;
import com.rrs.common.core.base.LogInfo;
import com.rrs.common.core.constant.SecurityConstants;
import com.rrs.common.core.threadpool.AsyncThreadExecutorProperties;
import com.rrs.common.core.threadpool.manager.AsyncManager;
import com.rrs.common.core.util.JacksonUtil;
import com.rrs.common.core.util.SpringContextUtils;
import com.rrs.common.log.annotation.Log;
import com.rrs.common.log.util.SysLogUtils;
import com.rrs.common.core.base.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作日志
 *
 * @author zjm
 */
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint point, Log log) throws Throwable {
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        LogInfo logInfo = SysLogUtils.getLogInfo();
        logInfo.setLogContent(log.value());
        logInfo.setLogType("2");
        logInfo.setMethod(className + "." + methodName + "()");
        logInfo.setRequestParam(paramsToJson(point.getArgs()));
        Long startTime = System.currentTimeMillis();

        Object result = null;
        Exception ex = null;
        try {
            result = point.proceed();
        } catch (Exception e) {
            ex = e;
        }
        Long endTime = System.currentTimeMillis();
        logInfo.setCostTime(endTime - startTime);

        String operateResult = "";
        if (result instanceof Result) {
            Result r = (Result) result;
            operateResult = r.getMsg();
        } else if (ex != null) {
            operateResult = ex.getMessage();
        }

        logInfo.setOperateResult(operateResult);

        AsyncThreadExecutorProperties asyncThreadExecutorProperties =
                SpringContextUtils.getBeanIgnoreNotFound(AsyncThreadExecutorProperties.class);
        if (asyncThreadExecutorProperties != null && asyncThreadExecutorProperties.getEnabled()) {
            // 异步方式保存日志
            AsyncManager.me().execute(new Runnable() {
                @Override
                public void run() {
                    SpringContextUtils.getBean(LogService.class).save(logInfo, SecurityConstants.INNER_TRUE);
                }
            });
        } else {
            // 同步方式保存系统日志
            SpringContextUtils.getBean(LogService.class).save(logInfo, SecurityConstants.INNER_TRUE);
        }

        if (ex != null) {
            throw ex;
        }

        return result;
    }

    /**
     * 将参数转换成json格式字符串，多个参数以逗号隔开
     *
     * @param args
     * @return
     */
    private String paramsToJson(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuffer params = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            String param = null;
            if (!(args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse)) {
                // 序列化忽略null值，写入数据库长度可以缩短
                param = JacksonUtil.objToStr(args[i], Include.NON_NULL);
            }
            if (param == null) {
                param = "{}";
            }
            if (i == args.length - 1) {
                params.append(param);
            } else {
                params.append(param).append(",");
            }
        }
        return params.toString();
    }

}
