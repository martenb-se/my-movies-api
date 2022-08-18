FROM openjdk:17-oracle

# Create App Directory in Container
RUN mkdir -p /opt/my-movies-api/.mvn
RUN mkdir -p /opt/my-movies-api/src/main/java
RUN mkdir -p /opt/my-movies-api/src/main/resources
RUN mkdir -p /opt/my-movies-api/src/test

# Copy Back-End Source Files
COPY .mvn /opt/my-movies-api/.mvn
COPY src/main/java /opt/my-movies-api/src/main/java
COPY src/main/resources /opt/my-movies-api/src/main/resources
COPY src/test /opt/my-movies-api/src/test
COPY .env mvnw my-movies-api.iml pom.xml /opt/my-movies-api/

# Set environment during build process when unit tests will run
ENV API_PORT=8080
ENV SPRING_APPLICATION_NAME=build-my-movies-api
ENV LOGGING_LEVEL=OFF

# Install & Build Back-End
WORKDIR /opt/my-movies-api
RUN ./mvnw package

# Copy Build to Root
RUN cp ./target/*.jar /app.jar

# Cleanup
RUN rm -rf /app

# Run
ENTRYPOINT ["java", "-jar", "/app.jar"]