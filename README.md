# WAES: Scalable Web Assignment
This project is an assignment provided by WAES in their candidates selection process. Description of the problem can be founded in the sources files

### Technologies
- Spring-boot
- Java 8
- Log4j
- Maven
- Lombok Plugin
- JUnit

### How to start
Once the application is fetched from git it can be built with maven
```
mvn clean install
```
This will fetch dependencies and run all tests

To run the app execute using maven:
```
mvn spring-boot:run
```

Or you can start the server running 
```
java -jar target/assignment-scalable-web-1.0.0.jar
```

The application will start a server locally and will be listening to the port 8080. As this is a Spring Boot application, you can change 
default properties adding a new argument in the command line. eg for listening on a different port
```
java -jar target/assignment-scalable-web-1.0.0.jar --server.port=<Port#>
```

### How to use it

Available Endpoints

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| PUT | /v1/diff/{id}/left | persists left value | 
| PUT | /v1/diff/{id}/right | persists right value | 
| GET | /v1/diff/{id}/ | return diff looking for id |

Those endpoint could be consumed using dedicated software (eg. Postman) or just in the command line using cURL.

1. Save or update left value:
```
curl -X PUT \
  http://localhost:8080/v1/diff/1/left \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"content":"AQ=="}'
```
2. Save or update right value:
```
curl -X PUT \
  http://localhost:8080/v1/diff/1/right \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"content":"BQ=="}'
```
3. Get diff for ID = 1
```
curl -X GET \
  http://localhost:8080/v1/diff/1 \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
```
And the response will be
```
    "id": "1",
    "left": "AQ==",
    "right": "BQ==",
    "diff": "Left and Right have the same length, but 1 byte(s) are different"
}
```
The content that we are sending to the API must be encoded in base64, otherwise the API will return an error with the message: Request body is invalid

Another error that you can get it's if you try to get some diff and some part of it is missing. In this case, the message will be: The left and right parts are missing for computing diff {1}

With the server running you can access to the H2 database [here](http://localhost:8080/h2/login). It could be usefull to see what is happening after perform some inserts/updates using endpoints.
Credentials are
- username: sa
- password: <empty>


### Future possible improvements
- Create API specification using Swagger or similar
- Test JacksonBase64 encoding/decoding
