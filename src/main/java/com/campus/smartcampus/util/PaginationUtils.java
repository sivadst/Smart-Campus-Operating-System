package com.campus.smartcampus.util;

import com.campus.smartcampus.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public final class PaginationUtils {

    private PaginationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> PaginatedResponse<T> buildPaginatedResponse(Page<T> page) {
        PaginatedResponse.PaginationMeta meta = PaginatedResponse.PaginationMeta.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        return PaginatedResponse.<T>builder()
                .success(true)
                .data(page.getContent())
                .pagination(meta)
                .build();
    }

    public static <T> PaginatedResponse<T> buildPaginatedResponse(List<T> items, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);

        PaginatedResponse.PaginationMeta meta = PaginatedResponse.PaginationMeta.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .build();

        return PaginatedResponse.<T>builder()
                .success(true)
                .data(items)
                .pagination(meta)
                .build();
    }
}
