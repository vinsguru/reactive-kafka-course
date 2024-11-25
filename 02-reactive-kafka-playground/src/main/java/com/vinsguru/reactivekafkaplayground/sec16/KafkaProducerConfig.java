package com.vinsguru.reactivekafkaplayground.sec16;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public SenderOptions<String, OrderEvent> senderOptions(KafkaProperties kafkaProperties, SslBundles sslBundles){
        return SenderOptions.<String, OrderEvent>create(kafkaProperties.buildProducerProperties(sslBundles));
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, OrderEvent> producerTemplate(SenderOptions<String, OrderEvent> options){
        return new ReactiveKafkaProducerTemplate<>(options);
    }

}
