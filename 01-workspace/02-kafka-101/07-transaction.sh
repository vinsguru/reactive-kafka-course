


kafka-topics.sh --bootstrap-server localhost:9092 --topic transfer-requests --create

kafka-topics.sh --bootstrap-server localhost:9092 --topic transaction-events --create

kafka-console-producer.sh \
    --bootstrap-server localhost:9092 \
    --topic transfer-requests \
    --property key.separator=: \
    --property parse.key=true

kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic transaction-events \
    --property print.key=true \
    --isolation-level=read_committed \
    --from-beginning   

kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic transaction-events \
    --property print.key=true \
    --from-beginning       