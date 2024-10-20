package gr.aueb.cf.datatest.model;

import gr.aueb.cf.datatest.model.static_data.EducationalUnit;
import jakarta.persistence.*;
import lombok.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "employees")
public class Employee extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Getter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(
            name = "employees_edu_units"
    )
    private Set<EducationalUnit> eduUnits = new HashSet<>();


    public Set<EducationalUnit> getAllEduUnits() {
        if (eduUnits == null) eduUnits = new HashSet<>();
        return Collections.unmodifiableSet(eduUnits);
    }

    public void addEducationalUnit(EducationalUnit educationalUnit) {
        if (eduUnits == null) eduUnits = new HashSet<>();
        eduUnits.add(educationalUnit);
    }

    public boolean hasEducationalUnits(){
        return eduUnits != null && !eduUnits.isEmpty();
    }
}
