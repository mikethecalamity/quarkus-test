FROM eclipse-temurin:17-jdk

ARG SERVICE=service1

COPY apps/subprojects/${SERVICE}/config/ /config
COPY apps/subprojects/${SERVICE}/build/quarkus-app/app/ /opt/deployment/app/
COPY apps/subprojects/${SERVICE}/build/quarkus-app/lib/ /opt/deployment/lib/
COPY apps/subprojects/${SERVICE}/build/quarkus-app/quarkus/ /opt/deployment/quarkus/
COPY apps/subprojects/${SERVICE}/build/quarkus-app/*.jar /opt/deployment/

EXPOSE 8080 8787

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

CMD java $JAVA_OPTS -jar /opt/deployment/quarkus-run.jar
