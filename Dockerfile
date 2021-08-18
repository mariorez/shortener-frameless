FROM gradle:jdk11
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN gradle clean build -x test -x detekt
CMD "gradle" "run"
