FROM openjdk:17-jdk-slim AS build-stage
WORKDIR /app
COPY . ./
RUN ./gradlew quarkusBuild --no-daemon

FROM openjdk:17-slim
RUN apt-get update && apt-get install -y openssl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build-stage /app/build/quarkus-app /app/
RUN openssl genrsa -out /app/priv.pem 8192 \
    && openssl rsa -in /app/priv.pem -pubout -out /app/pub.pem
ENV SMALLRYE_JWT_SIGN_KEY_LOCATION=/app/priv.pem
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]

