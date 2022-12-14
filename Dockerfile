FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/clojure-bank-0.0.1-SNAPSHOT-standalone.jar /clojure-bank/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/clojure-bank/app.jar"]
