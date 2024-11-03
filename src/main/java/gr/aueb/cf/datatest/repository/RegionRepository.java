package gr.aueb.cf.datatest.repository;

import gr.aueb.cf.datatest.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

}
