package com.example.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpClientExamples {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        System.out.println("Example code snippets for HTTPClient API in Java 11 DevBg");
        performHttpRequest();
    }

    private static void performHttpRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                //.proxy(ProxySelector.of(new InetSocketAddress("www-proxy.com", 8080)))
                //.authenticator(Authenticator.getDefault())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3010/api/public"))
                .GET()   // this is the default
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response headers: " + response.headers());
        System.out.println("Response body: " + response.body());
    }
}
