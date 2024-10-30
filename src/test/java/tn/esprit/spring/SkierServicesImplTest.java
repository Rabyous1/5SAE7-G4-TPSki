package tn.esprit.spring;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository ;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private IRegistrationRepository registrationRepository;


    @InjectMocks
    private SkierServicesImpl skierServices;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveAllSkiers() {
        List<Skier> skiers = new ArrayList<>();
        skiers.add(new Skier("John", "Doe", LocalDate.now(), "City", new Subscription(), null));

        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveAllSkiers();

        assertEquals(1, result.size());
    }

    @Test
    public void testAddSkier() {
        Skier skier = new Skier("Alice", "Smith", LocalDate.now(), "City", new Subscription(), null);
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);

        assertEquals(subscription.getStartDate().plusYears(1), result.getSubscription().getEndDate());
    }

    @Test
    public void testAssignSkierToSubscription() {
        Skier skier = new Skier("Bob", "Johnson", LocalDate.now(), "City", new Subscription(), null);
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);

        assertEquals(subscription, result.getSubscription());
    }

    @Test
    public void testRemoveSkier() {
        Long numSkier = 1L;

        skierServices.removeSkier(numSkier);

        verify(skierRepository).deleteById(numSkier); // Vérifie que deleteById a été appelé avec le bon paramètre
    }

    @Test
    public void testRetrieveSkier() {
        Long numSkier = 1L;
        Skier skier = new Skier("John", "Doe", LocalDate.now(), "City", new Subscription(), null);

        when(skierRepository.findById(numSkier)).thenReturn(Optional.of(skier));

        Skier result = skierServices.retrieveSkier(numSkier);

        assertEquals(skier, result); // Vérifie que le skieur retourné est le bon

        // Test lorsque le skieur n'est pas trouvé
        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());

        Skier resultNotFound = skierServices.retrieveSkier(2L);

        assertNull(resultNotFound); // Vérifie que null est retourné lorsque le skieur n'est pas trouvé
    }

    @Test
    public void testAssignSkierToPiste() {
        Long numSkieur = 1L;
        Long numPiste = 1L;

        Skier skier = new Skier("Alice", "Smith", LocalDate.now(), "City", new Subscription(), null);
        Piste piste = new Piste("Piste name", "Description");

        when(skierRepository.findById(numSkieur)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste));

        when(skierRepository.save(any(Skier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Skier result = skierServices.assignSkierToPiste(numSkieur, numPiste);

        assertNotNull(result);
        assertEquals(skier, result);
        assertEquals(1, result.getPistes().size());
        assertTrue(result.getPistes().contains(piste));

        verify(skierRepository).save(any(Skier.class));
    }
    @Test
    public void testRetrieveSkiersBySubscriptionType() {
        TypeSubscription subscriptionType = TypeSubscription.ANNUAL;

        Skier skier1 = new Skier("Alice", "Smith", LocalDate.now(), "City", new Subscription(TypeSubscription.MONTHLY), null);
        Skier skier2 = new Skier("Bob", "Johnson", LocalDate.now(), "Town", new Subscription(TypeSubscription.SEMESTRIEL), null);

        List<Skier> skiers = new ArrayList<>();
        skiers.add(skier1);
        skiers.add(skier2);

        when(skierRepository.findBySubscription_TypeSub(subscriptionType)).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(subscriptionType);

        assertEquals(2, result.size()); // Vérifie que le bon nombre de skieurs est retourné
        assertEquals(skier1, result.get(0)); // Vérifie le premier skieur retourné
        assertEquals(skier2, result.get(1)); // Vérifie le deuxième skieur retourné
    }


}