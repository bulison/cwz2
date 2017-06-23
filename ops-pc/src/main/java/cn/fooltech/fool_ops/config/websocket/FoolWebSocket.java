package cn.fooltech.fool_ops.config.websocket;

import cn.fooltech.fool_ops.domain.message.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class FoolWebSocket implements WebSocketConfigurer {

    @Autowired
    public PushService pushService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(foolWebSocketHandler(), "/push").withSockJS();
    }

    @Bean
    public WebSocketHandler foolWebSocketHandler() {
        return new FoolWebSocketHandler(pushService);
    }

    /**
     * Detects beans of type ServerEndpointConfig and registers with the standard Java WebSocket runtime. Also detects beans annotated with ServerEndpoint and registers them as well. Although not required, it is likely annotated endpoints should have their configurator property set to SpringConfigurator.
     * When this class is used, by declaring it in Spring configuration, it should be possible to turn off a Servlet container's scan for WebSocket endpoints. This can be done with the help of the <absolute-ordering> element in web.xml.
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
