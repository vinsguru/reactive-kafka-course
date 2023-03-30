kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --topic order-events --create

kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --command-config consumer.properties \
    --topic order-events --create