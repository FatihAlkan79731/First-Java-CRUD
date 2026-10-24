package com.example.bookcatalog;

import java.util.List;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Form used to create, update and delete a single {@link Book}.
 */
@SpringComponent
@UIScope
public class BookEditor extends VerticalLayout implements KeyNotifier {

    static final List<String> GENRES = List.of("Novel", "Poetry", "Science Fiction",
            "Fantasy", "Horror", "Drama", "History", "Biography", "Other");

    private final BookRepository repository;

    /** The book currently being edited. */
    private Book book;

    /* Fields bound to the properties of the Book entity */
    TextField title = new TextField("Title");
    TextField author = new TextField("Author");
    ComboBox<String> genre = new ComboBox<>("Genre");
    IntegerField publicationYear = new IntegerField("Publication year");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Book> binder = new Binder<>(Book.class);
    private ChangeHandler changeHandler;

    public BookEditor(BookRepository repository) {
        this.repository = repository;

        genre.setItems(GENRES);
        publicationYear.setMin(0);
        publicationYear.setMax(2100);

        H3 formTitle = new H3("Book details");
        formTitle.getStyle().set("margin", "0").set("color", "#1e3a8a");
        FormLayout fields = new FormLayout(title, author, genre, publicationYear);
        add(formTitle, fields, actions);

        // card style for the form
        getStyle()
            .set("background", "#f8fafc")
            .set("border", "1px solid #e2e8f0")
            .set("border-radius", "12px")
            .set("padding", "20px");

        // Title and author are required, the other fields are bound by naming convention
        binder.forField(title).asRequired("Title is required").bind(Book::getTitle, Book::setTitle);
        binder.forField(author).asRequired("Author is required").bind(Book::getAuthor, Book::setAuthor);
        binder.bindInstanceFields(this);

        setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editBook(null));
        setVisible(false);
    }

    void delete() {
        repository.delete(book);
        changeHandler.onChange();
    }

    void save() {
        if (!binder.validate().isOk()) {
            return;
        }
        repository.save(book);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editBook(Book b) {
        if (b == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = b.getId() != null;
        if (persisted) {
            // Load a fresh copy of the entity from the database
            book = repository.findById(b.getId()).get();
        }
        else {
            book = b;
        }
        // Only already saved books can be deleted
        delete.setVisible(persisted);

        binder.setBean(book);

        setVisible(true);
        title.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

}