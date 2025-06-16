FROM amazoncorretto:17
# Define an argument for the path to the JAR file created by Maven.
# This makes the Dockerfile more flexible.
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the build context (where CodeBuild runs) into the container.
# It is renamed to "application.jar" for a consistent name.
COPY ${JAR_FILE} application.jar

# Inform Docker that the container listens on port 8080 at runtime.
# This is the default port for Spring Boot web applications.
EXPOSE 8080

# The command to run when the container starts.
# This executes the Spring Boot application.
ENTRYPOINT ["java", "-jar", "application.jar"]