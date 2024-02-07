FROM eclipse-temurin:17-jre

WORKDIR /app

COPY ./build/libs/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
