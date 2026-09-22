package com.scholarsphere.content.controller;

import com.scholarsphere.content.entity.Author;
import com.scholarsphere.content.repository.AuthorRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Create author - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Valid @RequestBody Author author) {

        Author savedAuthor = authorRepository.save(author);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAuthor);
    }

    // Get all authors - USER and ADMIN
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {

        return ResponseEntity.ok(
                authorRepository.findAll()
        );
    }

    // Get author by ID - USER and ADMIN
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{authorId}")
    public ResponseEntity<Author> getAuthorById(
            @PathVariable(name = "authorId") Long authorId) {

        return ResponseEntity.ok(
                authorRepository.findById(authorId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Author not found with id: " + authorId
                                )
                        )
        );
    }
}