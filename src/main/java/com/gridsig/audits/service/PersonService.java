package com.gridsig.audits.service;

import com.gridsig.audits.entity.Person;
import com.gridsig.audits.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    PersonRepository personRepository;


    /**
     * Saves a new Person entity to the repository.
     *
     * @param person the person to save
     * @return
     * @throws IllegalArgumentException if the person data is invalid
     */
    public Person save(Person person) {
        if (person == null || StringUtils.isBlank(person.getName()) || person.getAge() <= 0) {
            logger.error("Invalid person data: {}", person);
            throw new IllegalArgumentException("Invalid person data provided");
        }
        logger.info("Saving person: {}", person);
        person = personRepository.save(person);
        logger.info("Person saved successfully: {}", person);

        return person;
    }

    /**
     * Updates an existing Person entity with new values.
     *
     * @param person the updated person data
     * @throws IllegalArgumentException if the person data is invalid or the ID does not exist
     */
    public void update(Person person) {
        if (person == null || person.getId() == null) {
            logger.error("Invalid person data or missing ID: {}", person);
            throw new IllegalArgumentException("Invalid person data or missing ID");
        }

        logger.info("Updating person with ID: {}", person.getId());

        // Find the existing person or throw an exception if not found
        Person existingPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found", person.getId());
                    return new IllegalArgumentException("Person with ID " + person.getId() + " not found");
                });

        // Update the fields of the existing person
        updateExistingPerson(existingPerson, person);

        // Save the updated person
        personRepository.save(existingPerson);
        logger.info("Person updated successfully: {}", existingPerson);
    }

    /**
     * Updates the fields of an existing Person with non-null and non-default values from the updated Person.
     *
     * @param existingPerson the existing person to update
     * @param updatedPerson  the updated person data
     */
    private void updateExistingPerson(Person existingPerson, Person updatedPerson) {
        logger.debug("Updating fields for person with ID: {}", existingPerson.getId());

        boolean updated = false;

        if (StringUtils.isNotBlank(updatedPerson.getName()) &&
                !updatedPerson.getName().equals(existingPerson.getName())) {
            logger.debug("Updating name from '{}' to '{}'", existingPerson.getName(), updatedPerson.getName());
            existingPerson.setName(updatedPerson.getName());
            updated = true;
        }

        if (updatedPerson.getAge() > 0 && updatedPerson.getAge() != existingPerson.getAge()) {
            logger.debug("Updating age from '{}' to '{}'", existingPerson.getAge(), updatedPerson.getAge());
            existingPerson.setAge(updatedPerson.getAge());
            updated = true;
        }

        if (updatedPerson.getGender() != null &&
                !updatedPerson.getGender().equals(existingPerson.getGender())) {
            logger.debug("Updating gender from '{}' to '{}'", existingPerson.getGender(), updatedPerson.getGender());
            existingPerson.setGender(updatedPerson.getGender());
            updated = true;
        }

        if (!updated) {
            logger.info("No fields were updated for person with ID: {}", existingPerson.getId());
        }
    }

    /**
     * Lists all persons with optional search filters.
     *
     * @param name   the name to search for (optional)
     * @param gender the gender to search for (optional)
     * @param age    the age to search for (optional)
     * @return a list of persons matching the filters
     */
    public List<Person> listPersons(String name, String gender, Integer age) {
        logger.info("Listing persons with filters - Name: {}, Gender: {}, Age: {}", name, gender, age);

        // Filter based on the provided parameters
        if (name != null && gender != null && age != null) {
            return personRepository.findByNameContainingIgnoreCaseAndGenderAndAge(name, gender, age);
        } else if (name != null) {
            return personRepository.findByNameContainingIgnoreCase(name);
        } else if (gender != null) {
            return personRepository.findByGender(gender);
        } else if (age != null) {
            return personRepository.findByAge(age);
        }

        // If no filters provided, return all persons
        return personRepository.findAll();
    }

    /**
     * Retrieves a person by ID.
     */
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person with ID " + id + " not found"));
    }

    /**
     * Deletes a person by ID.
     */
    public void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person with ID " + id + " not found"));
        personRepository.delete(person);
        logger.info("Person with ID {} deleted successfully", id);
    }
}
