FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/luminusdiff.jar /luminusdiff/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/luminusdiff/app.jar"]
