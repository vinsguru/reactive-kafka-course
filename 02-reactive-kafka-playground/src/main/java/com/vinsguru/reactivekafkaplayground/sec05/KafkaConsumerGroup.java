package com.vinsguru.reactivekafkaplayground.sec05;

/*
    Ensure that topic has multiple partitions
 */
public class KafkaConsumerGroup {

    private static class Consumer1{
        public static void main(String[] args) {
            KafkaConsumer.start("1");
        }
    }

    private static class Consumer2{
        public static void main(String[] args) {
            KafkaConsumer.start("2");
        }
    }

    private static class Consumer3{
        public static void main(String[] args) {
            KafkaConsumer.start("3");
        }
    }

}
