package com.mo16.springintegrationtraining.distrebutedscattergather.querypublisher;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "queriesSink")
public interface QueryPublisherGateway {
    @Gateway
    void publish(String queryString);
}