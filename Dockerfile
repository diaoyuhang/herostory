FROM openjdk:20-ea-17-jdk
MAINTAINER diaoyuhang
WORKDIR /usr/local/herostory
ADD ./target/herostory-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","herostory-1.0-SNAPSHOT.jar"]