# Stage 1: Cache Gradle dependencies
FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
# Только эти файлы необходимы для загрузки зависимостей
RUN gradle clean build --no-daemon --build-cache -i --stacktrace || true

# Stage 2: Build Application
FROM gradle:latest AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
WORKDIR /home/gradle/app
# Здесь мы копируем только те файлы, которые действительно нужны для билда
COPY . /home/gradle/app
RUN gradle buildFatJar --no-daemon --build-cache -i --stacktrace || true

# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/app/build/libs/*.jar /app/ktor-docker.jar
ENTRYPOINT ["java", "-jar", "/app/ktor-docker.jar"]

