package com.mo16.springintegrationtraining.scattergather.messageendpoints;


import com.mo16.springintegrationtraining.scattergather.messaginggatways.WordsPublisherGateway;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

@MessageEndpoint
public class WordsSplitter {
    private final WordsPublisherGateway wordsPublisher;

    WordsSplitter(WordsPublisherGateway wordsPublisher) {
        this.wordsPublisher = wordsPublisher;
    }

    @ServiceActivator(inputChannel = "wordsSinkChannel", outputChannel = "wordsAggregationSinkChannel")
    public void wordSplitter(List<String> words) {
        words.forEach(wordsPublisher::send);
    }
}
