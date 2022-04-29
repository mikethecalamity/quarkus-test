FROM eclipse-temurin:11-jdk

ARG SERVICE=service1

COPY modules/${SERVICE}/config/ /config
COPY modules/${SERVICE}/build/quarkus-app/app/ /opt/deployment/app/
COPY modules/${SERVICE}/build/quarkus-app/lib/ /opt/deployment/lib/
COPY modules/${SERVICE}/build/quarkus-app/quarkus/ /opt/deployment/quarkus/
COPY modules/${SERVICE}/build/quarkus-app/*.jar /opt/deployment/

EXPOSE 8080 8787

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

CMD java $JAVA_OPTS -jar /opt/deployment/quarkus-run.jar
