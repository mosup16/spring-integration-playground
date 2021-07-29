package com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter;

import com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.services.WordFrequencyService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class WordsFrequencyAggregator {
    private final WordFrequencyService wordFrequencyService;

    WordsFrequencyAggregator(WordFrequencyService wordFrequencyService) {
        this.wordFrequencyService = wordFrequencyService;
    }

    @ServiceActivator(inputChannel = "wordFrequencyCounterChannel")
    public void wordSplitter(String word) {
        System.out.println(word);
        wordFrequencyService.addWord(word);
    }

}
