package com.example.bookcatalog;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class BookCatalogApplicationTests {

	@Autowired
	private BookRepository repository;

	@Test
	public void shouldLoadSampleBooksWhenTheApplicationIsStarted() {
		then(this.repository.count()).isEqualTo(7);
	}

	@Test
	public void shouldFindBooksByPartOfAuthorName() {
		then(this.repository.search("wicz")).hasSize(2);
	}

	@Test
	public void shouldFindBooksByPartOfTitle() {
		then(this.repository.search("solaris")).hasSize(1);
	}
}
