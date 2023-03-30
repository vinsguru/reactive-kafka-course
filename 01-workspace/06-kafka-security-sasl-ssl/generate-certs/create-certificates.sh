#!/bin/bash

# CA create private key and root CA certificate
openssl genrsa -out root.key
openssl req -new -x509 -key root.key -out root.crt  -subj "/CN=localhost" -nodes

# keystore
keytool -keystore kafka.keystore.jks -storepass changeit -alias localhost -validity 3650 -genkey -keyalg RSA -dname "CN=localhost"

# create CSR (certificate signing request)
keytool -keystore kafka.keystore.jks -storepass changeit -alias localhost -certreq -file kafka-signing-request.crt

# CA signs the cerificate
openssl x509 -req -CA root.crt -CAkey root.key -in kafka-signing-request.crt -out kafka-signed.crt -days 3650 -CAcreateserial

# We can import root CA cert & our signed certificate
# This should be private and owned by the server
keytool -keystore kafka.keystore.jks -storepass changeit -alias CARoot -import -file root.crt -noprompt 
keytool -keystore kafka.keystore.jks -storepass changeit -alias localhost -import -file kafka-signed.crt -noprompt 

# This is for clients
keytool -keystore kafka.truststore.jks -storepass changeit -noprompt -alias CARoot -import -file root.crt

# move all these files to certs directory
mkdir -p ../certs
mv *.crt ../certs
mv *.jks ../certs
mv *.key ../certs