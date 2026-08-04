# ============================
# 1) Build da aplicação (Maven)
# ============================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# ============================
# 2) Runtime com Open Liberty
# ============================
FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi-minimal

COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --chown=1001:0 --from=build /app/target/*.war /config/apps/

RUN configure.sh

EXPOSE 9080
CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"]
