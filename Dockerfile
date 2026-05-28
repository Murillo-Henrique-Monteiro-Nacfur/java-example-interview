FROM gradle:8.5-jdk17-jammy AS build

WORKDIR /workspace/app

COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dlog4j2.formatMsgNoLookups=true", "-XX:MinRAMPercentage=60", "-XX:MaxRAMPercentage=90", "-server", "-XX:+OptimizeStringConcat", "-XX:+UseStringDeduplication", "-jar", "app.jar"]