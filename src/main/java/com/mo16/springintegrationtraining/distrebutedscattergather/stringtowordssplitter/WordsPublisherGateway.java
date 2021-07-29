package com.mo16.springintegrationtraining.distrebutedscattergather.stringtowordssplitter;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "wordsSink")
public interface WordsPublisherGateway {
    @Gateway
    void publish(String word);
}
