package com.mo16.springintegrationtraining.documentmessage;

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
public class DocumentMessageExample {
    public static void main(String[] args) {
        SpringApplication.run(DocumentMessageExample.class, args);
    }

    @Bean
    public MessageChannel cpuUsageChannel(){
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "cpuUsageChannel")
    interface CpuMonitorGateway{ @Gateway void send (String cpuUsage); }

    @Bean
    CommandLineRunner runner(CpuMonitorGateway monitorGateway){
        return args -> monitorGateway.send("cpu usage");
    }

    @MessageEndpoint
    static class CpuMonitorListener {
        @ServiceActivator(inputChannel = "cpuUsageChannel")
        void cpuUsage (Message<String> cpuUsage){
            System.out.println(cpuUsage.getPayload());
        }
    }


}
