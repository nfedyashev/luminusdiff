FROM openjdk:8-alpine

COPY target/uberjar/luminusdiff.jar /luminusdiff/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/luminusdiff/app.jar"]
