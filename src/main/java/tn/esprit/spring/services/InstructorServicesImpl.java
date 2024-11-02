package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices {

    private static final Logger logger = LogManager.getLogger(InstructorServicesImpl.class);

    private final IInstructorRepository instructorRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        logger.info("Adding new instructor: {}", instructor);
        Instructor result = instructorRepository.save(instructor);
        logger.info("Instructor added successfully with ID: {}", result.getNumInstructor());
        return result;
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        logger.info("Retrieving all instructors");
        List<Instructor> instructors = instructorRepository.findAll();
        logger.info("Retrieved {} instructors", instructors.size());
        return instructors;
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        logger.info("Updating instructor with ID: {}", instructor.getNumInstructor());
        Instructor result = instructorRepository.save(instructor);
        logger.info("Instructor updated successfully with ID: {}", result.getNumInstructor());
        return result;
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        logger.info("Retrieving instructor with ID: {}", numInstructor);
        Instructor instructor = instructorRepository.findById(numInstructor).orElse(null);
        if (instructor != null) {
            logger.info("Instructor retrieved successfully with ID: {}", numInstructor);
        } else {
            logger.warn("Instructor with ID: {} not found", numInstructor);
        }
        return instructor;
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        logger.info("Adding instructor and assigning to course with ID: {}", numCourse);
        Course course = courseRepository.findById(numCourse).orElse(null);
        if (course == null) {
            logger.warn("Course with ID: {} not found", numCourse);
            return null;
        }
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);
        Instructor result = instructorRepository.save(instructor);
        logger.info("Instructor added and assigned to course successfully. Instructor ID: {}, Course ID: {}", result.getNumInstructor(), numCourse);
        return result;
    }
}
