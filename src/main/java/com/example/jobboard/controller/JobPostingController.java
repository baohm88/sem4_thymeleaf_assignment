package com.example.jobboard.controller;

import com.example.jobboard.model.JobPosting;
import com.example.jobboard.model.JobType;
import com.example.jobboard.repository.CompanyRepository;
import com.example.jobboard.repository.JobPostingRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobPostingController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // 1️⃣ Danh sách Job
    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobPostingRepository.findAll());
        model.addAttribute("companies", companyRepository.findAll());
        return "job-list";
    }

    // 2️⃣ Form thêm mới — ✅ chặn nếu chưa có công ty nào
    @GetMapping("/new")
    public String showCreateForm(Model model, RedirectAttributes redirectAttributes) {
        if (companyRepository.count() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Hiện chưa có công ty nào để đăng tuyển mới. Hãy thêm ít nhất 1 công ty trước.");
            return "redirect:/companies";
        }

        model.addAttribute("job", new JobPosting());
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("jobTypes", JobType.values());
        return "job-form";
    }

    // 3️⃣ Lưu mới
    @PostMapping("/save")
    public String saveJob(@Valid @ModelAttribute("job") JobPosting job,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "job-form";
        }

        // ✅ Lấy lại company thực từ DB (tránh lỗi Transient object)
        if (job.getCompany() != null && job.getCompany().getId() != null) {
            job.setCompany(companyRepository.findById(job.getCompany().getId()).orElse(null));
        }

        if (job.getCompany() == null) {
            result.rejectValue("company.id", "error.job", "Công ty không hợp lệ.");
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "job-form";
        }

        jobPostingRepository.save(job);
        redirectAttributes.addFlashAttribute("successMessage", "✅ Đăng tin tuyển dụng mới thành công!");
        return "redirect:/jobs";
    }


    // 4️⃣ Form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Không tìm thấy tin tuyển dụng!");
            return "redirect:/jobs";
        }
        model.addAttribute("job", job);
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("jobTypes", JobType.values());
        return "job-form";
    }

    @PostMapping("/update/{id}")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("job") JobPosting job,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "job-form";
        }

        JobPosting existing = jobPostingRepository.findById(id).orElse(null);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Không tìm thấy tin tuyển dụng!");
            return "redirect:/jobs";
        }

        // ✅ Lấy lại company thực từ DB
        if (job.getCompany() != null && job.getCompany().getId() != null) {
            job.setCompany(companyRepository.findById(job.getCompany().getId()).orElse(null));
        }

        if (job.getCompany() == null) {
            result.rejectValue("company.id", "error.job", "Công ty không hợp lệ.");
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "job-form";
        }

        job.setId(id);
        job.setCreatedAt(existing.getCreatedAt());
        jobPostingRepository.save(job);

        redirectAttributes.addFlashAttribute("successMessage", "✅ Cập nhật tin tuyển dụng thành công!");
        return "redirect:/jobs";
    }


    // 6️⃣ Xóa
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (jobPostingRepository.existsById(id)) {
            jobPostingRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "🗑️ Đã xóa tin tuyển dụng!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Không tìm thấy tin tuyển dụng để xóa!");
        }
        return "redirect:/jobs";
    }

    // 7️⃣ Chi tiết tin tuyển dụng
    @GetMapping("/details/{id}")
    public String viewJobDetails(@PathVariable("id") Long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Không tìm thấy tin tuyển dụng!");
            return "redirect:/jobs";
        }
        model.addAttribute("job", job);
        return "job-details";
    }
}
