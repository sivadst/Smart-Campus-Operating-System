package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.NoticeRequest;
import com.campus.smartcampus.dto.response.NoticeResponse;
import com.campus.smartcampus.entity.Notice;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.NoticeRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeResponse createNotice(NoticeRequest request, UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory() != null ? request.getCategory() : NoticeCategory.GENERAL)
                .isPinned(request.isPinned())
                .expiresAt(request.getExpiresAt())
                .author(author)
                .targetRole(request.getTargetRole())
                .attachmentUrl(request.getAttachmentUrl())
                .build();

        Notice saved = noticeRepository.save(notice);
        log.info("Created notice: {} by user {}", saved.getTitle(), authorId);
        return mapToResponse(saved);
    }

    @Transactional
    public NoticeResponse publishNotice(UUID noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ResourceNotFoundException("Notice", "id", noticeId));

        notice.setPublished(true);
        notice.setPublishedAt(Instant.now());

        Notice saved = noticeRepository.save(notice);
        log.info("Published notice: {}", saved.getTitle());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponse> getPublishedNotices(Pageable pageable) {
        return noticeRepository.findAllByIsPublishedTrueOrderByIsPinnedDescPublishedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNoticeById(UUID id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice", "id", id));
        return mapToResponse(notice);
    }

    @Transactional
    public void deleteNotice(UUID id) {
        if (!noticeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notice", "id", id);
        }
        noticeRepository.deleteById(id);
        log.info("Deleted notice: {}", id);
    }

    private NoticeResponse mapToResponse(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .isPinned(notice.isPinned())
                .isPublished(notice.isPublished())
                .publishedAt(notice.getPublishedAt())
                .expiresAt(notice.getExpiresAt())
                .authorId(notice.getAuthor().getId())
                .authorName(notice.getAuthor().getFirstName() + " " + notice.getAuthor().getLastName())
                .targetRole(notice.getTargetRole())
                .attachmentUrl(notice.getAttachmentUrl())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
