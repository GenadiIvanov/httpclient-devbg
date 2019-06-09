package com.example.httpclient;

import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class MD5Subscriber implements HttpResponse.BodySubscriber<byte[]> {

    private final CompletableFuture<byte[]> future = new CompletableFuture<>();

    private MessageDigest digest;

    private Flow.Subscription subscription;

    public MD5Subscriber() {
        try {
            this.digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletionStage<byte[]> getBody() {
        return future;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<ByteBuffer> items) {
        System.out.println(StandardCharsets.UTF_8.decode(items.get(0).duplicate()).toString());
        items.forEach(digest::update);
        subscription.request(10);
    }

    @Override
    public void onError(Throwable throwable) {
        future.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        future.complete(digest.digest());
    }
}
