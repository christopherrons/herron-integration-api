package com.herron.exchange.integrations.bitstamp;

import com.herron.exchange.common.api.common.websocket.WebsocketHandler;
import com.herron.exchange.common.api.common.websocket.api.WebsocketRequest;
import com.herron.exchange.common.api.common.websocket.api.WebsocketSession;
import com.herron.exchange.integrations.bitstamp.client.BitstampWebsocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitstampWebsocketClient extends WebsocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitstampWebsocketClient.class);

    public BitstampWebsocketClient() {
        super();
    }

    @Override
    protected WebsocketSession createSession(WebsocketRequest request) {
        return new BitstampWebsocketSession(request.uri());
    }
}
