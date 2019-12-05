*Fibonacci Server* [![Build Status](https://travis-ci.org/Xicks/Fibonacci-Server.svg?branch=master)](https://travis-ci.org/Xicks/Fibonacci-Server)
--
Web server that calculates fibonacci values and store the results on PostgresSQL and Redis.

## Prerequisites

In order to successfully run this project as well as develop in it you need to have the following installed:

* JDK 11+: [Linux](https://tecadmin.net/install-openjdk-java-ubuntu/)
* Kotlin 1.3.50: [Install](https://medium.com/@sushanthande1/install-kotlin-on-linux-ubuntu-1a3f97dffa40)
* Gradle 5: [Install](https://gradle.org/install/)
* Docker: [Linux](https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/#install-using-the-repository)
* Docker Compose: [Install](https://docs.docker.com/compose/install/)

## Architecture
Clean Architecture: [Reference](https://www.freecodecamp.org/news/a-quick-introduction-to-clean-architecture-990c014448d2/)

![](resources/clean-arch.png)
## Technologies
- Gradle
- Kotlin
- Ktor
- Redis
- PostgresSQL