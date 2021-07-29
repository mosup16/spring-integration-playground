package com.mo16.springintegrationtraining.scattergather.messaginggatways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "wordsAggregationSinkChannel")
public interface WordsPublisherGateway {
    @Gateway
    void send(String queryString);
}
