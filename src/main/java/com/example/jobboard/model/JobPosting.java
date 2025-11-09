package com.example.jobboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề công việc không được để trống.")
    @Size(max = 150, message = "Tiêu đề tối đa 150 ký tự.")
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Mô tả công việc không được để trống.")
    @Lob
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Vị trí làm việc không được để trống.")
    @Column(nullable = false, length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobType jobType;

    private String salary;

    private Integer status = 1; // 1=active, 0=draft, -1=deleted

    @Column(nullable = false)
    private LocalDate postedDate = LocalDate.now();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Quan hệ với Company ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // --- Hooks ---
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
