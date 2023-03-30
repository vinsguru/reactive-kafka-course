package com.vinsguru.reactivekafkaplayground.sec12;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/*
    error handling demo: processing pipeline
 */
public class KafkaConsumerV2 {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerV2.class);

    public static void main(String[] args) {

        var consumerConfig = Map.<String, Object>of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, "demo-group",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "1"
        );

        var options = ReceiverOptions.create(consumerConfig)
                .subscription(List.of("order-events"));

        KafkaReceiver.create(options)
                .receive()
                .log()
                .concatMap(KafkaConsumerV2::process)
                .subscribe();

    }

    private static Mono<Void> process(ReceiverRecord<Object, Object> receiverRecord){
        return Mono.just(receiverRecord)
                .doOnNext(r -> {
                    var index = ThreadLocalRandom.current().nextInt(1, 10);
                    log.info("key: {}, index: {}, value: {}", r.key(), index, r.value().toString().toCharArray()[index]);
                })
               .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)).onRetryExhaustedThrow((spec, signal) -> signal.failure()))
               .doOnError(ex -> log.error(ex.getMessage()))
               .doFinally(s -> receiverRecord.receiverOffset().acknowledge())
               .onErrorComplete()
               .then();
    }



}
