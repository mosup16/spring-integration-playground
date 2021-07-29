package com.mo16.springintegrationtraining.distrebutedscattergather.stringtowordssplitter;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.kafka.inbound.KafkaMessageSource;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * the data flow of the application <br/>
 * queries -> queriesSink -> StringToWordsSplitter -> wordsSink -> words
 *
 * <li> queries , words are : Kafka Topic</li>
 * <li> queriesSink , wordsSink are : Spring Integration channel</li>
 * */
@EnableIntegration
@SpringBootApplication
public class QueryStringSplitterServiceApp {
    @Value("${app.integration.topic.queries}")
    private String queries;
    @Value("${app.integration.topic.words}")
    private String words;

    @Value("${app.integration.broker.host}")
    private String kafkaBootstrapServer;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("server.port" ,"0");
        props.setProperty("app.integration.topic.queries" ,"queries");
        props.setProperty("app.integration.topic.words" ,"words");
        props.setProperty("app.integration.broker.host" ,"mo:9092");
        new SpringApplicationBuilder(QueryStringSplitterServiceApp.class).properties(props).run(args);
    }

    @Bean // data consumed from kafka is published to queriesSink
    @InboundChannelAdapter(channel = "queriesSink")
    KafkaMessageSource<String,String> queriesSource(ConsumerFactory<String ,String> cf){
        ConsumerProperties cp = new ConsumerProperties(queries);
        cp.setKafkaConsumerProperties(getQueriesConsumerProperties());
        cp.setGroupId("QueryStringSplitter");
        return new KafkaMessageSource<String,String>(cf,cp);
    }

    @NotNull
    private Properties getQueriesConsumerProperties() {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ,kafkaBootstrapServer);
        return props;
    }

    @Bean
    MessageChannel queriesSink() { return new DirectChannel(); }

    @Bean
    MessageChannel wordsSink() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "wordsSink")
    MessageHandler wordsKafkaPublisher(KafkaTemplate<String,String> template){
        var mh = new KafkaProducerMessageHandler<>(template);
        mh.setTopicExpression(new LiteralExpression(words));
        return mh;
    }

    @Bean
    KafkaTemplate<String,String> kafkaTemplate(ProducerFactory<String,String> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
    @Bean
    ProducerFactory<String,String> producerFactory(){
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBootstrapServer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(props);
    }
}
