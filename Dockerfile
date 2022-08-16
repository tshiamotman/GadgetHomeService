FROM openjdk:16-alpine3.13

WORKDIR /app

ADD target/GadgetHomeServer-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
EXPOSE 5432

CMD ["java", "-jar", "app.jar"]