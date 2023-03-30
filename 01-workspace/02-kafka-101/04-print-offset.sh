
# to print offset, time etc
kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic hello-world \
    --property print.offset=true \
    --property print.timestamp=true