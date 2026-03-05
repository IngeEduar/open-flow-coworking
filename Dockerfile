FROM chainguard/maven:openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache tini

WORKDIR /app

COPY --from=build /app/target/open-flow-coworking-0.0.1-SNAPSHOT.jar open-flow-coworking-0.0.1-SNAPSHOT.jar

LABEL org.opencontainers.image.title="Openflow-Coworking" \
      org.opencontainers.image.description="Aplicación backend para la gestión de ingresos y salidas de coworkings" \
      org.opencontainers.image.version="0.0.1" \
      org.opencontainers.image.authors="Eduar Avendaño <ingeeduar20@gmail.com>" \
      org.opencontainers.image.vendor="Eduar Avendaño" \
      org.opencontainers.image.licenses="Proprietary" \
      org.opencontainers.image.source="https://github.com/IngeEduar/open-flow-coworking" \
      org.opencontainers.image.revision="${COMMIT}" \
      org.opencontainers.image.base.name="openjdk:21-jdk-slim"

EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["/sbin/tini", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar open-flow-coworking-0.0.1-SNAPSHOT.jar"]