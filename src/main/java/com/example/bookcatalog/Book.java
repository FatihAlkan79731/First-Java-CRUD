package com.example.bookcatalog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * A book stored in the catalog (JPA entity).
 */
@Entity
public class Book {

	@Id
	@GeneratedValue
	private Long id;

	private String title;

	private String author;

	private String genre;

	private Integer publicationYear;

	protected Book() {
	}

	public Book(String title, String author, String genre, Integer publicationYear) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publicationYear = publicationYear;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	@Override
	public String toString() {
		return String.format("Book[id=%d, title='%s', author='%s', genre='%s', year=%d]",
				id, title, author, genre, publicationYear);
	}

}
