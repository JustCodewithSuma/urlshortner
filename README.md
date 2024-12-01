URI Shortener REST Service

This is an API using REST Interface to generate a shortened link for any given URL.The backend should assign each submitted URL a unique, as short as possible, alphanumeric ID.

The REST client defines custom IDs.If the ID is already taken, an appropriate REST-compliant error message will be returned. 

Url shortener is a service that converts long URLs into short aliases to save space when sharing URLs in messages, twitter, presentations, etc. When a user opens a short URL, it will be automatically redirected to the original (long) URL.

The service provides the followinf REST Services: 

  POST  : Assigns each submitted URL a unique,as short as possible alphanumeric ID.It also have the ability to assign Customs ID´s,if the IDs are already taken then suitable error message would be returned.
  
  GET   : Redirects to the original Long URL 
  
  DELETE: Delete ShortURL based on the ID´s.

How to use: 

git clone https://github.com/JustCodewithSuma/urlshortner.git 

- Make sure you have access to local or H2 DB.
- Open project in your favorite editor and change application.yaml file to point to your H2 database
- Build Spring project
- Open localhost:8080/swagger-ui.html to see endpoints.


