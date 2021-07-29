package com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.services;

import java.util.function.BiConsumer;

public interface WordFrequencyService {
    void addWord(String word);
    void forEachRecord(BiConsumer<String,Integer> consumer);
    void onWordAddition(BiConsumer<String,Integer> consumer);
    int getWordFrequency(String word);

}

