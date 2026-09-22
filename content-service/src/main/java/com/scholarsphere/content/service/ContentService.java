package com.scholarsphere.content.service;

import com.scholarsphere.content.entity.Author;
import com.scholarsphere.content.entity.Category;
import com.scholarsphere.content.entity.Content;
import com.scholarsphere.content.repository.AuthorRepository;
import com.scholarsphere.content.repository.CategoryRepository;
import com.scholarsphere.content.repository.ContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public ContentService(
            ContentRepository contentRepository,
            AuthorRepository authorRepository,
            CategoryRepository categoryRepository) {

        this.contentRepository = contentRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    // Create content
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    // Get all content
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    // Get content by ID
    public Content getContentById(Long contentId) {

        return contentRepository.findById(contentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Content not found with id: " + contentId
                        )
                );
    }

    // Update content
    public Content updateContent(
            Long contentId,
            Content updatedContent) {

        Content existingContent = getContentById(contentId);

        existingContent.setTitle(updatedContent.getTitle());
        existingContent.setDescription(updatedContent.getDescription());
        existingContent.setContentType(updatedContent.getContentType());
        existingContent.setLanguage(updatedContent.getLanguage());
        existingContent.setPublicationDate(updatedContent.getPublicationDate());
        existingContent.setStatus(updatedContent.getStatus());

        return contentRepository.save(existingContent);
    }

    // Delete content
    public void deleteContent(Long contentId) {

        Content existingContent = getContentById(contentId);

        contentRepository.delete(existingContent);
    }

    // Search content
    public List<Content> searchContent(String keyword) {

        return contentRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword,
                        keyword
                );
    }

    // Assign authors and categories to content
    @Transactional
    public Content updateContentMetadata(
            Long contentId,
            List<Long> authorIds,
            List<Long> categoryIds) {

        Content content = getContentById(contentId);

        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            throw new RuntimeException(
                    "One or more author IDs do not exist"
            );
        }

        List<Category> categories =
                categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new RuntimeException(
                    "One or more category IDs do not exist"
            );
        }

        content.setAuthors(authors);
        content.setCategories(categories);

        return contentRepository.save(content);
    }
}