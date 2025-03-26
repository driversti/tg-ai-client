FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY libs ./libs

# Install the Alpine-specific tdlib.jar into the local Maven repository
RUN mvn install:install-file \
    -Dfile=./libs/tdlib.jar \
    -DgroupId=org.drinkless \
    -DartifactId=tdlib \
    -Dversion=207f3be \
    -Dpackaging=jar \
    -DgeneratePom=true

RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
LABEL authors="driversti"
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/libs ./libs
EXPOSE 8080
# Set LD_LIBRARY_PATH if the jar contains native libraries
ENV LD_LIBRARY_PATH=/app/libs
ENTRYPOINT ["java","-jar","app.jar"]
