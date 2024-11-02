package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private static final Logger logger = LogManager.getLogger(InstructorRestController.class);

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public Instructor addInstructor(@RequestBody Instructor instructor){
        logger.info("Adding new instructor: {}", instructor);
        Instructor result = instructorServices.addInstructor(instructor);
        logger.info("Instructor added successfully with ID: {}", result.getNumInstructor());
        return result;
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public Instructor addAndAssignToInstructor(@RequestBody Instructor instructor, @PathVariable("numCourse") Long numCourse){
        logger.info("Adding instructor and assigning to course with ID: {}", numCourse);
        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        logger.info("Instructor added and assigned to course successfully. Instructor ID: {}, Course ID: {}", result.getNumInstructor(), numCourse);
        return result;
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<Instructor> getAllInstructors(){
        logger.info("Retrieving all instructors");
        List<Instructor> instructors = instructorServices.retrieveAllInstructors();
        logger.info("Retrieved {} instructors", instructors.size());
        return instructors;
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public Instructor updateInstructor(@RequestBody Instructor instructor){
        logger.info("Updating instructor with ID: {}", instructor.getNumInstructor());
        Instructor result = instructorServices.updateInstructor(instructor);
        logger.info("Instructor updated successfully with ID: {}", result.getNumInstructor());
        return result;
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public Instructor getById(@PathVariable("id-instructor") Long numInstructor){
        logger.info("Retrieving instructor with ID: {}", numInstructor);
        Instructor result = instructorServices.retrieveInstructor(numInstructor);
        if (result != null) {
            logger.info("Instructor retrieved successfully with ID: {}", numInstructor);
        } else {
            logger.warn("Instructor with ID: {} not found", numInstructor);
        }
        return result;
    }

}
