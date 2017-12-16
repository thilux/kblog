# kBlog

This is a very simple project using kotlin's next hot thing, framework KTor.

The project provides a set of APIs for a blog where one can create posts and comments for the posts.

I intend to enhance this project as I learn more about Kotlin and KTor. For this initial version, please check the stack information below:

The solution is heavily based on the following sample projects found on GitHub or blogs online:

- [Simon Wirtz Ktor application REST application sample](https://github.com/s1monw1/ktor_application)


## Solution Stack

- Using Netty as the server engine
- All services are JSON based REST services
- In memory database with time to live constrained by application run time. No journals or logs are generated.
- SLF4J is used for logging.
- No security implemented so far.

## Building & Running

This project uses gradle for dependency management and build. To build the project, run the following command:

```
gradle build
```

For running the server the project, first, you need to create environment variable PORT with the number of the port in which you wish to kblog to listen to.

```$> export PORT=8080```

Then:

```
gradle run
```

