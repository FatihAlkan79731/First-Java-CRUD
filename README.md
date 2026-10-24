# First-Java-CRUD – Book Catalog

**Author:** Fatih Alkan (Student ID: 79731)
**GitHub:** [FatihAlkan79731](https://github.com/FatihAlkan79731)

A simple CRUD (Create, Read, Update, Delete) web application written in Java.
It manages a small catalog of books. The project is based on the Spring guide
[Creating CRUD UI with Vaadin](https://spring.io/guides/gs/crud-with-vaadin/).

## Features

- **Create** – add a new book with the *Add book* button
- **Read** – list all books in a table and search them by title or author
- **Update** – click a book in the table, change its data and press *Save*
- **Delete** – click a book in the table and press *Delete*
- Validation: title and author are required
- Sample books are loaded automatically at startup

## Technologies

- Java 21
- Spring Boot 4 (Spring Data JPA)
- Vaadin 25 (web user interface written in Java)
- H2 in-memory database
- Maven (Maven Wrapper included)

## Project structure

```
src/main/java/com/example/bookcatalog/
├── BookCatalogApplication.java   # entry point, loads sample data
├── Book.java                     # JPA entity (id, title, author, genre, publicationYear)
├── BookRepository.java           # Spring Data repository with a search query
├── MainView.java                 # main page: search field, table, "Add book" button
└── BookEditor.java               # form for creating, editing and deleting a book
src/main/resources/application.properties
src/test/java/...                 # unit and integration tests
doc/                              # screenshots documenting the work
```

## How to run locally

Requirements: **JDK 21** (or newer). Maven and Node.js do not have to be installed –
the Maven Wrapper downloads Maven, and Vaadin downloads Node.js automatically if needed.

Windows:

```
mvnw.cmd spring-boot:run
```

macOS / Linux:

```
./mvnw spring-boot:run
```

The first start can take a few minutes because dependencies are downloaded.
When the console shows `Started BookCatalogApplication`, open
<http://localhost:8080> in the browser.

To run the tests:

```
./mvnw test
```

## Documentation

Screenshots are stored in the [`doc`](doc) directory:

- the application running locally,
- example CRUD operations,
- the startup logs in the console,
- the GitHub commit history with commits from me and my partner.

## Contributors

- Fatih Alkan (79731) - author of the project
- Helin Gulec (79879) - reviewed the project and improved the documentation

- 
