package tn.esprit.spring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addInstructor_ShouldAddInstructor() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.addInstructor(instructor);

        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void retrieveAllInstructors_ShouldReturnListOfInstructors() {
        Instructor instructor1 = new Instructor();
        Instructor instructor2 = new Instructor();
        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);

        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void updateInstructor_ShouldUpdateInstructor() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.updateInstructor(instructor);

        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void retrieveInstructor_ShouldReturnInstructor_WhenExists() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor result = instructorServices.retrieveInstructor(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void retrieveInstructor_ShouldReturnNull_WhenNotExists() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor result = instructorServices.retrieveInstructor(1L);

        assertNull(result);
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void addInstructorAndAssignToCourse_ShouldAddInstructorAndAssignCourse_WhenCourseExists() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        Course course = new Course();
        course.setNumCourse(2L);

        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 2L);

        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        assertNotNull(result.getCourses());
        assertTrue(result.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(2L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void addInstructorAndAssignToCourse_ShouldReturnNull_WhenCourseNotExists() {
        Instructor instructor = new Instructor();

        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 2L);

        assertNull(result);
        verify(courseRepository, times(1)).findById(2L);
        verify(instructorRepository, never()).save(instructor);
    }
}
