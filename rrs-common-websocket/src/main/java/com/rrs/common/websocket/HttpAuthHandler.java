package com.rrs.common.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrs.common.core.security.SecurityUser;
import com.rrs.common.core.util.SpringContextUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class HttpAuthHandler extends TextWebSocketHandler {

    public final static String MSG_HEART_CHECK = "heartCheck";
    public final static String MSG_MSG = "msg";
    public final static String MSG_CMD = "cmd";

    /**
     * 用户进入系统监听
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object token = session.getAttributes().get("token");
        Object userName = session.getAttributes().get("userName");
        TokenStore tokenStore = SpringContextUtils.getBean(TokenStore.class);
        String username = getUsername(tokenStore, (String) token);

        if (userName.toString().equals(username)) {
            // 用户信息保存
            WsSessionManager.add(username, session);
        }else {
            throw new RuntimeException("用户登录失效");
        }
    }

    private String getUsername(TokenStore tokenStore, String tokenValue) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
        if (authentication == null) {
            throw new InvalidTokenException("Invalid access token: " + tokenValue);
        } else {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            if (user == null) {
                throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
            } else {
                return user.getUsername();
            }
        }
    }

    /**
     * 接受消息事件
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        WsMessage wsMessage = mapper.readValue(payload, WsMessage.class);

        if(wsMessage.getCode().equals(MSG_HEART_CHECK)) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(wsMessage)));
        } else if(wsMessage.getCode().equals(MSG_MSG)) {

        } else {
            System.out.println("消息不能被处理");
        }
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object name = session.getAttributes().get("userName");
        if (name != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(name.toString());
        }
    }

}