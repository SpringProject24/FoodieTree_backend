FROM amazoncorretto:11
RUN mkdir -p /cert
WORKDIR /app
COPY . /app
RUN ./gradlew clean build -x test
RUN cp build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]