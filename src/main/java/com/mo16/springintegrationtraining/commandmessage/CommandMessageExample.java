package com.mo16.springintegrationtraining.commandmessage;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
@EnableIntegration
public class CommandMessageExample {
    public static void main(String[] args) {
        SpringApplication.run(CommandMessageExample.class, args);
    }

    @Bean
    public MessageChannel taskCommandsChannel(){
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "taskCommandsChannel")
    interface TaskCommandsGateway{ @Gateway void executeTask (Message<String> command); }

    @Bean
    CommandLineRunner runner(TaskCommandsGateway commandsGateway){
        return args -> commandsGateway.executeTask(MessageBuilder.withPayload("start database migration").build());
    }

    @MessageEndpoint
    static class TaskCommandsListener {
        @ServiceActivator(inputChannel = "taskCommandsChannel")
        void cpuUsage (Message<String> command){
            System.out.println("executing -> " + command.getPayload());
        }
    }


}
