package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.response.NoticeResponse;
import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeController Unit Tests")
class NoticeControllerTest {

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    private MockMvc mockMvc;

    private UUID noticeId;
    private NoticeResponse noticeResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        noticeId = UUID.randomUUID();

        noticeResponse = NoticeResponse.builder()
                .id(noticeId)
                .title("Mid-Semester Examination Schedule Released")
                .content("The mid-semester examination for all departments begins on November 10th.")
                .category(NoticeCategory.EXAMINATION)
                .isPinned(true)
                .isPublished(true)
                .publishedAt(Instant.now())
                .authorId(UUID.randomUUID())
                .authorName("Dr. Rajan Menon")
                .targetRole(UserRole.STUDENT)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/notices should return paginated published notices")
    void getPublishedNotices_ReturnsPage() throws Exception {
        Page<NoticeResponse> page = new PageImpl<>(List.of(noticeResponse));
        when(noticeService.getPublishedNotices(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/notices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Mid-Semester Examination Schedule Released"))
                .andExpect(jsonPath("$.data.content[0].category").value("EXAMINATION"))
                .andExpect(jsonPath("$.data.content[0].pinned").value(true));

        verify(noticeService).getPublishedNotices(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/notices/{id} should return notice by ID")
    void getNoticeById_ReturnsNotice() throws Exception {
        when(noticeService.getNoticeById(noticeId)).thenReturn(noticeResponse);

        mockMvc.perform(get("/api/v1/notices/{id}", noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(noticeId.toString()))
                .andExpect(jsonPath("$.data.authorName").value("Dr. Rajan Menon"))
                .andExpect(jsonPath("$.data.targetRole").value("STUDENT"));

        verify(noticeService).getNoticeById(noticeId);
    }

    @Test
    @DisplayName("PUT /api/v1/notices/{id}/publish should publish notice and return 200 OK")
    void publishNotice_ReturnsPublished() throws Exception {
        when(noticeService.publishNotice(noticeId)).thenReturn(noticeResponse);

        mockMvc.perform(put("/api/v1/notices/{id}/publish", noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Notice published successfully"))
                .andExpect(jsonPath("$.data.title").value("Mid-Semester Examination Schedule Released"));

        verify(noticeService).publishNotice(noticeId);
    }

    @Test
    @DisplayName("DELETE /api/v1/notices/{id} should delete notice and return 200 OK")
    void deleteNotice_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/notices/{id}", noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Notice deleted successfully"));

        verify(noticeService).deleteNotice(noticeId);
    }
}
