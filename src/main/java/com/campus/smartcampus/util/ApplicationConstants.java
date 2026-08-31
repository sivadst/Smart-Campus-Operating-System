package com.campus.smartcampus.util;

public final class ApplicationConstants {

    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Pagination defaults
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIR = "desc";
    public static final int MAX_PAGE_SIZE = 100;

    // API versioning
    public static final String API_V1 = "/api/v1";

    // Roles
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_FACULTY = "FACULTY";
    public static final String ROLE_STUDENT = "STUDENT";

    // Attendance thresholds
    public static final double MIN_ATTENDANCE_PERCENTAGE = 75.0;

    // Academic year format
    public static final String ACADEMIC_YEAR_PATTERN = "^\\d{4}-\\d{4}$";
}
