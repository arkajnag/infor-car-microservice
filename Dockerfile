FROM apline:latest
LABEL MAINTAINER="Arkajyoti Nag(arka.imps@gmail.com)"
EXPOSE 3002
RUN apk --no-cache update
RUN apk --no-cache maven
RUN apk --no-cache openjdk8
WORKDIR /demo-car-service
COPY /target/car-service.jar /demo-car-service/target/car-service.jar
CMD ["java","-jar","/demo-car-service/target/car-service.jar"]