package com.example.httpclient;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static com.example.utils.ConsoleHelper.blue;
import static com.example.utils.ConsoleHelper.green;
import static com.example.utils.ConsoleHelper.red;

public class HttpClientAsyncExample {

    private static Logger logger = Logger.getLogger(HttpClientAsyncExample.class.getName());

    /**
     * This example requires local server on port 3010.
     * The server repo can be found at https://github.com/GenadiIvanov/httpserver-devbg
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info(red("HttpClient async demonstration"));
        long startTime = System.currentTimeMillis();

        displayLecturesContent();

        long endTime = System.currentTimeMillis();
        logger.info(red("It took us: " + (endTime - startTime) + " milliseconds"));
    }

    private static void displayLecturesContent() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest lecturesRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:3010/api/lectures"))
                .build();
        HttpResponse<String> listOfLectures = httpClient.send(lecturesRequest, HttpResponse.BodyHandlers.ofString());
        JSONArray jsonArray = new JSONArray(listOfLectures.body());
        logger.info(blue("List of lectures: ") + jsonArray.toString(2));

        List<String> lectureIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            lectureIds.add(jsonArray.getJSONObject(i).getString("id"));
        }

        lectureIds.forEach(id -> {
            HttpRequest lectureContentRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:3010/api/lectures/" + id + "/content?delay=1"))
                    .build();

            HttpResponse<String> lectureContent = null;
            try {
                lectureContent = httpClient.send(lectureContentRequest, HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException | IOException e) {
                logger.error("Error occurred: " + e.getMessage());
            }
            logger.info(green("Lecture content: ") + new JSONObject(lectureContent.body()).toString(2));
        });
    }
}
