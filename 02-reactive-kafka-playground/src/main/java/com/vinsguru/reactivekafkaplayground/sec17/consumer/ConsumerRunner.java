package com.vinsguru.reactivekafkaplayground.sec17.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsumerRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsumerRunner.class);

    @Autowired
    private ReactiveKafkaConsumerTemplate<String, DummyOrder> template;

    @Override
    public void run(String... args) throws Exception {
        this.template.receive()
             //        .doOnNext(r -> r.headers().forEach(h -> log.info("header key: {}, value: {}", h.key(), new String(h.value()))))
                .doOnNext(r -> log.info("key: {}, value: {}", r.key(), r.value()))
                .subscribe();
    }
}
