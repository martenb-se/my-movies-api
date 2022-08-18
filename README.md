# API - MyMovies Project
This is a simple project created for fun, it allows you to keep track of your favourite movies or tv-series.
It was created using [PostgresSQL](https://www.postgresql.org/), [Spring Boot](https://spring.io/projects/spring-boot) and [VueJS](https://vuejs.org/).

This is the **API** for the project. The main repository is:
[MyMovies Project](https://github.com/martenb-se/my-movies)

## Requirements
In order to simply try this project you only need the following:
### Software
* **Docker Compose**, [Installation instructions](https://docs.docker.com/compose/install/).

## Install
### Clone
Clone the repository and enter it
```shell
git clone git@github.com:martenb-se/my-movies-api.git
cd my-movies-api
```

### Preparation
In order to run or test the *MyMovies Project **back-end***, the following must be performed.

#### Environment Settings
Create a *.env* (dotenv) file in the root directory and fill out the following variables:
```dotenv
DEV_DOCKER_CONTAINER_NAME=EDIT_ME
DEV_API_PORT=EDIT_ME

API_PORT=EDIT_ME
SPRING_APPLICATION_NAME=EDIT_ME

LOGGING_LEVEL=EDIT_ME

SPRING_DATASOURCE_URL_DB=EDIT_ME
SPRING_DATASOURCE_USERNAME=EDIT_ME
SPRING_DATASOURCE_PASSWORD=EDIT_ME
```

Where
* `DEV_DOCKER_CONTAINER_NAME` is the name of the docker container that will be created.
* `DEV_API_PORT` is the port where the server will be exposed.
* `SPRING_APPLICATION_NAME` is the name of the application
* `LOGGING_LEVEL` is the current logging level (alternatives: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL` or `OFF`)
* `SPRING_DATASOURCE_URL_DB` is the database to use for the MyMovies application 

    *^ Should be the same as what was used for `DEV_APP_DB_USER` in [Database for MyMovies Project](https://github.com/martenb-se/my-movies-database)*


* `SPRING_DATASOURCE_USERNAME` is the username credential for the database

    *^ Should be the same as what was used for `DEV_APP_DB_PASS` in [Database for MyMovies Project](https://github.com/martenb-se/my-movies-database)*


* `SPRING_DATASOURCE_PASSWORD` is the password credential for the database

    *^ Should be the same as what was used for `DEV_APP_DB_NAME` in [Database for MyMovies Project](https://github.com/martenb-se/my-movies-database)*

## Start
In order to load the environment, verify build the application, and to verify that health checks are good, run the Docker Compose Script
```shell
./run.sh
```

### Sample printout from running
```text
$ ./run.sh
[+] Building 0.2s (19/19) FINISHED
 => [internal] load build definition from Dockerfile   0.0s
...
 => => naming to docker.io/springio/my-movies-api      0.0s

[+] Running 2/2
 ⠿ Network dev-my-movies-network-api      Created      0.0s
 ⠿ Container dev-my-movies-api            Started
Waiting for container health-check to finish..
...
Waiting for container health-check to finish..
Docker container dev-my-movies-api is now ready!
```

Once you see "_Docker container `DEV_DOCKER_CONTAINER_NAME` is now ready!_", the back-end is now reachable at:

http://127.0.0.1:`DEV_API_PORT`

## Stop and Uninstall
In order to stop the container and clean everything up, run:
```shell
./clean.sh
```

## Run Tests
The following tests will run inside a Docker container, therefore you will not need anything else than *Docker Compose* (as previously written). Once the tests have run, all will be cleaned up.

### Unit Tests
In order to run unit tests, run the following:
```shell
./test.unit.sh
```

#### Sample printout from running unit tests
```text
...
... (lots of text removed)
...
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.862 s - in se.martenb.mymoviesback.controller.MovieServiceControllerTest
[INFO] Running se.martenb.mymoviesback.SystemTests
[WARNING] Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 s - in se.martenb.mymoviesback.SystemTests
[INFO] Running se.martenb.mymoviesback.service.MovieServiceTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.107 s - in se.martenb.mymoviesback.service.MovieServiceTest
[INFO] Running se.martenb.mymoviesback.MovieApplicationTests
[WARNING] Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.001 s - in se.martenb.mymoviesback.MovieApplicationTests
[INFO] Running se.martenb.mymoviesback.model.MovieTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 s - in se.martenb.mymoviesback.model.MovieTest
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 43, Failures: 0, Errors: 0, Skipped: 4
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.802 s
[INFO] Finished at: 2022-08-17T13:10:52Z
[INFO] ------------------------------------------------------------------------
Untagged: my-movies-api-unittest:latest
Deleted: sha256:1e35c83969f6891f721401131bd62a4b5824b32d02f28f0758e59ed488750fcd
```

### All Tests
In order to run all tests, including system and integration tests, **make sure that the [Database for MyMovies Project](https://github.com/martenb-se/my-movies-database) is running**.
Then run the following:
```shell
./test.all.sh
```

#### Sample printout from running all tests
```text
...
... (lots of text removed)
...
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

...
... (lots of text removed)
...
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.469 s - in se.martenb.mymoviesback.dao.MovieRepositoryTest
[INFO] Running se.martenb.mymoviesback.controller.IntegrationTests
...
... (lots of text removed)
...
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.081 s - in se.martenb.mymoviesback.controller.IntegrationTests
[INFO] Running se.martenb.mymoviesback.controller.MovieServiceControllerTest

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.74 s - in se.martenb.mymoviesback.controller.MovieServiceControllerTest
[INFO] Running se.martenb.mymoviesback.SystemTests

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.523 s - in se.martenb.mymoviesback.SystemTests
[INFO] Running se.martenb.mymoviesback.service.MovieServiceTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.103 s - in se.martenb.mymoviesback.service.MovieServiceTest
[INFO] Running se.martenb.mymoviesback.MovieApplicationTests

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.566 s - in se.martenb.mymoviesback.MovieApplicationTests
[INFO] Running se.martenb.mymoviesback.model.MovieTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.051 s - in se.martenb.mymoviesback.model.MovieTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 84, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.253 s
[INFO] Finished at: 2022-08-17T13:42:10Z
[INFO] ------------------------------------------------------------------------
Untagged: my-movies-api-alltest:latest
Deleted: sha256:f92821b06bb8ff3a485fb468ef7e7e76645cad43fc9762098e3eb57778efb23d
```

## API
The application presents the following API.
### List All Movies
#### Request
`GET /api/movies`
#### Response (JSON)
```json
[
  {
    "id": 130,
    "imdbId": "tt2015381",
    "name": "Guardians of the Galaxy",
    "seen": true,
    "rating": 10
  },
  {
    "id": 131,
    "imdbId": "tt10872600",
    "name": "Spider-Man: No Way Home",
    "seen": true,
    "rating": 8
  }
]
```

### Get Specific Movie
#### Request
`GET /api/movies/tt10872600`
#### Response (JSON)
```json
{
  "id": 131,
  "imdbId": "tt10872600",
  "name": "Spider-Man: No Way Home",
  "seen": true,
  "rating": 8
}
```

### Add A Movie
#### Request
```http request
POST /api/movies
Content-Type: application/json
```
Data (JSON)
```json
{
  "imdbId": "tt1156398",
  "name": "Zombieland",
  "seen": true,
  "rating": 8
}
```
#### Response (Text)
```text
Movie is saved successfully
```

### Update A Movie
#### Request
```http request
PUT /api/movies/tt10872600
Content-Type: application/json
```
Data (JSON)
```json
{
  "imdbId": "tt10872600",
  "name": "Spider-Man: No Way Home (NEW)",
  "seen": true,
  "rating": 10
}
```
#### Response (Text)
```text
Movie was updated successfully
```

### Update A Movie's Rating
#### Request
```http request
PUT /api/movies/tt10872600/rating
Content-Type: application/json
```
##### Request Data (JSON)
```json
8
```
#### Response (Text)
```text
Movie rating was updated successfully
```

### Update A Movie's Name
#### Request
```http request
PUT /api/movies/tt10872600/name
Content-Type: application/json
```
Data (JSON)
```json
"Spider-Man: No Way Home (CHANGED)"
```
#### Response (Text)
```text
Movie name was updated successfully
```

### Update A Movie's IMDB ID
#### Request
```http request
PUT /api/movies/tt10872600/imdbid
Content-Type: application/json
```
Data (JSON)
```json
"tt123"
```
#### Response (Text)
```text
Movie IMDB id was updated successfully
```

### Update A Movie's Status As Having Been Seen
#### Request
```http request
PUT /api/movies/tt10872600/seen
Content-Type: application/json
```
Data (JSON)
```json
8
```
#### Response (Text)
```text
Movie seen status and rating updated successfully
```

### Update A Movie's Status As No Longer Having Been Seen
#### Request
`PUT /api/movies/tt10872600/seen`
#### Response (Text)
```text
Movie seen status and rating updated successfully
```

### Delete A Movie
#### Request
`DELETE /api/movies/tt10872600`
#### Response (Text)
```text
Movie is deleted successfully
```

## Alternative execution
### Requirements
In order to run or test the project **outside** Docker (for further development purposes), the following is needed.
#### Software
* **JDK 17** *(Java)*, [Google search for "how to install OpenJDK 17"](https://www.google.com/search?q=how+to+install+OpenJDK+17).

## Install
### Preparation
#### Extra Environment Settings
It is now necessary to use three more environment variables in the .env (dotenv) file.
```dotenv
API_PORT=EDIT_ME
SPRING_DATASOURCE_URL_HOST=EDIT_ME
SPRING_DATASOURCE_URL_PORT=EDIT_ME
```

Where
* `API_PORT` is the port the back-end is exposed at.
* `SPRING_DATASOURCE_URL_HOST` is the host the database is available at.
* `SPRING_DATASOURCE_URL_PORT` is the port the database is exposed at.

## Run
The following (1) verifies that it's possible to load the environment variables from the .env (dotenv) file, (2) loads the environment variables and (3) starts the application. And it will only modify the environment in a subshell (*the variables will not be set after execution*).
```shell
((source env.load.sh) && source env.load.sh && ./mvnw spring-boot:run)
```

## Test
### Unit test
```shell
((source env.load.sh) && source env.load.sh && ./mvnw test)
```
### All tests
The following will run all tests, including integration and system tests.
```shell
((source env.load.sh) && source env.load.sh && export SPRING_TESTING_PROFILES_ACTIVE=daotest,applicationtest,integrationtest,systemtest && ./mvnw test)
```

## Build JAR & Run
```shell
((source env.load.sh) && source env.load.sh && ./mvnw package && java -jar ./target/*.jar)
```
