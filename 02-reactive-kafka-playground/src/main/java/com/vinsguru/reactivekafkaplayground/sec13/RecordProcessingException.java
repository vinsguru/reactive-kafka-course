package com.vinsguru.reactivekafkaplayground.sec13;

import reactor.kafka.receiver.ReceiverRecord;

public class RecordProcessingException extends RuntimeException{

    private final ReceiverRecord<?, ?> record;

    public RecordProcessingException(ReceiverRecord<?, ?> record, Throwable e) {
        super(e);
        this.record = record;
    }

    @SuppressWarnings("unchecked")
    public <K, V> ReceiverRecord<K, V> getRecord() {
        return (ReceiverRecord<K, V>) record;
    }

}
