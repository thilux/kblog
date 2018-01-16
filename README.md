# kBlog

This is a very simple project using kotlin's next hot thing, framework KTor.

The project provides a set of APIs for a blog where one can create posts and comments for the posts.

I intend to enhance this project as I learn more about Kotlin and KTor. For this initial version, please check the stack information below:

The solution is heavily based on the following sample projects found on GitHub or blogs online:

- [Simon Wirtz Ktor application REST application sample](https://github.com/s1monw1/ktor_application)


## Solution Stack

- Using Netty as the server engine
- All services are JSON based REST services
- H2 in memory database using [Squash](https://github.com/orangy/squash) as the persistence layer
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

## Branches

The repository is divided in the following branches, each being incremental to the previous in terms of features:

- **part1:** Implementation with no database, storing data in runtime memory (available)
- **part2:** Implementation of database handling for real persistence on database (planned)
- **part3:** Implementation of JWT security (planned)

