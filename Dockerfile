FROM gradle:7.2.0-jdk11-hotspot@sha256:2cb13534d5547aa22874204eea20f3b6be0636c1602fd145ebb03a0f346aaf2f AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN gradle clean build -x test -x detekt

FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine@sha256:dff626e70ae1c4bdde817f160e9256f9d2ccab1fc27f458e872c7e63881c0a01
RUN apk add dumb-init
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /project/build/libs/*-all.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
CMD "dumb-init" "java" "-jar" "java-application.jar"
