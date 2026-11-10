
# Stage 1: Build the application using Java 25 and Maven
FROM eclipse-temurin:25-jdk-noble AS build

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests


# Stage 2: Run the WAR using Tomcat
FROM tomcat:10.1.61-jre25-temurin

ENV PORT=10000

# Configure Tomcat to listen on Render's port
RUN sed -i 's/port="8080"/port="10000"/' \
    /usr/local/tomcat/conf/server.xml \
    && rm -rf /usr/local/tomcat/webapps/*

# Deploy FashionStore as the root application
COPY --from=build \
    /app/target/FashionStore-0.0.1-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

EXPOSE 10000

CMD ["catalina.sh", "run"]
