FROM java:8-jre
COPY ./smile-office.jar /smile-office.jar
RUN mkdir /log
ENTRYPOINT ["nohup", "java", "-jar", "/smile-office.jar", ">", "/log/smile-office.log", "2>&1", "&"]