Fitness Application project done with Angular, Spring Boot and PostgreSQL (which was supposed to be first a tictactoe application thats why frontend folder is named weird) where user needs to create an account and add healthData and TrainingData. After giving healthdata and trainingdata it is shown on the chart where user can follow progression. Project contains CRUD (Create, Read, Update, Delete) operations.

Instructions

Start database in docker container by running the following command in the root directory of the project

````
docker compose up
````

Start Frontend with following commands
````
cd tictactoe
npm install
npm run start
````

Start Backend with following commands (also make sure to have connections to database in application.properties file)
````
spring.datasource.url=jdbc:postgresql://localhost:5432/users
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
````

````
cd project-api
mvn clean install
mvn spring-boot:run -P dev
````

You can also run the entire application in docker by running following command in the project root
````
docker-compose -f docker-compose.full.yml up
````

# Project Screenshots

<div align="center">
    <p>User registration </p>
    <img src="/screenshots/register_user.png" width="600px"</img>
    <p>User Profilepage</p>
    <img src="/screenshots/user_profilepage.png" width="600px"</img>
    <p>User HealthData</p>
    <img src="/screenshots/user_healthdata.png" width="600px"</img>
    <p>User HealthData Calculations</p>
    <img src="/screenshots/user_healthdata_with_information.png" width="600px"</img>
    <p>TrainingData</p>
    <img src="/screenshots/user_dailytraining.png" width="600px"</img>
    <p>User Progress Chart</p>
    <img src="/screenshots/user_progresschart.png" width="600px"</img>

</div>
