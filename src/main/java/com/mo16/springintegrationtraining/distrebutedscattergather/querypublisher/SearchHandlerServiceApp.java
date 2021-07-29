package com.mo16.springintegrationtraining.distrebutedscattergather.querypublisher;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

// publishes the data to kafka Topic named queries
@SpringBootApplication
@EnableIntegration
public class SearchHandlerServiceApp {

    @Value("${app.integration.topic.queries}")
    private String queries;

    @Value("${app.integration.broker.host}")
    private String kafkaBootstrapServer;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("server.port" ,"8080");
        props.setProperty("app.integration.topic.queries" ,"queries");
        props.setProperty("app.integration.broker.host" ,"mo:9092");
        new SpringApplicationBuilder(SearchHandlerServiceApp.class).properties(props).run(args);
    }

    @Bean
    MessageChannel queriesSink() {
        return new DirectChannel();
    } //outbound channel

    @Bean
    CommandLineRunner runner(QueryPublisherGateway publisher) {
        return args -> Stream.of("how data is\\stored in cassandra", "microservices design patterns"
                , "how to do Transactional messaging", "I am hungry").forEach(publisher::publish);
    }

    @Bean
    @ServiceActivator(inputChannel = "queriesSink")
    MessageHandler queriesKafkaPublisher(KafkaTemplate<String,String> template){
        var mh = new KafkaProducerMessageHandler<>(template);
        mh.setTopicExpression(new LiteralExpression(queries));
        return mh;
    }

    @Bean
    KafkaTemplate<String,String> kafkaTemplate(ProducerFactory<String,String> factory){
        return new KafkaTemplate<>(factory);
    }

    @Bean
    ProducerFactory<String,String> producerFactory(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBootstrapServer);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(configs);
    }


}
