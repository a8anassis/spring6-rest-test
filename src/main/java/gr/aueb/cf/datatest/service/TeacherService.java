package gr.aueb.cf.datatest.service;

import gr.aueb.cf.datatest.core.excpetions.AppObjectInvalidArgumentException;
import gr.aueb.cf.datatest.core.excpetions.AppObjectAlreadyExists;
import gr.aueb.cf.datatest.core.filters.Paginated;
import gr.aueb.cf.datatest.core.filters.TeacherFilters;
import gr.aueb.cf.datatest.core.specifications.TeacherSpecification;
import gr.aueb.cf.datatest.dto.TeacherInsertDTO;
import gr.aueb.cf.datatest.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.datatest.mapper.Mapper;
import gr.aueb.cf.datatest.model.Attachment;
import gr.aueb.cf.datatest.model.PersonalInfo;
import gr.aueb.cf.datatest.model.Teacher;
import gr.aueb.cf.datatest.repository.PersonalInfoRepository;
import gr.aueb.cf.datatest.repository.TeacherRepository;
import gr.aueb.cf.datatest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepository teacherRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final PersonalInfoRepository personalInfoRepository;


    @Transactional
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO, MultipartFile amkaFile)
            throws AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        // Check if the VAT already exists in the system
        if (userRepository.findByVat(teacherInsertDTO.getUser().getVat()).isPresent()) {
            throw new AppObjectAlreadyExists("VAT", "User with VAT " + teacherInsertDTO.getUser().getVat() + " already exists.");
        }

        // Check if the AMKA already exists in the system
        if (personalInfoRepository.findByAmka(teacherInsertDTO.getPersonalInfo().getAmka()).isPresent()) {
            throw new AppObjectAlreadyExists("AMKA", "PersonalInfo with AMKA " + teacherInsertDTO.getPersonalInfo().getAmka() + " already exists.");
        }

        if (userRepository.findByUsername(teacherInsertDTO.getUser().getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("Username", "User with username " + teacherInsertDTO.getUser().getUsername() + " already exists.");
        }

        Teacher teacher = mapper.mapToTeacherEntity(teacherInsertDTO);
        //teacher.getUser().setPassword(passwordEncoder.encode(teacher.getUser().getPassword()));

//        if (!amkaFile.isEmpty()) {
//            saveAmkaFile(teacher.getPersonalInfo(), amkaFile);
//        }

        // Save the teacher (cascades to User and PersonalInfo)
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Return a TeacherReadOnlyDTO after saving
        return mapper.mapToTeacherReadOnlyDTO(savedTeacher);
    }


    @Transactional
    public Paginated<TeacherReadOnlyDTO> getTeachersFilteredPaginated(TeacherFilters filters) {
        var filtered = teacherRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToTeacherReadOnlyDTO));
    }


    // Method to handle saving the uploaded AMKA file and linking it to PersonalInfo
    @Transactional(rollbackFor = Exception.class)
    public void saveAmkaFile(PersonalInfo personalInfo, MultipartFile amkaFile)
            throws IOException {
        if (amkaFile != null && !amkaFile.isEmpty()) {
            // Get the original file name
            String originalFilename = amkaFile.getOriginalFilename();

            // Generate a unique saved name for the file to avoid conflicts
            String savedName = UUID.randomUUID().toString() + getFileExtension(originalFilename);

            // Define the file path where the file will be stored
            String uploadDirectory = "uploads/";
            Path filePath = Paths.get(uploadDirectory + savedName);

            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());

            // Write the file to the file system
            Files.write(filePath, amkaFile.getBytes());

            // Create an Attachment object to store file metadata
            Attachment attachment = new Attachment();
            attachment.setFilename(originalFilename);           // Set the original file name
            attachment.setSavedName(savedName);                 // Set the saved (unique) file name
            attachment.setFilePath(filePath.toString());        // Full path to the file
            attachment.setContentType(amkaFile.getContentType()); // Set the content type (MIME type)
            attachment.setExtension(getFileExtension(originalFilename)); // Set the file extension

            // Link the Attachment to the PersonalInfo entity
            personalInfo.setAmkaFile(attachment);
        }
    }

    // Helper method to extract the file extension
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }


    @Transactional
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    @Transactional
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return teacherRepository.findAll(pageable).map(mapper::mapToTeacherReadOnlyDTO);
    }

    @Transactional
    public Page<TeacherReadOnlyDTO> getPaginatedSortedTeachers(int page, int size, String sortBy, String sortDirection) {
        // Create Sort object based on the direction and property
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        // Create Pageable object with page number, size, and sort
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch the paginated and sorted result
        return teacherRepository.findAll(pageable).map(mapper::mapToTeacherReadOnlyDTO);
    }


    private Specification<Teacher> getSpecsFromFilters(TeacherFilters filters) {
        return Specification
                .where(TeacherSpecification.trStringFieldLike("uuid", filters.getUuid()))
                .and(TeacherSpecification.teacherUserVatIs(filters.getUserVat()))
                .and(TeacherSpecification.trPersonalInfoAmkaIs(filters.getUserAmka()))
                .and(TeacherSpecification.trUserIsActive(filters.getActive()));
    }
}
