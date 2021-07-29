package com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.services;


import com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.repositories.WordFrequencyRepo;

import java.util.function.BiConsumer;

public class WordFrequencyServiceImpl implements WordFrequencyService {
    private final WordFrequencyRepo wordFrequencyRepo;
    private BiConsumer<String, Integer> onWordAdditionHandler;

    public WordFrequencyServiceImpl(WordFrequencyRepo wordFrequencyRepo) {
        this.wordFrequencyRepo = wordFrequencyRepo;
    }

    @Override
    public void addWord(String word) {
        wordFrequencyRepo.addWord(word);
        onWordAdditionHandler.accept(word,getWordFrequency(word));
    }

    @Override
    public void forEachRecord(BiConsumer<String,Integer> consumer) {
        wordFrequencyRepo.forEachRecord(consumer);
    }

    @Override
    public void onWordAddition(BiConsumer<String,Integer> consumer) {
        onWordAdditionHandler = consumer;
    }

    @Override
    public int getWordFrequency(String word) {
        return wordFrequencyRepo.getWordFrequency(word);
    }
}
