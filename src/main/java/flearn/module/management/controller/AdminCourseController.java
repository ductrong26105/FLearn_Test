package flearn.module.management.controller;

import flearn.module.management.dto.request.CourseRequest;
import flearn.module.management.dto.response.CourseResponse;
import flearn.entity.Course;
import flearn.enums.CourseStatus;
import flearn.repository.ClassroomRepository;
import flearn.repository.CourseRepository;
import flearn.module.management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;

    @GetMapping
    public String listCourses(@RequestParam(required = false) String keyword, Model model) {
        List<CourseResponse> courses = courseService.searchCourses(keyword);
        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        return "admin/courses/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("courseRequest", new CourseRequest());
        return "admin/courses/create";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute CourseRequest request,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/admin/courses/create";
        }
        try {
            courseService.createCourse(request);
            redirectAttributes.addFlashAttribute("successMsg", "Da tao khoa hoc thanh cong.");
            return "redirect:/admin/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/admin/courses/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        CourseResponse course = courseService.getCourseById(id);
        CourseRequest request = CourseRequest.builder()
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .status(course.getStatus())
                .build();
        model.addAttribute("courseRequest", request);
        model.addAttribute("courseId", id);
        return "admin/courses/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateCourse(@PathVariable Integer id,
                               @Valid @ModelAttribute CourseRequest request,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/admin/courses/" + id + "/edit";
        }
        try {
            courseService.updateCourse(id, request);
            redirectAttributes.addFlashAttribute("successMsg", "Da cap nhat khoa hoc.");
            return "redirect:/admin/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/admin/courses/" + id + "/edit";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        CourseResponse course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        
        // Find associated classrooms
        Course courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity != null) {
            model.addAttribute("classes", classroomRepository.findByCourse(courseEntity));
        }
        return "admin/courses/detail";
    }

    @PostMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            courseService.toggleCourseStatus(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da thay doi trang thai khoa hoc.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da xoa khoa hoc.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}
