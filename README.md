# svg-backend
SVQ Spring Boot Kotlin Backend

## Run locally

Prerequisites: Create a Spotify app on https://developer.spotify.com/dashboard/applications

Use its clientId and clientSecret in the application `spring.security.oauth2.client.registration.spotify.client-id` and 
`spring.security.oauth2.client.registration.spotify.client-secret` properties, respectively. 
(Note that you don't actually need to provide them in the application.yml, you could also override them using command line arguments)

After that, the app can by navigating to the top-level folder `svq-backend` and executing the following command:

```sh
./gradlew bootRun
```

The app should now be running on http://localhost:8080. Feel free to test it by navigating to http://localhost:8080/api/v1/ping or 
authenticate at http://localhost:8080/api/v1/user/me