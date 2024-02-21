package com.tutorial.websocketdemo.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.websocketdemo.client.BinanceClient;
import com.tutorial.websocketdemo.enums.Period;
import com.tutorial.websocketdemo.enums.SocketType;
import com.tutorial.websocketdemo.form.BinanceRequest;
import com.tutorial.websocketdemo.form.SocketRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final BinanceClient binanceClient;

    public SocketHandler(BinanceClient binanceClient) {
        this.binanceClient = binanceClient;
    }

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("Websocket closed");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Websocket established {}", session.getId());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            SocketRequest request = objectMapper.readValue(message.getPayload().toString(), SocketRequest.class);

            if (request.getType().equals(SocketType.CRYPTO)) {
                BinanceRequest binanceRequest = new BinanceRequest();
                binanceRequest.setId(1);
                binanceRequest.setMethod(request.getMethod());
                List<String> params = request.getTickers().stream().map((c) -> {
                    return c.toLowerCase() + "@"
                            + Period.getPeriodByTypeAndPeriod(request.getType(), request.getPeriod());
                }).collect(Collectors.toList());
                binanceRequest.setParams(params);

                binanceClient.sendMessage(session, binanceRequest);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Error {}", exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
