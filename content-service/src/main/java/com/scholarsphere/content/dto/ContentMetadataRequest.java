package com.scholarsphere.content.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ContentMetadataRequest {

    @NotNull(message = "Author IDs cannot be null")
    private List<Long> authorIds;

    @NotNull(message = "Category IDs cannot be null")
    private List<Long> categoryIds;

    public ContentMetadataRequest() {
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}