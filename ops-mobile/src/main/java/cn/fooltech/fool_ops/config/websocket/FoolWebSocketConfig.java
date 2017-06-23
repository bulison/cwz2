//package cn.fooltech.fool_ops.config.websocket;
//
//import cn.fooltech.fool_ops.domain.message.service.PushService;
//import cn.fooltech.fool_ops.security.jwt.TokenProvider;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptorAdapter;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.*;
//
//import javax.inject.Inject;
//
//@Configuration
//@EnableWebSocket
//@EnableWebSocketMessageBroker
//public class FoolWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
//
//    @Inject
//    public PushService pushService;
//
//    @Inject
//    private TokenProvider tokenProvider;
//
////    @Bean
////    public SimpMessagingTemplate brokerMessagingTemplate() {
////        SimpMessagingTemplate template = new SimpMessagingTemplate(brokerChannel());
////        String prefix = getBrokerRegistry().getUserDestinationPrefix();
////        if (prefix != null) {
////            template.setUserDestinationPrefix(prefix);
////        }
////    }
//
//
////    @Override
////    public void configureClientInboundChannel(ChannelRegistration registration) {
////        registration.setInterceptors(new ChannelInterceptorAdapter() {
////
////            @Override
////            public Message<?> preSend(Message<?> message, MessageChannel channel) {
////
////                StompHeaderAccessor accessor =
////                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
////
////                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
////                    String jwtToken = accessor.getFirstNativeHeader("Auth");
////                    if (StringUtils.isNotEmpty(jwtToken)) {
////                        Authentication authToken = tokenProvider.getAuthentication(jwtToken);
////                        SecurityContextHolder.getContext().setAuthentication(authToken);
////                        accessor.setUser(authToken);
////                    }
////                }
////
////                return message;
////            }
////        });
////    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic","/queue");  // 推送消息前缀
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user");//推送用户前缀
//    }
//
//    /**
//     * 建立连接的端点
//     * @param registry
//     */
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/stomp").setAllowedOrigins("*").withSockJS();
//    }
//}
