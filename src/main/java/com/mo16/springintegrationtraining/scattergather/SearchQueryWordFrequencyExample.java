package com.mo16.springintegrationtraining.scattergather;

import com.mo16.springintegrationtraining.scattergather.messaginggatways.SearchQueriesPublisherGateway;
import com.mo16.springintegrationtraining.scattergather.repositories.WordFrequencyRepo;
import com.mo16.springintegrationtraining.scattergather.services.WordFrequencyService;
import com.mo16.springintegrationtraining.scattergather.services.WordFrequencyServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

import java.util.stream.Stream;

@SpringBootApplication
@EnableIntegration
public class SearchQueryWordFrequencyExample {
    public static void main(String[] args) {
        SpringApplication.run(SearchQueryWordFrequencyExample.class, args);
    }

    @Bean
    MessageChannel searchQueriesSinkChannel() {
        return new DirectChannel();
    }

    @Bean
    MessageChannel wordsSinkChannel() {
        return new DirectChannel();
    }

    @Bean
    MessageChannel wordsAggregationSinkChannel() {
        return new DirectChannel();
    }

    @Bean
    WordFrequencyService wordFrequencyService(WordFrequencyRepo repo){
        WordFrequencyServiceImpl service = new WordFrequencyServiceImpl(repo);
        service.onWordAddition((word, freq) -> System.out.println(word + "-> "+ freq));
        return service;
    }

    @Bean
    CommandLineRunner runner(SearchQueriesPublisherGateway publisher) {
        return args -> Stream.of("how data is\\stored in cassandra", "microservices design patterns"
                , "how to do Transactional messaging", "I am hungry").forEach(publisher::send);
    }




}
