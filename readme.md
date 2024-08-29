# Auth and Profile project

## Description
This project is a simple project to demonstrate the use of Django Rest Framework. It has two main functionalities:
- Auth: It allows users to register and login.
- Profile: It allows users to update their profile.

## Running the application
TO run the application we need to first set up all the dependencies.

```docker compose up``` This sets up the kafka service.

```docker run --name mongo-docker -p 27017:27017 --network=springlearning  mongo:latest``` This sets up the mongodb service.

```docker run -p 8082:8082 --network=springlearning --link mongo-docker:mongo auth-app``` This sets up the auth service.

```docker run -p 8081:8081 --network=springlearning --link mongo-docker:mongo --link kafka-server-docker:consumer --link kafka-server-docker:producer profile-app``` This sets up the profile service.