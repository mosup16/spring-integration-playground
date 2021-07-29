package com.mo16.springintegrationtraining.scattergather.messageendpoints;

import com.mo16.springintegrationtraining.scattergather.services.WordFrequencyService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class WordsFrequencyAggregator {
    private final WordFrequencyService wordFrequencyService;

    WordsFrequencyAggregator(WordFrequencyService wordFrequencyService) {
        this.wordFrequencyService = wordFrequencyService;
    }

    @ServiceActivator(inputChannel = "wordsAggregationSinkChannel")
    public void wordSplitter(Message<String> word) {
        wordFrequencyService.addWord(word.getPayload());
    }
}
