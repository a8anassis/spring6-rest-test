package gr.aueb.cf.datatest.dto;

import gr.aueb.cf.datatest.model.Attachment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherInsertDTO {

    @NotNull(message = "isActive field is required")
    private Boolean isActive;

    @NotNull(message = "User details are required")
    private UserInsertDTO user;

    @NotNull(message = "Personal Info is required")
    private PersonalInfoInsertDTO personalInfo;
}
