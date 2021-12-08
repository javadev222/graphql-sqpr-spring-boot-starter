FROM maven:3.5-jdk-11 as BUILD
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests=true

FROM openjdk:11-jdk
ENV JAVA_OPTS=""
COPY --from=BUILD /usr/src/app/target /app
WORKDIR /app
EXPOSE 8030
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/test-1.0-SNAPSHOT.jar" ]
