package dev.phong.webChat.handler.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class StompClientHandler extends StompSessionHandlerAdapter {

    private String roomId;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to WebSocket!");
        session.subscribe("/topic/" + this.roomId, this);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received: " + payload);

    }
}
