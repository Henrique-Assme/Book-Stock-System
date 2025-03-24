package com.library.book_stock.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.book_stock.dto.BookResponse;
import com.library.book_stock.model.Book;
import com.library.book_stock.repository.BookRepository;

@RestController
@RequestMapping("/api/book")
public class BookController {

    public final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            BookResponse response = new BookResponse("Livro encotrado!", book);
            return ResponseEntity.status(200).body(response);
        } else {
            BookResponse response = new BookResponse("Livro não encotrado.", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());

        if (existingBook.isPresent()) {
            BookResponse response = new BookResponse("Livro já cadastrado! Atualize a quantidade.", null);
            return ResponseEntity.status(409).body(response);
        } else {
            book.setStockQuantity(1);
            Book newBook = bookRepository.save(book);
            BookResponse response = new BookResponse("Livro cadastrado com sucesso!", newBook);
            return ResponseEntity.status(201).body(response);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<BookResponse> incrementExistingBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setStockQuantity(bookToUpdate.getStockQuantity() + 1);
            Book updatedBook = bookRepository.save(bookToUpdate);
            BookResponse response = new BookResponse("Quantidade atualizada!", updatedBook);
            return ResponseEntity.status(200).body(response);
        } else {
            BookResponse response = new BookResponse("Livro não encontrado", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookResponse> decrementExistingBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            if (bookToUpdate.getStockQuantity() > 0) {
                bookToUpdate.setStockQuantity(bookToUpdate.getStockQuantity() - 1);
                Book updatedBook = bookRepository.save(bookToUpdate);
                BookResponse response = new BookResponse("Quantidade atualizada!", updatedBook);
                return ResponseEntity.status(200).body(response);
            }

            bookRepository.deleteById(id);
            BookResponse response = new BookResponse("Livro removido!", null);
            return ResponseEntity.status(200).body(response);
        } else {
            BookResponse response = new BookResponse("Livro não encontrado", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> editBookProperty(@PathVariable Long id, @RequestBody Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setAuthor(updatedBook.getAuthor());
            book.setTitle(updatedBook.getTitle());
            book.setGenre(updatedBook.getGenre());
            book.setDescription(updatedBook.getDescription());
            bookRepository.save(book);
            BookResponse response = new BookResponse("Livro atualizado!", book);
            return ResponseEntity.status(200).body(response);
        } else {
            BookResponse response = new BookResponse("Livro não encontrado", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
