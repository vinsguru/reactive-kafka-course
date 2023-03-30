package com.vinsguru.analyticsservice.service;

import com.vinsguru.analyticsservice.entity.ProductViewCount;
import com.vinsguru.analyticsservice.event.ProductViewEvent;
import com.vinsguru.analyticsservice.repository.ProductViewRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductViewEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductViewEventConsumer.class);
    private final ReactiveKafkaConsumerTemplate<String, ProductViewEvent> template;
    private final ProductViewRepository repository;

    @PostConstruct
    public void subscribe(){
        this.template
                .receive()
                .bufferTimeout(1000, Duration.ofSeconds(1))
                .flatMap(this::process)
                .subscribe();
    }

   private Mono<Void> process(List<ReceiverRecord<String, ProductViewEvent>> events){
        var eventsMap = events.stream()
                .map(r -> r.value().getProductId())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return this.repository.findAllById(eventsMap.keySet()) // what if there are no records
                .collectMap(ProductViewCount::getId)
                .defaultIfEmpty(Collections.emptyMap())
                .map(dbMap -> eventsMap.keySet().stream().map(productId -> updateViewCount(dbMap, eventsMap, productId)).collect(Collectors.toList()))
                .flatMapMany(this.repository::saveAll)
                .doOnComplete(() -> events.get(events.size() - 1).receiverOffset().acknowledge())
                .doOnError(ex -> log.error(ex.getMessage()))
                .then();
   }

   private ProductViewCount updateViewCount(Map<Integer, ProductViewCount> dbMap, Map<Integer, Long> eventMap, int productId){
        var pvc = dbMap.getOrDefault(productId, new ProductViewCount(productId, 0L, true));
        pvc.setCount(pvc.getCount() + eventMap.get(productId));
        return pvc;
   }

}
