FROM openjdk:16
COPY ./build/artifacts/dashboard_jar/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar","dashboard.main.jar"]