package com.example.httpclient;

import com.example.utils.ExecutorServiceHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Http2PushExample {

    private static Logger logger = Logger.getLogger(Http2PushExample.class.getName());
    private static ExecutorService executor = ExecutorServiceHelper.getExecutorService(20, logger);

    private static final String SERVER_PUSH_NUMBER = "30";
    private static final String SERVER_PUSH_URI = "https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum="
            + SERVER_PUSH_NUMBER;

    public static void main(String[] args) {
        logger.info("Running HTTP/2 example with push promises...");
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(Version.HTTP_2)
                    .build();

            long start = System.currentTimeMillis();

            HttpRequest mainRequest = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_PUSH_URI))
                    .build();

            List<Future<?>> futures = new ArrayList<>();

            Set<String> pushedImages = new HashSet<>();

            httpClient.sendAsync(mainRequest, BodyHandlers.ofString(), (initiatingRequest, pushPromiseRequest, acceptor) -> {
                logger.info("Got promise request " + pushPromiseRequest.uri());
                acceptor.apply(BodyHandlers.ofString()).thenAccept(resp -> {
                    logger.info("Got pushed response " + resp.uri());
                    pushedImages.add(resp.uri().toString());
                });
            }).thenAccept(mainResponse -> {

                logger.info("Main response status code: " + mainResponse.statusCode());
                logger.info("Main response headers: " + mainResponse.headers());
                String responseBody = mainResponse.body();
                logger.info(responseBody);

                // For each image resource in the main HTML, send a request on a separate thread
                responseBody.lines()
                        .filter(line -> line.trim().startsWith("<img height"))
                        .map(line -> line.substring(line.indexOf("src='") + 5, line.indexOf("'/>")))
                        .filter(image -> !pushedImages.contains("https://http2.akamai.com" + image))
                        .forEach(image -> {

                            Future<?> imgFuture = executor.submit(() -> {
                                HttpRequest imgRequest = HttpRequest.newBuilder()
                                        .uri(URI.create("https://http2.akamai.com" + image))
                                        .build();
                                try {
                                    HttpResponse<String> imageResponse = httpClient.send(imgRequest, BodyHandlers.ofString());
                                    logger.info("Loaded " + image + ", status code: " + imageResponse.statusCode());
                                } catch (IOException | InterruptedException ex) {
                                    logger.error("Error during image request for " + image, ex);
                                }
                            });
                            futures.add(imgFuture);
                            logger.info("Submitted future for image " + image);
                        });

            }).join();

            // Wait for all submitted image loads to be completed
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException ex) {
                    logger.error("Error waiting for image load", ex);
                }
            });

            long end = System.currentTimeMillis();
            logger.info("Total load time: " + (end - start) + " ms");

        } finally {
            executor.shutdown();
        }
    }
}