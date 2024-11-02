package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSubscription_Annual() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionService.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(subscription.getStartDate().plusYears(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById_Found() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveSubscriptionById_NotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertNull(result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = Set.of(new Subscription(), new Subscription());

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionService.getSubscriptionByType(TypeSubscription.MONTHLY);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY);
    }
}
