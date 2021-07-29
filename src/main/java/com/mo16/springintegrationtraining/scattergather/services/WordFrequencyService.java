package com.mo16.springintegrationtraining.scattergather.services;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface WordFrequencyService {
    void addWord(String word);
    void forEachRecord(BiConsumer<String,Integer> consumer);
    void onWordAddition(BiConsumer<String,Integer> consumer);
    int getWordFrequency(String word);

}

