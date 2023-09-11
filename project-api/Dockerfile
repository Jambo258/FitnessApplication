# Use an official Maven image as the base image
FROM maven:3.8.5-openjdk-17-slim

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the project's pom.xml and src files to the container
COPY pom.xml .
COPY src/ src/

# Build the Maven project
RUN mvn clean package -DskipTests -q

# Specify the command to run when the container starts
CMD ["mvn", "spring-boot:run"]