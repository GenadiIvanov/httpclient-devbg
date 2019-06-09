package com.example.httpclient;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class HttpCustomSubscriberExamples {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        System.out.println("Custom Subscriber example");
        performHttpRequest();
    }

    private static void performHttpRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3010/api/file"))
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, responseInfo -> new MD5Subscriber());
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response Content-type header: " + response.headers().firstValue("content-type").get());
        System.out.println("Response Content-type header: " + response.headers().firstValue("content-length").get());
        System.out.println("Response md5 digest: " + DatatypeConverter.printHexBinary(response.body()));
    }
}
