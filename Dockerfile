#################################
# Build stage
#################################

FROM eclipse-temurin:21.0.6_7-jdk AS builder

WORKDIR /app


COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw


COPY pom.xml .


RUN ./mvnw dependency:go-offline


COPY src src


RUN ./mvnw clean package -DskipTests



#################################
# Runtime stage
#################################

FROM eclipse-temurin:21.0.6_7-jre


WORKDIR /app


COPY --from=builder /app/target/*.jar app.jar



RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m --uid 10001 spring \
    && mkdir -p /app/uploads \
    && chown -R spring:spring /app

USER spring


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app/app.jar"]
