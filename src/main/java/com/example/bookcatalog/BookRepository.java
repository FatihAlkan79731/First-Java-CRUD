package com.example.bookcatalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

	/**
	 * Finds books whose title or author contains the given text (case-insensitive).
	 */
	List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

	default List<Book> search(String text) {
		return findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(text, text);
	}
}
