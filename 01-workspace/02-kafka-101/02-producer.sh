# to produce messages
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic hello-world

# linger.ms 
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic hello-world --timeout 100