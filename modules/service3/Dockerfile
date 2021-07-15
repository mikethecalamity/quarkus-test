FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

ARG SERVICE=service3

ENV WORK_DIR=/opt/deployment
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

COPY modules/${SERVICE}/config/ /config
COPY modules/${SERVICE}/build/quarkus-app/app/ /opt/deployment/app/
COPY modules/${SERVICE}/build/quarkus-app/lib/ /opt/deployment/lib/
COPY modules/${SERVICE}/build/quarkus-app/quarkus/ /opt/deployment/quarkus/
COPY modules/${SERVICE}/build/quarkus-app/*.jar /opt/deployment/

EXPOSE 8080 8787

CMD java $JAVA_OPTS -jar /opt/deployment/quarkus-run.jar
