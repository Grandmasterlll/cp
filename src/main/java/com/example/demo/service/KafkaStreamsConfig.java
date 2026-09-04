package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;
import java.util.Properties;

@Configuration
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public KafkaStreams userEventStreamProcessor(StreamsBuilder streamsBuilder) {
        StreamsBuilder builder = streamsBuilder;

        // Читаем события пользователей
        KStream<String, String> userEvents = builder.stream(
                "user-events",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        // Фильтруем только CREATED события
        KStream<String, String> createdUsers = userEvents.filter((key, value) -> 
                value != null && value.contains("CREATED")
        );

        // Группируем и считаем количество событий
        KTable<String, Long> userEventCounts = createdUsers
                .groupBy((key, value) -> value)
                .count(Materialized.as("user-event-counts"));

        // Подписываемся на изменения
        userEventCounts.toStream().foreach((key, count) -> 
                log.info("User event count for '{}': {}", key, count)
        );

        // Создаем windowed aggregation для подсчета событий за интервал
        KTable<Windowed<String>, Long> windowedCounts = userEvents
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
                .count(Materialized.as("windowed-counts"));

        windowedCounts.toStream().foreach((windowedKey, count) -> 
                log.info("Windowed count for '{}': {}", windowedKey.key(), count)
        );

        Properties props = new Properties();
        props.put("spring.kafka.bootstrap-servers", bootstrapServers);
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("processing.guarantee", "exactly_once_v2");

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }

    @Bean
    public KafkaStreams documentEventStreamProcessor(StreamsBuilder streamsBuilder) {
        StreamsBuilder builder = streamsBuilder;

        // Читаем события документов
        KStream<String, String> documentEvents = builder.stream(
                "document-events",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        // Фильтруем события
        KStream<String, String> processedDocuments = documentEvents.filter((key, value) -> 
                value != null && !value.isEmpty()
        );

        // Подсчет событий по авторам
        KTable<String, Long> authorCounts = processedDocuments
                .mapValues(value -> {
                    if (value != null && value.contains("authorId")) {
                        int start = value.indexOf("authorId") + 9;
                        int end = value.indexOf("\"", start);
                        return end > start ? value.substring(start, end) : "unknown";
                    }
                    return "unknown";
                })
                .groupBy((key, authorId) -> authorId)
                .count(Materialized.as("author-event-counts"));

        authorCounts.toStream().foreach((authorId, count) -> 
                log.info("Document events for author '{}': {}", authorId, count)
        );

        Properties props = new Properties();
        props.put("spring.kafka.bootstrap-servers", bootstrapServers);
        props.put("schema.registry.url", "http://localhost:8081");

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }
}
