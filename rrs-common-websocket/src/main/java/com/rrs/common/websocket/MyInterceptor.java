package com.rrs.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.Charset;
import java.util.Map;

public class MyInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler
            wsHandler, Map<String, Object> attributes) {
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), Charset.forName("utf-8"));
        String uid = paramMap.get("token");
        String userName = paramMap.get("userName");
        if (StrUtil.isNotBlank(uid) && StrUtil.isNotBlank(userName)) {
            // 放入属性域
            attributes.put("token", uid);
            attributes.put("userName", userName);
            System.out.println("用户 " + userName + " 握手成功！");
            return true;
        }
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        System.out.println("握手完成");
    }

}