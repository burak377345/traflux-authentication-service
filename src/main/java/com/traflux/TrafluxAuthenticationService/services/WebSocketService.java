package com.traflux.TrafluxAuthenticationService.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traflux.TrafluxAuthenticationService.WebSocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class WebSocketService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendUpdate(Object data) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(data);
            WebSocketHandler.broadcast(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
