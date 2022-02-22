FROM adoptopenjdk/openjdk12
EXPOSE 8080
ADD build/libs/movie_database-* app.jar
ENTRYPOINT ["java","-jar","/app.jar"]