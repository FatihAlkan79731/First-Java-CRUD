package com.example.bookcatalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Book Catalog - First Java CRUD application.
 *
 * @author Fatih Alkan (79731)
 */
@SpringBootApplication
public class BookCatalogApplication {

	private static final Logger log = LoggerFactory.getLogger(BookCatalogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookCatalogApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(BookRepository repository) {
		return (args) -> {
			// save some sample books
			repository.save(new Book("Pan Tadeusz", "Adam Mickiewicz", "Poetry", 1834));
			repository.save(new Book("Quo Vadis", "Henryk Sienkiewicz", "Novel", 1896));
			repository.save(new Book("Solaris", "Stanislaw Lem", "Science Fiction", 1961));
			repository.save(new Book("Pride and Prejudice", "Jane Austen", "Novel", 1813));
			repository.save(new Book("Frankenstein", "Mary Shelley", "Horror", 1818));
			repository.save(new Book("The Time Machine", "H. G. Wells", "Science Fiction", 1895));
			repository.save(new Book("Crime and Punishment", "Fyodor Dostoevsky", "Novel", 1866));

			// fetch all books
			log.info("Books found with findAll():");
			log.info("---------------------------");
			for (Book book : repository.findAll()) {
				log.info(book.toString());
			}
			log.info("");

			// fetch books by author
			log.info("Books found with search('wicz'):");
			log.info("--------------------------------");
			for (Book book : repository.search("wicz")) {
				log.info(book.toString());
			}
			log.info("");
			log.info("Book Catalog is ready - open http://localhost:8080 in your browser");
		};
	}

}
