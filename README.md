# transfer service

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

:star: send a star my way

A lightweight transfer & account management system

* [x] manages account ledger balances
* [x] manages account transaction history 
* [x] utilizes transaction history to determine live balance 
* [x] on app start a test account gets inserted into the database with details:
  * account number: `1000000011`
  * account type: `current`
  * realtime balance: 1,000,000
* [x] restricted account types: `current` & `savings`

# Table of content

- [Dependencies](#installation-dependencies)
    - [Parameters to consider](#parameters-to-consider)
- [How to run code (from jar)](#how-to-run-from-jar)
- [How to run code (as docker container)](#how-to-run-code-from-docker-container)
- [Invalid API request & error handling](#handling-errors)
- [How to get API metrics](#how-to-view-service-metrics)

# installation dependencies

Service requires that apache maven 3+ be installed.

1. install `apache maven` from here unto your unix machine: https://www.baeldung.com/install-maven-on-windows-linux-mac#installing-maven-on-mac-os-x
2. install `java 11` on your unix machine as seen here https://java.tutorials24x7.com/blog/how-to-install-java-11-on-mac
3. docker is only `required if you follow` the "How to run code (as docker container)" approach below

###### confirm maven is installed once step 1 above is complete:

```bash
mvn -v
```


### Parameters to consider

The following application configurations `see application.yml` need not be changed to start the service.

```yaml
server:
  port: 40000
```

### How to run from jar

Running this service is as easy as 1.. 2.. 3..

**1**. using your command line window, navigate to the 'root' folder of this project and execute the following command:

```bash
chmod +x ./run_locally.sh && ./run_locally.sh
```
> note: make sure no other process is on port 40000 with this: `kill -9 $(lsof -t -i:40000)`

**2**. confirm application is running

```bash
curl http://localhost:40000/health
```

should return a 200 OK message with response body as below:

```json
{
  "status": "UP",
  ...
}
```

**3**. curl the api endpoint to fetch your account balance as seen below:

```bash
curl --location --request GET 'http://localhost:40000/account/1000000011/type/current'
```

###### returns sample json and 200 OK response as seen below:

```bash
{
    "account_number": "1000000011",
    "account_type": "current",
    "balance": 999500.0,
    "ledger_balance": 999500.0
}
```

###### where account cannot be retrieved:

```bash
{
    "code": "03",
    "message": "Account not found, please check senders account number or account type"
}
```

**4**. curl the api to send a transfer request against an existing account:

```bash
curl --location --request POST 'http://localhost:40000/transfer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "request_reference_id": "139f09ae-31f3-4cbb-9116-96882b700daf",
    "sender_email": "a@y.com",
    "request_date" : "2020-03-09 17:55:00",
    "source_account" : {
        "account_number": "1000000011",
        "account_type": "current"
    },
    "target_account" : {
        "account_number": "1000000021",
        "account_type": "savings"
    },
    "request_amount": "100.0"
}'
```

###### returns sample json and 200 OK response as seen below:

```bash
{
    "statusCode": "00",
    "message": "Success"
}
```

###### where source account cannot be retrieved:

```bash
{
    "code": "03",
    "message": "Account not found, please check senders account number or account type"
}
```

### How to run code from docker container

Running this service from docker is as easy as 1.. 2.. 3..

**1**. using your command line window, navigate to the 'root' folder of this project and execute the following command:

```bash
chmod +x ./deployment/run_locally_via_docker.sh && ./deployment/run_locally_via_docker.sh
```
> note: make sure no other process is on port 40001 with this: `kill -9 $(lsof -t -i:40001)`
> image will have the name `bank.saudi.fransi/payment-transfer-service` and container name `payment-transfer-service`

**2**. confirm application is running:

```bash
curl http://localhost:40001/health
```

should return a 200 OK with response as seen below:

```json
{
  "status": "UP"
}
```

**3**. curl the api endpoint on port 40001 to transfer from account 1000000011:

```bash
curl --location --request POST 'http://localhost:40001/transfer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "request_reference_id": "139f09ae-31f3-4cbb-9116-96882b700daf",
    "sender_email": "a@y.com",
    "request_date" : "2020-03-09 17:55:00",
    "source_account" : {
        "account_number": "1000000011",
        "account_type": "current"
    },
    "target_account" : {
        "account_number": "1000000021",
        "account_type": "savings"
    },
    "request_amount": "100.0"
}'
```

> returns sample json and HTTP response code 200

```bash
{
    "statusCode": "00",
    "message": "Success"
}
```

**4**. curl the api endpoint to fetch your account balance so:

```bash
curl --location --request GET 'http://localhost:40001/account/1000000011/type/current'
```

###### returns sample json and 200 OK response as seen below:

```bash
{
    "account_number": "1000000011",
    "account_type": "current",
    "balance": 999500.0,
    "ledger_balance": 999500.0
}
```

### Handling errors

Where an invalid payload is posted to /instructions api, HTTP 400 (bad request) is returned to calling client.

```bash
{
    "code": "02",
    "message": "invalid request"
}
```

### How to view service metrics

This application leverages prometheus to expose API metrics (jvm, memory, http...), curl as shown below to view.

```bash
curl --location --request GET 'http://localhost:{PORT}/prometheus'
```

### How to view swagger api

curl as seen below

```bash
curl --location --request GET 'http://localhost:{PORT}/v2/api-docs'
```