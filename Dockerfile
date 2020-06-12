FROM clojure:openjdk-11-lein

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps
COPY . /usr/src/app
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

ENV TOKYO_PORT=80
EXPOSE ${TOKYO_PORT}

CMD ["java", "-jar", "app-standalone.jar"]