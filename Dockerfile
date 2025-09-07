FROM openjdk:22-jdk
COPY target/billingsoftware-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
