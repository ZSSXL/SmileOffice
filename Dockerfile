FROM java:8-jre
COPY ./smile-service.jar /smile-office.jar
RUN mkdir /log
ENTRYPOINT ["nohup", "java", "-jar", "/smile-office.jar", "--spring.profiles.active=prod", ">", "/log/smile-office.log", "2>&1", "&"]