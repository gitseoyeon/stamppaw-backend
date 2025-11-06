FROM eclipse-temurin:17-jdk

# jar를 둘 디렉터리 생성
WORKDIR /app

# build/libs 아래 있는 jar 복사
COPY ./build/libs/*.jar /app/stamppaw_backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/stamppaw_backend.jar"]