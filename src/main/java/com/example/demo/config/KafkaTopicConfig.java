package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userEventsTopic() {
        Map<String, String> configs = new HashMap<>();
        configs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
        configs.put(TopicConfig.RETENTION_MS_CONFIG, "604800000"); // 7 days
        configs.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1");
        
        return new NewTopic("user-events", 3, (short) 1)
                .configs(configs);
    }

    @Bean
    public NewTopic documentEventsTopic() {
        Map<String, String> configs = new HashMap<>();
        configs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
        configs.put(TopicConfig.RETENTION_MS_CONFIG, "604800000"); // 7 days
        configs.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1");
        
        return new NewTopic("document-events", 3, (short) 1)
                .configs(configs);
    }

    @Bean
    public NewTopic userEventsProcessedTopic() {
        return new NewTopic("user-events-processed", 3, (short) 1);
    }

    @Bean
    public NewTopic documentEventsProcessedTopic() {
        return new NewTopic("document-events-processed", 3, (short) 1);
    }
}
