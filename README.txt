Project name: WeatherApp

Purpose of project: Learn how to connect with external API.

Description: Project allows user to get and post current weather based on provided cityName.
App based on cityName allows users to get geolocation or current weather from external API.
Geolocation contains information like as city, country, latitude, longitude.
Current weather contains information like as current, max and min temperature, wind speed, pressure, humidity, clouds, date time, latitude, longitude.
App based on geolocationDto or weatherDto allows users to post/save geolocation or weather in database.
During trying to save new geolocation or weather in db app is checking that specified city in request exist based on method get geolocation.
If city name is not exist app will return error. Otherwise, app will save new data in db.
To get and post weather or geolocation I used JSON in learning purpose, but it also can be done using by dto classes with appropriate structure.

Used:
- Java version 19,
- Spring Boot 3,
- Liquibase,
- Mapstruck,
- MySQL DB,
- Lombok,
- JSON,
- Mockito 5 (JUnit Jupiter).

Test coverage is 98% (exclude entity, repository and mappers classes).