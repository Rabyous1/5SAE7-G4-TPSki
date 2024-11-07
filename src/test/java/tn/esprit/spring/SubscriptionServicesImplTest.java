package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2023, 1, 1));

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionService.addSubscription(subscription);

        assertEquals(LocalDate.of(2024, 1, 1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertEquals(1L, result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }
}
