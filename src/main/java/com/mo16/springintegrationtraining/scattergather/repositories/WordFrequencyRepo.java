package com.mo16.springintegrationtraining.scattergather.repositories;

import java.util.function.BiConsumer;

public interface WordFrequencyRepo {
    void addWord(String word);
    void forEachRecord(BiConsumer<String,Integer> consumer);
    int getWordFrequency(String word);
}



