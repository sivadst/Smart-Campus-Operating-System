package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.BookCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String publisher;
    private Integer publishedYear;

    @NotNull(message = "Category is required")
    private BookCategory category;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;

    private String shelfLocation;
}
