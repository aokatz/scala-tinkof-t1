package org.tinkoff.task.one.service;

import org.tinkoff.task.one.model.ApplicationStatusResponse;
import org.tinkoff.task.one.model.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HandlerImpl implements Handler {

    private final Client client;

    public HandlerImpl(Client client) {
        this.client = client;
    }

    @Override
    public ApplicationStatusResponse performOperation(String id) {
        CompletableFuture<Response> appStatusResp1 = CompletableFuture.supplyAsync(() -> client.getApplicationStatus1(id));
        CompletableFuture<Response> appStatusResp2 = CompletableFuture.supplyAsync(() -> client.getApplicationStatus2(id));
        CompletableFuture<ApplicationStatusResponse> responseCompletableFuture = appStatusResp1.applyToEitherAsync(appStatusResp2, this::toAppResponse);

        try {
            return responseCompletableFuture
                    .get(15L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private ApplicationStatusResponse toAppResponse(Response response) {
        if (response instanceof Response.Success success) {
            return new ApplicationStatusResponse.Success(success.applicationId(), success.applicationStatus());
        }
        if (response instanceof Response.Failure failure) {
            return new ApplicationStatusResponse.Failure(null, 0);
        }
        if (response instanceof Response.RetryAfter) {
            //TODO Implement retry
            return new ApplicationStatusResponse.Failure(null, 0);
        }
        return new ApplicationStatusResponse.Failure(null, 0);
    }


}
