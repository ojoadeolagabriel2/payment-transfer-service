#!/bin/sh

set -e

# test and package service
mvn clean package spring-boot:repackage

# run jar
java -jar target/payment-transfer-service.jar