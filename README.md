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

### How to use

- register a new user on initial project setup (there is no test user)
- play around with the features 

## Codebase TLDR

Feel free to ignore the infrastructure setup and focus on the application features. 

Commits for that lense begin at
```03a8f7af0f7e82a149af9bb12b93fc894e0779ad``` 
and end at
```ac711b8f2ca39cbdb4be6c1550959e0cff1586e6```

Of course feel free to check out the rest if interested.

I completely butchered the 4-hour time suggestion with this.
Since this is no real world product and I was given much freedom, I thought why not try to make it fancy.

Unfortunately, I therefore didn't manage to deliver feature completeness. \
Also, there are still careless errors in design and codebase because I rushed.

##  Known issues:


- forgot to add sold assets back to available assets in the frontend (they'll be back after reload)
- should have added more tests
- UI looks akward on first buy with empty portfolio
- portfolio looks akward in general
- portfolio isn't where it should be
- if you leave the stream open for long the graphs get really dense due to no limitation like e.g. show only latest x prices
- I hardly know anything about trading so please excuse potential weirdness of wordings or logic in general (maybe should have done research, didn't because of time limit)
- I would have discussed the changes of App design from the specification with the customer