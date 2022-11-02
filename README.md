# recipe-service

Recipe Service is built with Spring boot, which is a Java-based framework used to create stand-alone, production-grade Spring-based Applications.

## Prerequisites
Running, debugging and editing the source code requires the following tools to be installed on your machine

- [Java 8](https://www.oracle.com/nl/java/technologies/javase/javase8-archive-downloads.html) with unlimited JCE capabilities.
- [Docker](https://www.docker.com/) to pull the PostgreSQL Docker image.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (as with the JDK, other IDEs probably work fine too, it was just tested on IntelliJ).
- [Maven 3.x.x](https://maven.apache.org/download.cgi) or higher.
- A tool like [Postman](https://www.getpostman.com/) to easily create, edit and send custom REST calls over HTTP.

## Running the App in Local
1. **Postgres with Docker and Docker compose:**
      Running Postgres with Docker and docker-compose makes it very easy to run and maintain especially in a development environment.
    - Use the docker-compose.yml file which is present in the root folder of the project.
    - Make sure Docker is available and running.
    - Execute from Terminal:
     ```
      docker-compose -f docker-compose.yml up
    ```
    you should see a message **database system is ready to accept connections**

2. **Build the application**
    - Go to the root folder of the project.
    - Execute from Terminal:
    ```
      mvn clean install
    ```
3. **Run the Spring boot App**
     - Execute from Terminal:
    ```
      mvn spring-boot:run 
      ```
    - The default port for recipe service is 9090.**Tomcat started on port(s): 9090 (http) with context path ''**
## REST API Documentation
Recipe Service uses Swagger 2, using the Springfox implementation of the Swagger 2 specification.
Swagger UI is a built-in solution that makes user interaction with the Swagger-generated API documentation much easier.
 - Access the swagger documentation from the below url.
    ```
      http://localhost:9090/swagger-ui.html
  
      ```
 - **Postman collection** is availabe in the root folder of the project. see the file **Recipe.postman_collection.json**.
## recipe-service TechStack:
1.**Postgres with Docker image**:
PostgreSQL stands as a open-source database management system that is highly regarded for its reliability, scalability, stability, and security.
Recipe Service has established a **Many to Many relationship** between **Recipe** and **Ingredient** tables.

2.The **JPA Metamodel** provides a type-safe way to reference entity attributes.
JPA static metamodel classes while writing criteria queries in Hibernate.see **RecipeRepositoryImpl** for the usage.

3.**MapStruct** :boilerplate code has been removed by using MapStruct while converting POJOs to other POJOs.

4.**Lombok**
