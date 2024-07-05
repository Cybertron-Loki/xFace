package project.test.xface.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置消息代理前缀，客户端订阅消息时需要加上这个前缀
        registry.enableSimpleBroker("/topic/group_invitation_apply","/topic/friend_invitation_apply","/topic/group_apply","/topic/group_message");
        // 客户端向服务器发送消息的前缀，例如客户端发送 `/app/sendMessage` 则会进入 @MessageMapping("/sendMessage") 方法处理
//        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 客户端建立 WebSocket 连接的端点
        registry.addEndpoint("/websocket-endpoint").withSockJS();
    }
}
