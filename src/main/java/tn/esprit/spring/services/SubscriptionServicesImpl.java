package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionServicesImpl implements ISubscriptionServices {

    private final ISubscriptionRepository subscriptionRepository;
    private final ISkierRepository skierRepository;

    @Override
    public Subscription addSubscription(Subscription subscription) {
        log.info("Adding subscription of type: {}", subscription.getTypeSub());
        switch (subscription.getTypeSub()) {
            case ANNUAL:
                subscription.setEndDate(subscription.getStartDate().plusYears(1));
                log.debug("Set end date for ANNUAL subscription: {}", subscription.getEndDate());
                break;
            case SEMESTRIEL:
                subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                log.debug("Set end date for SEMESTRIEL subscription: {}", subscription.getEndDate());
                break;
            case MONTHLY:
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                log.debug("Set end date for MONTHLY subscription: {}", subscription.getEndDate());
                break;
            default:
                log.error("Unknown subscription type: {}", subscription.getTypeSub());
                break;
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        log.info("Subscription added successfully with ID: {}", savedSubscription.getNumSub());
        return savedSubscription;
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        log.info("Updating subscription with ID: {}", subscription.getNumSub());
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        log.info("Subscription updated successfully with ID: {}", updatedSubscription.getNumSub());
        return updatedSubscription;
    }

    @Override
    public Subscription retrieveSubscriptionById(Long numSubscription) {
        log.info("Retrieving subscription with ID: {}", numSubscription);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        if (subscription != null) {
            log.info("Subscription retrieved: {}", subscription);
        } else {
            log.warn("Subscription with ID {} not found", numSubscription);
        }
        return subscription;
    }

    @Override
    public Set<Subscription> getSubscriptionByType(TypeSubscription type) {
        log.info("Retrieving subscriptions of type: {}", type);
        Set<Subscription> subscriptions = subscriptionRepository.findByTypeSubOrderByStartDateAsc(type);
        log.info("Number of subscriptions found: {}", subscriptions.size());
        return subscriptions;
    }

    @Override
    public List<Subscription> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving subscriptions between dates {} and {}", startDate, endDate);
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate);
        log.info("Number of subscriptions found: {}", subscriptions.size());
        return subscriptions;
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *")
    public void retrieveSubscriptions() {
        log.info("Scheduled task: retrieve subscriptions sorted by end date.");
        for (Subscription sub : subscriptionRepository.findDistinctOrderByEndDateAsc()) {
            Skier skier = skierRepository.findBySubscription(sub);
            log.info("Subscription ID: {} | End Date: {} | Skier: {} {}",
                    sub.getNumSub(), sub.getEndDate(), skier.getFirstName(), skier.getLastName());
        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void showMonthlyRecurringRevenue() {
        log.info("Calculating monthly recurring revenue.");
        Float revenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL) / 6
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL) / 12;
        log.info("Monthly Revenue = {}", revenue);
    }
}
