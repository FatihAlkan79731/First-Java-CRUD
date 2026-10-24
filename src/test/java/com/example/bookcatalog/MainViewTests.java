package com.example.bookcatalog;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = MainViewTests.Config.class)
public class MainViewTests {

	@Autowired BookRepository repository;

	BookEditor editor;

	MainView mainView;

	@BeforeEach
	public void setup() {
		this.editor = new BookEditor(this.repository);
		this.mainView = new MainView(this.repository, editor);
	}

	@Test
	public void shouldInitializeTheGridWithBookRepositoryData() {
		int bookCount = (int) this.repository.count();

		then(mainView.grid.getColumns()).hasSize(5);
		then(getBooksInGrid()).hasSize(bookCount);
		then(mainView.bookCount.getText()).isEqualTo("Books shown: " + bookCount);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Book> getBooksInGrid() {
		ListDataProvider<Book> ldp = (ListDataProvider) mainView.grid.getDataProvider();
		return new ArrayList<>(ldp.getItems());
	}

	@Test
	public void shouldFillOutTheGridWithNewData() {
		int initialBookCount = (int) this.repository.count();

		editor.editBook(new Book("", "", null, null));
		editor.title.setValue("The Little Prince");
		editor.author.setValue("Antoine de Saint-Exupery");
		editor.publicationYear.setValue(1943);

		this.editor.save();

		then(getBooksInGrid()).hasSize(initialBookCount + 1);
		then(getBooksInGrid().get(getBooksInGrid().size() - 1))
			.extracting("title", "author", "publicationYear")
			.containsExactly("The Little Prince", "Antoine de Saint-Exupery", 1943);
	}

	@Test
	public void shouldFilterTheGridByAuthor() {
		this.repository.save(new Book("Dune", "Frank Herbert", "Science Fiction", 1965));

		mainView.listBooks("Herbert");

		then(getBooksInGrid()).hasSize(1);
		then(getBooksInGrid().get(0))
			.extracting("title", "author")
			.containsExactly("Dune", "Frank Herbert");
	}

	@Test
	public void shouldInitializeWithInvisibleEditor() {
		then(this.editor.isVisible()).isFalse();
	}

	@Test
	public void shouldMakeEditorVisible() {
		Book first = getBooksInGrid().get(0);
		this.mainView.grid.select(first);

		then(this.editor.isVisible()).isTrue();
	}

	@Configuration
	@EnableAutoConfiguration(exclude = com.vaadin.flow.spring.SpringBootAutoConfiguration.class)
	static class Config {

		@Autowired
		BookRepository repository;

		@PostConstruct
		public void initializeData() {
			this.repository.save(new Book("Pan Tadeusz", "Adam Mickiewicz", "Poetry", 1834));
			this.repository.save(new Book("Quo Vadis", "Henryk Sienkiewicz", "Novel", 1896));
			this.repository.save(new Book("Solaris", "Stanislaw Lem", "Science Fiction", 1961));
		}
	}
}
