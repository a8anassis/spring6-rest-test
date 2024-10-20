package gr.aueb.cf.datatest.mapper;

import gr.aueb.cf.datatest.dto.*;
import gr.aueb.cf.datatest.model.PersonalInfo;
import gr.aueb.cf.datatest.model.Teacher;
import gr.aueb.cf.datatest.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        TeacherReadOnlyDTO dto = new TeacherReadOnlyDTO();

        dto.setId(teacher.getId());
        dto.setUuid(teacher.getUuid());
        dto.setIsActive(teacher.getIsActive());

        // Map User to UserReadOnlyDTO
        UserReadOnlyDTO userDTO = new UserReadOnlyDTO();
        userDTO.setFirstname(teacher.getUser().getFirstname());
        userDTO.setLastname(teacher.getUser().getLastname());
        userDTO.setVat(teacher.getUser().getVat());
        dto.setUser(userDTO);

        // Map PersonalInfo to PersonalInfoReadOnlyDTO
        PersonalInfoReadOnlyDTO personalInfoDTO = new PersonalInfoReadOnlyDTO();
        personalInfoDTO.setAmka(teacher.getPersonalInfo().getAmka());
        personalInfoDTO.setIdentityNumber(teacher.getPersonalInfo().getIdentityNumber());
        dto.setPersonalInfo(personalInfoDTO);

        return dto;
    }


    public Teacher mapToTeacherEntity(TeacherInsertDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setIsActive(dto.getIsActive());

        // Map fields from UserDTO
        UserInsertDTO userDTO = dto.getUser();
        User user = new User();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setVat(userDTO.getVat());
        user.setFatherName(userDTO.getFatherName());
        user.setFatherLastname(userDTO.getFatherLastname());
        user.setMotherName(userDTO.getMotherName());
        user.setMotherLastname(userDTO.getMotherLastname());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setGender(userDTO.getGender());
        user.setRole(userDTO.getRole());
        user.setIsActive(dto.getIsActive());
        teacher.setUser(user);  // Set User entity to Teacher

        // Map fields from PersonalInfoDTO
        PersonalInfoInsertDTO personalInfoDTO = dto.getPersonalInfo();
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setAmka(personalInfoDTO.getAmka());
        personalInfo.setIdentityNumber(personalInfoDTO.getIdentityNumber());
        personalInfo.setPlaceOfBirth(personalInfoDTO.getPlaceOfBirth());
        personalInfo.setMunicipalityOfRegistration(personalInfoDTO.getMunicipalityOfRegistration());
        teacher.setPersonalInfo(personalInfo);  // Set PersonalInfo entity to Teacher

        return teacher;
    }
}
