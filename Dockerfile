FROM maven:3.9.6-eclipse-temurin-21-alpine AS  build
COPY ./ /src
RUN mvn -f /src/pom.xml clean package

FROM eclipse-temurin:21_35-jre-alpine
COPY --from=build /src/target/*.jar /thunder.jar
EXPOSE 1883
ENTRYPOINT ["java", "-cp", "/thunder.jar", "org.fungover.Main"]
