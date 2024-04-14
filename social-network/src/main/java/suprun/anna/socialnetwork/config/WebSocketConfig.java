package suprun.anna.socialnetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Bean
	public HandshakeInterceptor httpSessionHandshakeInterceptor() {
		return new HttpSessionHandshakeInterceptor();
	}

	@Bean
	public TaskScheduler heartbeatScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("heartbeat-scheduler-");
		scheduler.setPoolSize(1);
		return scheduler;
	}

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat")
				.setAllowedOriginPatterns("*")
				.withSockJS()
				.setInterceptors(httpSessionHandshakeInterceptor())
				.setWebSocketEnabled(true)
				.setSessionCookieNeeded(true)
				.setTaskScheduler(heartbeatScheduler())
		;
	}
}
