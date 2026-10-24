package com.example.bookcatalog;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
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

        // page layout: centered content with a maximum width
        setMaxWidth("1100px");
        getStyle().set("margin", "0 auto").set("padding", "24px");

        // header banner
        Icon bookIcon = VaadinIcon.BOOK.create();
        bookIcon.setSize("36px");
        bookIcon.setColor("white");
        H2 title = new H2("Book Catalog");
        title.getStyle().set("color", "white").set("margin", "0");
        Span subtitle = new Span("Manage your book collection: add, view, edit and delete books.");
        subtitle.getStyle().set("color", "rgba(255, 255, 255, 0.85)");
        VerticalLayout titles = new VerticalLayout(title, subtitle);
        titles.setPadding(false);
        titles.setSpacing(false);
        HorizontalLayout header = new HorizontalLayout(bookIcon, titles);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.getStyle()
            .set("background", "linear-gradient(135deg, #1e3a8a, #2563eb)")
            .set("border-radius", "12px")
            .set("padding", "20px 24px")
            .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.15)");

        // toolbar: search, add button and counter
        filter.setPlaceholder("Search by title or author");
        filter.setPrefixComponent(VaadinIcon.SEARCH.create());
        filter.setClearButtonVisible(true);
        filter.setWidth("320px");
        bookCount.getStyle()
            .set("background", "#e0e7ff")
            .set("color", "#1e3a8a")
            .set("padding", "4px 12px")
            .set("border-radius", "999px")
            .set("font-weight", "600")
            .set("font-size", "14px");
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, bookCount);
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        // table
        grid.setHeight("340px");
        grid.setColumns("id", "title", "author", "genre", "publicationYear");
        grid.getColumnByKey("id").setWidth("60px").setFlexGrow(0);
        grid.getColumnByKey("publicationYear").setHeader("Year").setWidth("100px").setFlexGrow(0);
        grid.getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 8px rgba(0, 0, 0, 0.08)");

        // footer
        Div footer = new Div(new Span("First Java CRUD project - Fatih Alkan (79731)"));
        footer.setWidthFull();
        footer.getStyle()
            .set("text-align", "center")
            .set("color", "#6b7280")
            .set("font-size", "13px")
            .set("margin-top", "16px");

        add(header, actions, grid, editor, footer);

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