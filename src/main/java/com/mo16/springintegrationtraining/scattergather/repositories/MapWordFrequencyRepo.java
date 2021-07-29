package com.mo16.springintegrationtraining.scattergather.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.function.BiConsumer;

@Repository
public class MapWordFrequencyRepo implements WordFrequencyRepo {
    private final Map<String, Integer> wordFrequenciesDataStore;

    MapWordFrequencyRepo(Map<String, Integer> wordFrequenciesDataStore) {
        this.wordFrequenciesDataStore = wordFrequenciesDataStore;
    }

    @Override
    public void addWord(String word) {
        Integer count = wordFrequenciesDataStore.get(word);
        wordFrequenciesDataStore.put(word, count != null ? count + 1 : 1);
    }

    @Override
    public void forEachRecord(BiConsumer<String, Integer> consumer) {
        wordFrequenciesDataStore.forEach(consumer);
    }

    @Override
    public int getWordFrequency(String word) {
        return wordFrequenciesDataStore.get(word);
    }
}
