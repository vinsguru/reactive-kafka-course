# create a kafka topic called hello-world
# we assume that directory which contains 'kafka-topics.sh' is included in the PATH
kafka-topics.sh --bootstrap-server localhost:9092 --topic hello-world --create

# list all topics
kafka-topics.sh --bootstrap-server localhost:9092 --list

# describe a topic
kafka-topics.sh --bootstrap-server localhost:9092 --topic hello-world --describe

# delete a topic
kafka-topics.sh --bootstrap-server localhost:9092 --topic hello-world --delete

# topic with partitons
kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --create --partitions 2

# topic with replicaiton factor
kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --create --replication-factor 3