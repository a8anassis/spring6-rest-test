package gr.aueb.cf.datatest.model.static_data;

import gr.aueb.cf.datatest.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "educational_units")
public class EducationalUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Getter(AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "eduUnits")
    private Set<Employee> employees = new HashSet<>();

}
