package com.fz.server.config;


import com.fz.server.config.security.component.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/12/8:49
 * @Description: WebSocket配置类
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 添加这个Endpoint 这样在网页可以通过websocket连接上服务
     * 也就是配置websocket的一个服务地址，并且可以指定是否使用socketjs
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        /**
         * 将ws/ep路径注册为stomp的端点，用户连接者这个端点就可以进行websocket通讯，支持socketJs
         * setAllowedOrigins 允许跨域 *表示所有连接
         * withSockJS 支持socketJs访问
         */
        stompEndpointRegistry.addEndpoint("/ws/ep").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration webSocketTransportRegistration) {

    }

    /**
     * 如果不使用JWT时 则不需要进行该方法的配置 被登录拦截器拦截
     * 输入通道参数配置
     *
     * @param channelRegistration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
        channelRegistration.setInterceptors(new ChannelInterceptor() {
            /**
             * 域发送
             * @param message
             * @param messageChannel
             * @return
             */
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                /**
                 * 使用accessor判断是否为链接
                 *如果是链接 需要获取token 并且设置用户对象
                 */
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    if(!StringUtils.isEmpty(token)){
                        String authToken = token.substring(tokenHead.length());
                        //token中存在用户名
                        String userName = jwtTokenUtils.getUserNameFromToken(token);
                        if(!StringUtils.isEmpty(userName)){
                            /**
                             * 登录将登录信息写进Redis
                             */
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                            /**
                             * 判断是否token有用 重新设置用户对象
                             */
                            if(jwtTokenUtils.validateToken(authToken,userDetails)){
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                accessor.setUser(authenticationToken);
                            }
                        }
                    }
                }
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {

            }

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {

            }

            @Override
            public boolean preReceive(MessageChannel messageChannel) {
                return false;
            }

            @Override
            public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
                return null;
            }

            @Override
            public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {

            }


        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {

    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> list) {
        return false;
    }

    /**
     * 配置消息代理
     * @param messageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        /**
         * 配置代理域，可以配置多个，配置代理的目的为：queue 前缀为queue
         * 可以在配置域上向客户端推送消息
         */
        messageBrokerRegistry.enableSimpleBroker("/queue");
    }
}
