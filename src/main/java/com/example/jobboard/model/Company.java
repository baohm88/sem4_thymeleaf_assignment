//package com.example.jobboard.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@Entity
//@Table(name = "companies")
//public class Company {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT")
//    private Long id;
//
//    @NotBlank(message = "Tên công ty không được để trống.")
//    @Size(max = 100, message = "Tên công ty tối đa 100 ký tự.")
//    @Column(name = "name", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
//    private String name;
//
//    @NotBlank(message = "Địa chỉ công ty không được để trống.")
//    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(255)")
//    private String address;
//
//    @Pattern(regexp = "^(https?:\\/\\/)?[\\w.-]+(\\.[\\w.-]+)+[/#?]?.*$",
//            message = "Website không hợp lệ (phải là URL hợp lệ).")
//    @Column(name = "website", columnDefinition = "VARCHAR(255)")
//    private String website;
//
//    @NotBlank(message = "Email không được để trống.")
//    @Email(message = "Email không hợp lệ.")
//    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(150)")
//    private String email;
//
//    @Column(name = "status", columnDefinition = "INT DEFAULT 1 COMMENT '1=active,0=inactive,-1=deleted'")
//    private Integer status = 1;
//
//    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<JobPosting> jobPostings = new ArrayList<>();
//
//    // --- Hooks ---
//    @PrePersist
//    public void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//
//    // --- Constructors, getters, setters ---
//    public Company() {}
//    // getters and setters omitted for brevity
//}

package com.example.jobboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên công ty không được để trống.")
    @Size(max = 100, message = "Tên công ty tối đa 100 ký tự.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Địa chỉ công ty không được để trống.")
    @Column(nullable = false, length = 255)
    private String address;

    @Pattern(regexp = "^(https?:\\/\\/)?[\\w.-]+(\\.[\\w.-]+)+[/#?]?.*$",
            message = "Website không hợp lệ (phải là URL hợp lệ).")
    @Column(length = 255)
    private String website;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không hợp lệ.")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // 1=active, 0=inactive, -1=deleted
    private Integer status = 1;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPosting> jobPostings = new ArrayList<>();

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
