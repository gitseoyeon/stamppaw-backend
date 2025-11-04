# jdk17 Image Start
FROM openjdk:17

# jar 파일 복제
COPY ./build/libs/stamppaw_backend-0.0.1-SNAPSHOT.jar stamppaw_backend.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "stamppaw_backend.jar"]