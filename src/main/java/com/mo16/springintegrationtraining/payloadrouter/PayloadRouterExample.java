package com.mo16.springintegrationtraining.payloadrouter;

import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

@SpringBootApplication
@EnableIntegration
public class PayloadRouterExample {
    public static void main(String[] args) {
        SpringApplication.run(PayloadRouterExample.class, args);
    }

    @Bean
    CommandLineRunner runner(OperationsPublisherGateway publisher) {
        return args -> {
            User user = new User("mosup");
            publisher.send(MessageBuilder.withPayload(new InsertOperation<>(user)).build());
            publisher.send(MessageBuilder
                    .withPayload(new UpdateOperation<>(user.id, new User(user.id, "mosup emad"))).build());
        };
    }

    @Bean public MessageChannel insertOperationsChannel() { return new DirectChannel(); }

    @Bean public MessageChannel updateOperationsChannel() { return new DirectChannel(); }

    @Bean public MessageChannel OperationsRouterChannel() { return new DirectChannel(); }

    @MessagingGateway(defaultRequestChannel = "OperationsRouterChannel")
    interface OperationsPublisherGateway {
        @Gateway void send(Message operation);
    }

    @Bean
    @ServiceActivator(inputChannel = "OperationsRouterChannel")
    public PayloadTypeRouter route() {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(InsertOperation.class.getName(), "insertOperationsChannel");
        router.setChannelMapping(UpdateOperation.class.getName(), "updateOperationsChannel");
        return router;
    }

    @MessageEndpoint
    static class InsertOperationsHandler {
        @ServiceActivator(inputChannel = "insertOperationsChannel")
        public void handleInsertOperations(Message<InsertOperation> insertOperation) {
            System.out.println("inserting -> " + insertOperation.getPayload().entity);
        }
    }

    @MessageEndpoint
    static class UpdateOperationsHandler {
        @ServiceActivator(inputChannel = "updateOperationsChannel")
        public void handleInsertOperations(Message<UpdateOperation> updateOperation) {
            System.out.println("updating -> " + updateOperation.getPayload().entity);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private String id;
        private String name;

        public User(String name) {
            this.name = name;
            this.id = UUID.randomUUID().toString();
        }

    }

    @Getter
    @RequiredArgsConstructor
    static class InsertOperation<T> {
        private final Operation operationType = Operation.Insert;
        private final T entity;
    }

    @Getter
    @RequiredArgsConstructor
    static class UpdateOperation<T> {
        private final Operation operationType = Operation.Update;
        private final String Id;
        private final T entity;
    }

    enum Operation {
        Insert,
        Update
    }

}
