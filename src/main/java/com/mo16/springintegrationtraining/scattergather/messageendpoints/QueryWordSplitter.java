package com.mo16.springintegrationtraining.scattergather.messageendpoints;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Arrays;
import java.util.List;

@MessageEndpoint
public class QueryWordSplitter {
    @ServiceActivator(inputChannel = "searchQueriesSinkChannel", outputChannel = "wordsSinkChannel")
    public List<String> wordSplitter(String queryString) {
        System.out.println(Arrays.toString(queryString.split("[^\\w]")));
        return List.of(queryString.split("[^\\w]"));
    }
}
