package com.vinsguru.reactivekafkaplayground.sec16;

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
    public ReceiverOptions<String, DummyOrder> receiverOptions(KafkaProperties kafkaProperties, SslBundles sslBundles){
        return ReceiverOptions.<String, DummyOrder>create(kafkaProperties.buildConsumerProperties(sslBundles))
                .consumerProperty(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, "false")
                .consumerProperty(JsonDeserializer.USE_TYPE_INFO_HEADERS, false)
                .consumerProperty(JsonDeserializer.VALUE_DEFAULT_TYPE, DummyOrder.class)
                .subscription(List.of("order-events"));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, DummyOrder> consumerTemplate(ReceiverOptions<String, DummyOrder> options){
        return new ReactiveKafkaConsumerTemplate<>(options);
    }

}
