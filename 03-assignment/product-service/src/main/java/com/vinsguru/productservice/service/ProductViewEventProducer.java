package com.vinsguru.productservice.service;

import com.vinsguru.productservice.event.ProductViewEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderRecord;

@AllArgsConstructor
public class ProductViewEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductViewEventProducer.class);

    private final ReactiveKafkaProducerTemplate<String, ProductViewEvent> template;
    private final Sinks.Many<ProductViewEvent> sink;
    private final Flux<ProductViewEvent> flux;
    private final String topic;

    public void subscribe(){
        var srFlux = this.flux
                .map(e -> new ProducerRecord<>(topic, e.getProductId().toString(), e))
                .map(pr -> SenderRecord.create(pr, pr.key()));
        this.template.send(srFlux)
                .doOnNext(r -> log.info("emitted event: {}", r.correlationMetadata()))
                .subscribe();
    }

    public void emitEvent(ProductViewEvent event){
        this.sink.tryEmitNext(event);
    }

}
