//package gr.aueb.cf.datatest.controller;
//
//import gr.aueb.cf.datatest.core.excpetions.AppObjectInvalidArgumentException;
//import gr.aueb.cf.datatest.core.excpetions.AppObjectAlreadyExists;
//import gr.aueb.cf.datatest.dto.PersonalInfoInsertDTO;
//import gr.aueb.cf.datatest.dto.TeacherInsertDTO;
//import gr.aueb.cf.datatest.dto.TeacherReadOnlyDTO;
//import gr.aueb.cf.datatest.dto.UserInsertDTO;
//import gr.aueb.cf.datatest.mapper.Mapper;
//import gr.aueb.cf.datatest.service.TeacherService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.ui.Model;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Controller
//@RequiredArgsConstructor
//public class TeacherController {
//
//
//    private final TeacherService teacherService;
//    private final Mapper mapper;
//
//    @GetMapping("/teachers")
//    public String getPaginatedTeachers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            Model model) {
//
//        Page<TeacherReadOnlyDTO> teachersPage = teacherService.getPaginatedTeachers(page, size);
//        model.addAttribute("teachersPage", teachersPage);
//
//        return "teachers";  // Thymeleaf template name
//    }
//
//    @GetMapping("/teachers/save")
//    public String showTeacherForm(Model model) {
//        TeacherInsertDTO teacherInsertDTO = new TeacherInsertDTO();
//        teacherInsertDTO.setUser(new UserInsertDTO());  // Initialize nested UserDTO
//        teacherInsertDTO.setPersonalInfo(new PersonalInfoInsertDTO());  // Initialize nested PersonalInfoDTO
//
//        model.addAttribute("teacherInsertDTO", teacherInsertDTO);
//        return "teacher-form";  // Thymeleaf template name
//    }
//
//
//    @PostMapping("/teachers/save")
//    public String saveTeacher(
//            @Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO teacherInsertDTO,
//            BindingResult bindingResult,
//            @RequestParam("amkaFile") MultipartFile amkaFile,
//            Model model) throws AppObjectInvalidArgumentException {
//
//        // Check if there are validation errors
//        if (bindingResult.hasErrors()) {
//            // If there are errors, return to the form with the error messages
//            return "teacher-form";
//        }
//
//        try {
//
//            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO, amkaFile);
//
//            model.addAttribute("savedTeacher", teacherReadOnlyDTO);
//            return "redirect:/teachers";
//
//        } catch (AppObjectAlreadyExists e) {
//            // Handle duplicate errors, add a meaningful message to the model for the user
//            model.addAttribute("errorMessage", e.getMessage());
//            return "teacher-form";
//        } catch (IOException e) {
//            // Handle file upload exceptions
//            model.addAttribute("errorMessage", "Error uploading file: " + e.getMessage());
//            return "teacher-form";
//        }
//    }
//
//
//
//}
