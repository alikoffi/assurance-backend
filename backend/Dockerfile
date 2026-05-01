FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV JAVA_OPTS=""

COPY --from=build /workspace/build/libs/assurance-backend.jar /app/assurance-backend.jar

EXPOSE 8075

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/assurance-backend.jar"]
