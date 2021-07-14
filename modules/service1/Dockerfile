ARG SERVICE=service1
ARG BUILD_IMAGE=adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine
ARG DEPLOY_IMAGE=adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

FROM ${BUILD_IMAGE} AS build

ARG SERVICE

WORKDIR /opt/work
COPY ./gradlew ./gradlew.bat ./
COPY ./gradle ./gradle/
WORKDIR /opt/work/java
ENV GRADLE_USER_HOME /.gradle
RUN ./gradlew -v

COPY . /opt/work
RUN /opt/work/gradlew --profile --no-daemon ${SERVICE}:quarkusBuild

FROM ${DEPLOY_IMAGE} AS deploy

ARG SERVICE

ENV SERVICE=${SERVICE}
ENV WORK_DIR=/opt/deployment
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

COPY --from=build /opt/work/modules/${SERVICE}/config /config
COPY --from=build /opt/work/modules/${SERVICE}/.build/quarkus-app/app/ /opt/deployment/app/
COPY --from=build /opt/work/modules/${SERVICE}/.build/quarkus-app/lib/ /opt/deployment/lib/
COPY --from=build /opt/work/modules/${SERVICE}/.build/quarkus-app/quarkus/ /opt/deployment/quarkus/
COPY --from=build /opt/work/modules/${SERVICE}/.build/quarkus-app/*.jar /opt/deployment/

EXPOSE 8080 8787

CMD java $JAVA_OPTS -jar /opt/deployment/quarkus-run.jar
