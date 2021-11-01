FROM adoptopenjdk/openjdk13
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN mkdir -p /libs
COPY libs/* /libs/
ENTRYPOINT java -cp app.jar -Dloader.path=/libs/ org.springframework.boot.loader.PropertiesLauncher -Djava.security.egd=file:/dev/./urandom
EXPOSE 8080
