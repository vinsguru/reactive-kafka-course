# to consume messages
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello-world

# to consume from beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello-world --from-beginning