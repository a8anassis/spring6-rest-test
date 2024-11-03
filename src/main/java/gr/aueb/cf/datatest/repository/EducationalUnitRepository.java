package gr.aueb.cf.datatest.repository;

import gr.aueb.cf.datatest.model.static_data.EducationalUnit;
import gr.aueb.cf.datatest.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationalUnitRepository extends JpaRepository<EducationalUnit, Long> {

}
