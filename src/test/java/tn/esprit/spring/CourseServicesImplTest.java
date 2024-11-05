package tn.esprit.spring;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository; // Mock du référentiel

    @InjectMocks
    private CourseServicesImpl courseServices; // Service à tester

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    @Test
    public void testRetrieveAllCourses() {
        Course course1 = new Course(); // Vous pouvez initialiser les propriétés du cours ici
        Course course2 = new Course();
        List<Course> courses = Arrays.asList(course1, course2);

        // Configurer le comportement du mock
        when(courseRepository.findAll()).thenReturn(courses);

        // Appeler la méthode à tester
        List<Course> result = courseServices.retrieveAllCourses();

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll(); // Vérifier que la méthode a été appelée une fois
    }

    @Test
    public void testAddCourse() {
        Course course = new Course(); // Initialisez les propriétés selon vos besoins

        // Configurer le comportement du mock
        when(courseRepository.save(course)).thenReturn(course);

        // Appeler la méthode à tester
        Course result = courseServices.addCourse(course);

        // Vérifier le résultat
        assertNotNull(result);
        verify(courseRepository, times(1)).save(course); // Vérifier que save a été appelé
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course(); // Initialisez les propriétés selon vos besoins

        // Configurer le comportement du mock
        when(courseRepository.save(course)).thenReturn(course);

        // Appeler la méthode à tester
        Course result = courseServices.updateCourse(course);

        // Vérifier le résultat
        assertNotNull(result);
        verify(courseRepository, times(1)).save(course); // Vérifier que save a été appelé
    }

    @Test
    public void testRetrieveCourse() {
        Long courseId = 1L;
        Course course = new Course(); // Initialisez les propriétés selon vos besoins

        // Configurer le comportement du mock
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Appeler la méthode à tester
        Course result = courseServices.retrieveCourse(courseId);

        // Vérifier le résultat
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(courseId); // Vérifier que findById a été appelé
    }

    @Test
    public void testRetrieveCourseNotFound() {
        Long courseId = 1L;

        // Configurer le comportement du mock pour ne rien retourner
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Appeler la méthode à tester
        Course result = courseServices.retrieveCourse(courseId);

        // Vérifier que le résultat est null
        assertNull(result);
        verify(courseRepository, times(1)).findById(courseId); // Vérifier que findById a été appelé
    }
}
