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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Http2ClientExample {

    private static Logger logger = Logger.getLogger(Http2ClientExample.class.getName());

    private static ExecutorService executor = ExecutorServiceHelper.getExecutorService(6, logger);

    public static void main(String[] args) throws Exception {
        logger.info("Running HTTP/2 example...");
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(Version.HTTP_2)
                    .build();

            long start = System.currentTimeMillis();

            HttpRequest mainRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://http1.akamai.com/demo/h2_demo_frame.html"))
                    .build();

            HttpResponse<String> mainResponse = httpClient.send(mainRequest, BodyHandlers.ofString());

            logger.info("Main response status code: " + mainResponse.statusCode());
            logger.info("Main response headers: " + mainResponse.headers());
            String responseBody = mainResponse.body();
            logger.info(responseBody);

            List<Future<?>> futures = new ArrayList<>();

            // For each image resource in the main HTML, send a request on a separate thread
            responseBody.lines()
                    .filter(line -> line.trim().startsWith("<img height"))
                    .map(line -> line.substring(line.indexOf("src='") + 5, line.indexOf("'/>")))
                    .forEach(image -> {

                        Future<?> imgFuture = executor.submit(() -> {
                            HttpRequest imgRequest = HttpRequest.newBuilder()
                                    .uri(URI.create("https://http2.akamai.com" + image))
                                    .build();
                            try {
                                HttpResponse<String> imageResponse = httpClient.send(imgRequest, BodyHandlers.ofString());
                                logger.info("[" + Thread.currentThread().getName() + "] " + "Loaded " + image + ", " +
                                        "status code: " + imageResponse.statusCode() + ", " + "Protocol version: " + imageResponse.version());
                            } catch (IOException | InterruptedException ex) {
                                logger.error("Error during image request for " + image, ex);
                            }
                        });
                        futures.add(imgFuture);
                        logger.info("[" + Thread.currentThread().getName() + "] " + "Submitted future for image " + image);
                    });

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
