#################################
# Build stage
#################################

FROM eclipse-temurin:21-jdk AS builder

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

FROM eclipse-temurin:21-jre


WORKDIR /app


COPY --from=builder /app/target/*.jar app.jar



RUN useradd -m spring


USER spring


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app/app.jar"]