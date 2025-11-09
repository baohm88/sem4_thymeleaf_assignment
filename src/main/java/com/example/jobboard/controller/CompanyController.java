package com.example.jobboard.controller;

import com.example.jobboard.model.Company;
import com.example.jobboard.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    // 1️⃣ Danh sách công ty
    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "company-list";
    }

    // 2️⃣ Hiển thị form thêm mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company());
        return "company-form";
    }

    // 3️⃣ Xử lý thêm mới
    @PostMapping("/save")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "company-form";
        }

        companyRepository.save(company);
        redirectAttributes.addFlashAttribute("successMessage", "Công ty đã được thêm mới thành công!");
        return "redirect:/companies";
    }

    // 4️⃣ Hiển thị form cập nhật
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Company company = companyRepository.findById(id)
                .orElse(null);

        if (company == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công ty cần chỉnh sửa!");
            return "redirect:/companies";
        }

        model.addAttribute("company", company);
        return "company-form"; // Dùng lại cùng form cho create/edit
    }

    // 5️⃣ Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("company") Company company,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "company-form";
        }

        Company existing = companyRepository.findById(id).orElse(null);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công ty để cập nhật!");
            return "redirect:/companies";
        }

        company.setId(id);
        company.setCreatedAt(existing.getCreatedAt());
        companyRepository.save(company);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin công ty thành công!");
        return "redirect:/companies";
    }

    // 6️⃣ Xóa công ty
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "🗑️ Đã xóa công ty thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công ty cần xóa!");
        }
        return "redirect:/companies";
    }
}
