package com.wccgroup.challenge.service;

import com.wccgroup.challenge.domain.model.Postcode;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IPostcodeService {
    Postcode getPostcode(String id);
    @Async
    CompletableFuture<Postcode> getPostcodeAsync(String id);
    boolean addPostcode(Postcode postcode);
    boolean setPostcode(Postcode postcode);
}
