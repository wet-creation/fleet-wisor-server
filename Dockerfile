# Stage 1: Build Application
FROM gradle:8.7-jdk21 AS build

WORKDIR /home/gradle/app

# Копируем gradle конфиги и кешируем зависимости
COPY build.gradle.kts settings.gradle.kts gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
RUN gradle dependencies --no-daemon --build-cache

# Копируем весь проект и билдим обычный jar
COPY . /home/gradle/app
RUN gradle clean build --no-daemon --build-cache -i --stacktrace

# Stage 2: Create minimal runtime image
FROM amazoncorretto:22

WORKDIR /app

COPY --from=build /home/gradle/app/build/libs/*.jar /app/ktor-server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ktor-server.jar"]
