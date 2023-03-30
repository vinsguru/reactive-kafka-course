package com.vinsguru.reactivekafkaplayground;

import com.vinsguru.reactivekafkaplayground.sec17.consumer.DummyOrder;
import com.vinsguru.reactivekafkaplayground.sec17.producer.OrderEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(OutputCaptureExtension.class)
@TestPropertySource(properties = "app=consumer")
public class OrderEventConsumerTest extends AbstractIT {

    @Test
    public void consumerTest(CapturedOutput output){

        KafkaSender<String, OrderEvent> sender = createSender();
        var uuid = UUID.randomUUID();
        var orderEvent = new OrderEvent(uuid, 1, LocalDateTime.now());
        var dummyOrder = new DummyOrder(uuid.toString(), "1");
        var sr = toSenderRecord("order-events", "1", orderEvent);

        var mono = sender.send(Mono.just(sr))
                .then(Mono.delay(Duration.ofMillis(500)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        Assertions.assertTrue(output.getOut().contains(dummyOrder.toString()));

    }


}
