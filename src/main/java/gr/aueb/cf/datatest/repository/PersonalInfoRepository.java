package gr.aueb.cf.datatest.repository;

import gr.aueb.cf.datatest.model.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long>, JpaSpecificationExecutor<PersonalInfo> {
    Optional<PersonalInfo> findByAmka(String amka);
}
