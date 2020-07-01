### HOW TO RUN APPLICATION LOCALLY

Step one start Postgres via Docker run:  sudo docker run -d -p 5432:5432 --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword postgres 

Step two start application via mvn:  mvn spring-boot:run

Step three go to url http://localhost:8080/weather?city=amsterdam 

Change query parameter city, to see results for other cities.

### Running unit tests
mvn clean verify

### Running unit tests with integration-tests
mvn clean verify -Pintegration-test



Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution** 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
