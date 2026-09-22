package com.scholarsphere.content.controller;

import com.scholarsphere.content.dto.ContentMetadataRequest;
import com.scholarsphere.content.entity.Content;
import com.scholarsphere.content.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    // Create new content - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Content> createContent(
            @Valid @RequestBody Content content) {

        Content savedContent = contentService.createContent(content);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedContent);
    }

    // Get all content - USER and ADMIN
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Content>> getAllContent() {

        return ResponseEntity.ok(
                contentService.getAllContent()
        );
    }

    // Search content - USER and ADMIN
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Content>> searchContent(
            @RequestParam Map<String, String> params) {

        String keyword = params.get("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                contentService.searchContent(keyword.trim())
        );
    }

    // Get content by ID - USER and ADMIN
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{contentId}")
    public ResponseEntity<Content> getContentById(
            @PathVariable(name = "contentId") Long contentId) {

        return ResponseEntity.ok(
                contentService.getContentById(contentId)
        );
    }

    // Update content - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{contentId}")
    public ResponseEntity<Content> updateContent(
            @PathVariable(name = "contentId") Long contentId,
            @Valid @RequestBody Content content) {

        return ResponseEntity.ok(
                contentService.updateContent(
                        contentId,
                        content
                )
        );
    }

    // Assign authors and categories - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{contentId}/metadata")
    public ResponseEntity<Content> updateContentMetadata(
            @PathVariable(name = "contentId") Long contentId,
            @Valid @RequestBody ContentMetadataRequest request) {

        Content updatedContent =
                contentService.updateContentMetadata(
                        contentId,
                        request.getAuthorIds(),
                        request.getCategoryIds()
                );

        return ResponseEntity.ok(updatedContent);
    }

    // Delete content - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{contentId}")
    public ResponseEntity<String> deleteContent(
            @PathVariable(name = "contentId") Long contentId) {

        contentService.deleteContent(contentId);

        return ResponseEntity.ok(
                "Content deleted successfully"
        );
    }
}