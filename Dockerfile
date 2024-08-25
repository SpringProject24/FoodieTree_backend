FROM amazoncorretto:11
RUN mkdir -p /cert
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]