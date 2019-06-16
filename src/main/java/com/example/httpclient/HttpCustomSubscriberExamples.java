package com.example.httpclient;

import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.example.utils.ConsoleHelper.blue;
import static com.example.utils.ConsoleHelper.cyan;
import static com.example.utils.ConsoleHelper.green;
import static com.example.utils.ConsoleHelper.red;

public class HttpCustomSubscriberExamples {

    private static Logger logger = Logger.getLogger(HttpCustomSubscriberExamples.class.getName());

    private static final String LARGE_FILE_URI = "http://localhost:3010/api/file";

    /**
     * This example requires local server on port 3010.
     * The server repo can be found at https://github.com/GenadiIvanov/httpserver-devbg
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info(red("Custom Subscriber example..."));
        performHttpRequest();
    }

    private static void performHttpRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LARGE_FILE_URI))
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, responseInfo -> new MD5Subscriber());
        logger.info(green("Response status code: ") + response.statusCode());
        logger.info(green("Response 'Content-type' header: ") + response.headers().firstValue("content-type").orElse(""));
        logger.info(green("Response 'Content-length' header: ") + red(response.headers().firstValue("content-length").orElse("")));
        logger.info(blue("Response MD5 digest: ") + cyan(DatatypeConverter.printHexBinary(response.body())));
    }
}
