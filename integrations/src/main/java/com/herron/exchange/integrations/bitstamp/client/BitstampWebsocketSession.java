package com.herron.exchange.integrations.bitstamp.client;

import com.herron.exchange.common.api.common.websocket.WebsocketMessageWrapper;
import com.herron.exchange.common.api.common.websocket.api.WebsocketSession;
import com.herron.exchange.integrations.bitstamp.messages.model.BitstampEvent;

import java.util.Optional;

public class BitstampWebsocketSession extends WebsocketSession {

    public BitstampWebsocketSession(String uri) {
        super(uri);
    }

    @Override
    protected Optional<WebsocketMessageWrapper> extractWrapper(CharSequence charSequence) {
        if (charSequence.isEmpty()) {
            return Optional.empty();
        }
        var event = BitstampEvent.from(charSequence.toString());
        return Optional.of(new WebsocketMessageWrapper(event.createKey(uri.toString()), event));
    }
}