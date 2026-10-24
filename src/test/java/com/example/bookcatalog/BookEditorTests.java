package com.example.bookcatalog;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookEditorTests {

	private static final String TITLE = "Solaris";
	private static final String AUTHOR = "Stanislaw Lem";

	@Mock BookRepository bookRepository;
	@InjectMocks BookEditor editor;
	@Mock BookEditor.ChangeHandler changeHandler;

	@BeforeEach
	public void init() {
		editor.setChangeHandler(changeHandler);
	}

	@Test
	public void shouldStoreBookInRepoWhenEditorSaveClicked() {
		this.editor.editBook(new Book("", "", null, null));

		this.editor.title.setValue(TITLE);
		this.editor.author.setValue(AUTHOR);

		this.editor.save();

		then(this.bookRepository).should().save(argThat(bookMatchesEditorFields()));
	}

	@Test
	public void shouldNotSaveBookWithoutTitle() {
		this.editor.editBook(new Book("", "", null, null));

		this.editor.author.setValue(AUTHOR);

		this.editor.save();

		then(this.bookRepository).should(org.mockito.Mockito.never()).save(any());
	}

	@Test
	public void shouldDeleteBookFromRepoWhenEditorDeleteClicked() {
		this.editor.editBook(new Book(TITLE, AUTHOR, "Science Fiction", 1961));

		editor.delete();

		then(this.bookRepository).should().delete(argThat(bookMatchesEditorFields()));
	}

	private ArgumentMatcher<Book> bookMatchesEditorFields() {
		return book -> TITLE.equals(book.getTitle()) && AUTHOR.equals(book.getAuthor());
	}

}
