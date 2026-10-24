package com.example.bookcatalog;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

/**
 * Main page of the application: a searchable list of books and an editor form.
 */
@Route
@PageTitle("Book Catalog")
public class MainView extends VerticalLayout {

	private final BookRepository repo;

	private final BookEditor editor;

	final Grid<Book> grid;

	final TextField filter;

	final Span bookCount;

	private final Button addNewBtn;

	public MainView(BookRepository repo, BookEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Book.class);
		this.filter = new TextField();
		this.bookCount = new Span();
		this.addNewBtn = new Button("Add book", VaadinIcon.PLUS.create());

		// build layout
		H2 header = new H2("Book Catalog");
		Span subtitle = new Span("Manage your book collection: add, view, edit and delete books.");

		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, bookCount);
		actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
		add(header, subtitle, actions, grid, editor);

		grid.setHeight("320px");
		grid.setColumns("id", "title", "author", "genre", "publicationYear");
		grid.getColumnByKey("id").setWidth("60px").setFlexGrow(0);
		grid.getColumnByKey("publicationYear").setHeader("Year").setWidth("100px").setFlexGrow(0);

		filter.setPlaceholder("Search by title or author");
		filter.setPrefixComponent(VaadinIcon.SEARCH.create());
		filter.setClearButtonVisible(true);

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listBooks(e.getValue()));

		// Connect selected Book to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> editor.editBook(e.getValue()));

		// Create and edit a new Book when the button is clicked
		addNewBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			editor.editBook(new Book("", "", null, null));
		});

		// Listen to changes made by the editor and refresh data from the database
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listBooks(filter.getValue());
		});

		// Initialize listing
		listBooks(null);
	}

	void listBooks(String filterText) {
		List<Book> books = StringUtils.hasText(filterText) ? repo.search(filterText) : repo.findAll();
		grid.setItems(books);
		bookCount.setText("Books shown: " + books.size());
	}

}
