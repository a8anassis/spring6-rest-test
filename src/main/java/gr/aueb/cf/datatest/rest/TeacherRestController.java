package gr.aueb.cf.datatest.rest;

import gr.aueb.cf.datatest.core.excpetions.AppObjectInvalidArgumentException;
import gr.aueb.cf.datatest.core.excpetions.AppObjectAlreadyExists;
import gr.aueb.cf.datatest.core.excpetions.AppServerException;
import gr.aueb.cf.datatest.dto.PersonalInfoInsertDTO;
import gr.aueb.cf.datatest.dto.TeacherInsertDTO;
import gr.aueb.cf.datatest.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.datatest.dto.UserInsertDTO;
import gr.aueb.cf.datatest.mapper.Mapper;
import gr.aueb.cf.datatest.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import gr.aueb.cf.datatest.core.excpetions.ValidationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherRestController {


    private final TeacherService teacherService;
    private final Mapper mapper;

    @GetMapping("/teachers")
    public ResponseEntity<Page<TeacherReadOnlyDTO>> getPaginatedTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<TeacherReadOnlyDTO> teachersPage = teacherService.getPaginatedTeachers(page, size);
        return new ResponseEntity<>(teachersPage, HttpStatus.OK);
    }

    @PostMapping("/teachers/save")
    public ResponseEntity<TeacherReadOnlyDTO> saveTeacher(
            @Valid @RequestBody TeacherInsertDTO teacherInsertDTO,
//            @Valid @RequestPart(name = "teacher") TeacherInsertDTO teacherInsertDTO,
//            @Nullable @RequestParam("amkaFile") MultipartFile amkaFile,
            BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
           throw new ValidationException(bindingResult);
        }

        try {
//            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO, amkaFile);
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO, null);
            return new ResponseEntity<>(teacherReadOnlyDTO, HttpStatus.OK);
        } catch (IOException e) {
            throw new AppServerException("Attachment", "Attachment can not get uploaded");
        }
    }
}

