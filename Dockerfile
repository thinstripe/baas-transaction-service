FROM openjdk:11-jre
EXPOSE 8080
COPY build/libs/transaction-service-*.jar /usr/local/bin/transaction-service.jar
RUN chmod +x /usr/local/bin/transaction-service.jar
ENTRYPOINT ["java", "-jar", "/usr/local/bin/transaction-service.jar"]
