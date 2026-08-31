package com.campus.smartcampus.event;

import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class NoticePublishedEvent extends ApplicationEvent {
    private final UUID noticeId;
    private final String title;
    private final NoticeCategory category;
    private final UserRole targetRole;

    public NoticePublishedEvent(Object source, UUID noticeId, String title, NoticeCategory category, UserRole targetRole) {
        super(source);
        this.noticeId = noticeId;
        this.title = title;
        this.category = category;
        this.targetRole = targetRole;
    }
}
