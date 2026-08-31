package com.campus.smartcampus.config;

import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.repository.DepartmentRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding initial data...");
            seedUsers();
            seedDepartments();
            log.info("Data seeding completed.");
        }
    }

    private void seedUsers() {
        User admin = User.builder()
                .email("admin@smartcampus.edu")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .firstName("System")
                .lastName("Administrator")
                .role(UserRole.SUPER_ADMIN)
                .isActive(true)
                .emailVerified(true)
                .build();
        userRepository.save(admin);

        User faculty = User.builder()
                .email("faculty@smartcampus.edu")
                .passwordHash(passwordEncoder.encode("Faculty@123"))
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.FACULTY)
                .isActive(true)
                .emailVerified(true)
                .build();
        userRepository.save(faculty);

        User student = User.builder()
                .email("student@smartcampus.edu")
                .passwordHash(passwordEncoder.encode("Student@123"))
                .firstName("Jane")
                .lastName("Smith")
                .role(UserRole.STUDENT)
                .isActive(true)
                .emailVerified(true)
                .build();
        userRepository.save(student);

        log.info("Seeded 3 users (admin, faculty, student)");
    }

    private void seedDepartments() {
        String[][] departments = {
                {"CSE", "Computer Science and Engineering", "Dept of Computer Science"},
                {"ECE", "Electronics and Communication Engineering", "Dept of Electronics"},
                {"ME", "Mechanical Engineering", "Dept of Mechanical Engineering"},
                {"CE", "Civil Engineering", "Dept of Civil Engineering"},
                {"MBA", "Master of Business Administration", "School of Management"}
        };

        for (String[] dept : departments) {
            Department department = Department.builder()
                    .code(dept[0])
                    .name(dept[1])
                    .description(dept[2])
                    .build();
            departmentRepository.save(department);
        }
        log.info("Seeded {} departments", departments.length);
    }
}
