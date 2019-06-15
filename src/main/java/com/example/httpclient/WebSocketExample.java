package com.example.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

import com.example.utils.ExecutorServiceHelper;
import org.apache.log4j.Logger;

import static com.example.utils.ConsoleHelper.*;

public class WebSocketExample {

    private static Logger logger = Logger.getLogger(WebSocketExample.class.getName());
    private static ExecutorService executor = ExecutorServiceHelper.getExecutorService(3, logger);

    private static final String ECHO_WEBSOCKET_URI = "wss://echo.websocket.org";

    public static void main(String[] args) throws Exception {
        logger.info("Running WebSocket example...");

        HttpClient httpClient = HttpClient.newBuilder().executor(executor).build();
        Builder webSocketBuilder = httpClient.newWebSocketBuilder();
        WebSocket webSocket = webSocketBuilder.buildAsync(URI.create(ECHO_WEBSOCKET_URI), new WebSocketListener()).join();
        logger.info(red("WebSocket created"));

        Thread.sleep(1000);
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "OK").thenRun(() -> logger.info(red("Sent close")));
    }


    private static class WebSocketListener implements Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            logger.info(green("CONNECTED"));
            sendTextIfOpened("Hello", webSocket);
            Listener.super.onOpen(webSocket);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            logger.error("Error occurred", error);
            Listener.super.onError(webSocket, error);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            logger.info(cyan("RECEIVED: ") + data);
            sendTextIfOpened("Some important messages", webSocket);
            return Listener.super.onText(webSocket, data, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            logger.info(green("CLOSED [status: " + statusCode + ", reason: " + reason + "]"));
            executor.shutdown();
            return Listener.super.onClose(webSocket, statusCode, reason);
        }

        private void sendTextIfOpened(String text, WebSocket webSocket) {
            if(!webSocket.isOutputClosed()) {
                logger.info(blue("SENT: ") + text);
                webSocket.sendText(text, true);
            }
        }
    }
}