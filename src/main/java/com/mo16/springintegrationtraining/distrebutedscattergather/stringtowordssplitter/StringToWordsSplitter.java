package com.mo16.springintegrationtraining.distrebutedscattergather.stringtowordssplitter;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;


@MessageEndpoint
public class StringToWordsSplitter {
    private final WordsPublisherGateway wordsPublisher;

    StringToWordsSplitter(WordsPublisherGateway wordsPublisher) {
        this.wordsPublisher = wordsPublisher;
    }

    @ServiceActivator(inputChannel = "queriesSink")
    public void stringSplitter(String queryString) {
        System.out.println(queryString);
        for (String word : queryString.split("[^\\w]")) wordsPublisher.publish(word);
    }
}
