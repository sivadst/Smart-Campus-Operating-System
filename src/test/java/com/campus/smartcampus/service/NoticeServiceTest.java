package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.NoticeRequest;
import com.campus.smartcampus.dto.response.NoticeResponse;
import com.campus.smartcampus.entity.Notice;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.NoticeRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeService Unit Tests")
class NoticeServiceTest {

    @Mock private NoticeRepository noticeRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private NoticeService noticeService;

    private User author;
    private Notice notice;
    private UUID authorId, noticeId;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        noticeId = UUID.randomUUID();

        author = User.builder().id(authorId).firstName("Admin").lastName("User").email("admin@campus.edu").role(UserRole.ADMIN).build();

        notice = Notice.builder()
                .id(noticeId)
                .title("Campus Reopening")
                .content("Campus will reopen next Monday")
                .category(NoticeCategory.GENERAL)
                .author(author)
                .isPinned(true)
                .isPublished(false)
                .build();
    }

    @Test
    @DisplayName("Should create draft notice successfully")
    void createNotice_ValidRequest_ReturnsNoticeResponse() {
        NoticeRequest request = NoticeRequest.builder()
                .title("Campus Reopening").content("Campus will reopen next Monday")
                .category(NoticeCategory.GENERAL).isPinned(true)
                .build();

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        NoticeResponse response = noticeService.createNotice(request, authorId);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Campus Reopening");
        assertThat(response.isPinned()).isTrue();
    }

    @Test
    @DisplayName("Should publish draft notice")
    void publishNotice_ExistingDraft_SetsPublishedTrue() {
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        NoticeResponse response = noticeService.publishNotice(noticeId);

        assertThat(response).isNotNull();
        verify(noticeRepository).save(argThat(n -> n.isPublished() && n.getPublishedAt() != null));
    }
}
