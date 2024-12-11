package com.gridsig.audits.repository;

import com.gridsig.audits.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Search by name containing a substring, case-insensitive
    List<Person> findByNameContainingIgnoreCase(String name);

    // Search by exact gender match
    List<Person> findByGender(String gender);

    // Search by exact age match
    List<Person> findByAge(Integer age);

    // Combine all filters if needed (Optional)
    List<Person> findByNameContainingIgnoreCaseAndGenderAndAge(String name, String gender, Integer age);
}
