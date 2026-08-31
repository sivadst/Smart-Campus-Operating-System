package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.BookCategory;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private UUID id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer publishedYear;
    private BookCategory category;
    private int totalCopies;
    private int availableCopies;
    private String shelfLocation;
    private boolean isActive;
}
