package com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter;


import com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.repositories.WordFrequencyRepo;
import com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.services.WordFrequencyService;
import com.mo16.springintegrationtraining.distrebutedscattergather.wordfrequencycounter.services.WordFrequencyServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.kafka.inbound.KafkaMessageSource;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.messaging.MessageChannel;

import java.util.Properties;


// gets words from kafka Topic named words ,
// and then uses its internal store to update the word frequency
@EnableIntegration
@SpringBootApplication
public class WordFrequencyCounterServiceApp {
    @Value("${app.integration.topic.words}")
    private String words;

    @Value("${app.integration.broker.host}")
    private String kafkaBootstrapServer;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("server.port" ,"0");
        props.setProperty("app.integration.topic.words" ,"words");
        props.setProperty("app.integration.broker.host" ,"mo:9092");
        new SpringApplicationBuilder(WordFrequencyCounterServiceApp.class).properties(props).run(args);
    }
    @Bean
    MessageChannel wordFrequencyCounterChannel() {
        return new DirectChannel();
    } //inbound channel

    @Bean
    WordFrequencyService wordFrequencyService(WordFrequencyRepo repo){
        WordFrequencyServiceImpl service = new WordFrequencyServiceImpl(repo);
        service.onWordAddition((word, freq) -> System.out.println(word + "-> "+ freq));
        return service;
    }

    @Bean
    @InboundChannelAdapter(channel = "wordFrequencyCounterChannel" , poller = @Poller(fixedRate = "10"))
    KafkaMessageSource<String,String> wordsSource(ConsumerFactory<String,String> cf){
        ConsumerProperties cp = new ConsumerProperties(words);
        cp.setGroupId("Word-frequency-counter");
        cp.setKafkaConsumerProperties(getWordsConsumerProperties());
        return new KafkaMessageSource<>(cf, cp);
    }

    @NotNull
    private Properties getWordsConsumerProperties() {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ,kafkaBootstrapServer);
        return props;
    }
}
