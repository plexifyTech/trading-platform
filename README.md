# Online Trading platform mockup

This is a test project emulating an online trading platform where users can buy, sell and monitor assets.

## Getting started

To deploy the project, run

### `docker-compose -up`

The deployment (especially of keycloak) might take a while. Please check that all containers are up and running before accessing the web page.

The gradle build on docker might be flaky sometimes (false alerts that it could not find spring dependencies or 
false-positive test-failures). Please try it again a few times.

The frontend can be reached at
http://127.0.0.1:3000


