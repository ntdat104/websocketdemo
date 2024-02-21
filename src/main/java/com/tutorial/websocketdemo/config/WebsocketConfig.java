package com.tutorial.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.tutorial.websocketdemo.client.BinanceClient;
import com.tutorial.websocketdemo.handler.SocketHandler;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private final BinanceClient binanceClient;

    public WebsocketConfig(BinanceClient binanceClient) {
        this.binanceClient = binanceClient;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(binanceClient), "/ws").setAllowedOrigins("*");
    }
    
}
