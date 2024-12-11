package com.gridsig.audits.controller;

import com.gridsig.audits.entity.Person;
import com.gridsig.audits.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonService personService;

    /**
     * Endpoint to save a new Person.
     *
     * @param person the Person entity to save
     * @return ResponseEntity indicating success or failure
     */
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> save(@RequestBody Person person) {
        try {
            person = personService.save(person);
            logger.info("Person saved successfully: {}", person);
            return ResponseEntity.status(HttpStatus.CREATED).body(person);
        } catch (Exception e) {
            logger.error("Error saving person: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint to update an existing Person.
     *
     * @param person the Person entity with updated data
     * @return ResponseEntity indicating success or failure
     */
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@RequestBody Person person) {
        try {
            personService.update(person);
            logger.info("Person updated successfully: {}", person);
            return ResponseEntity.ok("Person updated successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Person not found: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating person: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating person");
        }
    }

    /**
     * Endpoint to list all persons with optional filters.
     *
     * @param name   optional name filter
     * @param gender optional gender filter
     * @param age    optional age filter
     * @return ResponseEntity containing the list of persons
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> listPersons(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age) {
        try {
            List<Person> persons = personService.listPersons(name, gender, age);
            logger.info("Persons listed successfully with filters - Name: {}, Gender: {}, Age: {}", name, gender, age);
            return ResponseEntity.ok(persons);
        } catch (Exception e) {
            logger.error("Error listing persons: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to get a person by ID.
     *
     * @param id the ID of the person
     * @return ResponseEntity containing the person details or an error message
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        try {
            Person person = personService.getPersonById(id);
            logger.info("Person retrieved successfully with ID: {}", id);
            return ResponseEntity.ok(person);
        } catch (IllegalArgumentException e) {
            logger.warn("Person not found: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving person: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to delete a person by ID.
     *
     * @param id the ID of the person
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            logger.info("Person deleted successfully with ID: {}", id);
            return ResponseEntity.ok("Person deleted successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Person not found: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting person: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting person");
        }
    }

    /**
     * Endpoint to check the status of the service.
     *
     * @return Status message
     */
    @GetMapping("/")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Service is working");
    }
}
