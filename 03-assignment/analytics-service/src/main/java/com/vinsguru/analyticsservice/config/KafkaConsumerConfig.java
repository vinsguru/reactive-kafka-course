package com.vinsguru.analyticsservice.config;

import com.vinsguru.analyticsservice.event.ProductViewEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ReceiverOptions<String, ProductViewEvent> receiverOptions(KafkaProperties properties, SslBundles sslBundles){
        return ReceiverOptions.<String, ProductViewEvent>create(properties.buildConsumerProperties(sslBundles))
                              .consumerProperty(JsonDeserializer.VALUE_DEFAULT_TYPE, ProductViewEvent.class)
                              .consumerProperty(JsonDeserializer.USE_TYPE_INFO_HEADERS, false)
                              .subscription(List.of("product-view-events"));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, ProductViewEvent> kafkaConsumerTemplate(ReceiverOptions<String, ProductViewEvent> receiverOptions){
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }

}
