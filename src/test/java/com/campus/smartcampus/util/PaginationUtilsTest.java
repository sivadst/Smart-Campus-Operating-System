package com.campus.smartcampus.util;

import com.campus.smartcampus.dto.response.PaginatedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaginationUtils Unit Tests")
class PaginationUtilsTest {

    @Test
    @DisplayName("buildPaginatedResponse(Page) should map Spring Page to PaginatedResponse")
    void buildPaginatedResponse_FromPage_MapsCorrectly() {
        List<String> items = List.of("Course A", "Course B", "Course C");
        Page<String> page = new PageImpl<>(items, PageRequest.of(0, 10), 25);

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(page);

        assertTrue(response.isSuccess());
        assertEquals(3, response.getData().size());
        assertEquals("Course A", response.getData().get(0));

        PaginatedResponse.PaginationMeta meta = response.getPagination();
        assertEquals(0, meta.getPage());
        assertEquals(10, meta.getSize());
        assertEquals(25, meta.getTotalElements());
        assertEquals(3, meta.getTotalPages());
        assertTrue(meta.isHasNext());
        assertFalse(meta.isHasPrevious());
    }

    @Test
    @DisplayName("buildPaginatedResponse(Page) should handle last page correctly")
    void buildPaginatedResponse_LastPage_NoNext() {
        List<String> items = List.of("Item Z");
        Page<String> page = new PageImpl<>(items, PageRequest.of(2, 10), 21);

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(page);

        PaginatedResponse.PaginationMeta meta = response.getPagination();
        assertEquals(2, meta.getPage());
        assertFalse(meta.isHasNext());
        assertTrue(meta.isHasPrevious());
    }

    @Test
    @DisplayName("buildPaginatedResponse(Page) should handle empty page")
    void buildPaginatedResponse_EmptyPage_ReturnsEmptyData() {
        Page<String> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(page);

        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        assertEquals(0, response.getPagination().getTotalElements());
        assertEquals(0, response.getPagination().getTotalPages());
        assertFalse(response.getPagination().isHasNext());
        assertFalse(response.getPagination().isHasPrevious());
    }

    @Test
    @DisplayName("buildPaginatedResponse(List) should build response from raw list with manual pagination")
    void buildPaginatedResponse_FromList_BuildsCorrectly() {
        List<String> items = List.of("Room 101", "Room 102");

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(items, 1, 5, 12);

        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());

        PaginatedResponse.PaginationMeta meta = response.getPagination();
        assertEquals(1, meta.getPage());
        assertEquals(5, meta.getSize());
        assertEquals(12, meta.getTotalElements());
        assertEquals(3, meta.getTotalPages());
        assertTrue(meta.isHasNext());
        assertTrue(meta.isHasPrevious());
    }

    @Test
    @DisplayName("buildPaginatedResponse(List) should handle first page of many")
    void buildPaginatedResponse_FromList_FirstPage() {
        List<String> items = List.of("A", "B", "C");

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(items, 0, 3, 9);

        PaginatedResponse.PaginationMeta meta = response.getPagination();
        assertEquals(0, meta.getPage());
        assertEquals(3, meta.getTotalPages());
        assertTrue(meta.isHasNext());
        assertFalse(meta.isHasPrevious());
    }

    @Test
    @DisplayName("buildPaginatedResponse(List) should handle last page")
    void buildPaginatedResponse_FromList_LastPage() {
        List<String> items = List.of("X");

        PaginatedResponse<String> response = PaginationUtils.buildPaginatedResponse(items, 2, 3, 7);

        PaginatedResponse.PaginationMeta meta = response.getPagination();
        assertEquals(2, meta.getPage());
        assertEquals(3, meta.getTotalPages());
        assertFalse(meta.isHasNext());
        assertTrue(meta.isHasPrevious());
    }

    @Test
    @DisplayName("Constructor should throw UnsupportedOperationException")
    void constructor_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var constructor = PaginationUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw (UnsupportedOperationException) e.getCause();
            }
        });
    }
}
